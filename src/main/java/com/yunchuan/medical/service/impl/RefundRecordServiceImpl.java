package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.dto.RefundDTO;
import com.yunchuan.medical.entity.RefundRecord;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.mapper.RefundRecordMapper;
import com.yunchuan.medical.service.RefundRecordService;
import com.yunchuan.medical.service.PaymentService;
import com.yunchuan.medical.util.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 退款记录服务实现类
 */
@Service
public class RefundRecordServiceImpl implements RefundRecordService {
    
    private static final Logger log = LoggerFactory.getLogger(RefundRecordServiceImpl.class);
    
    private final RefundRecordMapper refundRecordMapper;
    private final PaymentService paymentService;
    
    public RefundRecordServiceImpl(RefundRecordMapper refundRecordMapper, PaymentService paymentService) {
        this.refundRecordMapper = refundRecordMapper;
        this.paymentService = paymentService;
    }
    
    @Override
    public IPage<RefundDTO> getRefundPage(Page<RefundDTO> page) {
        // 创建实体分页对象
        Page<RefundRecord> recordPage = new Page<>(page.getCurrent(), page.getSize());
        
        // 使用 MyBatis-Plus 的分页查询
        QueryWrapper<RefundRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        
        // 先查询总记录数
        Long total = refundRecordMapper.selectCount(queryWrapper);
        
        // 执行分页查询
        List<RefundRecord> records = refundRecordMapper.selectList(
            queryWrapper.last(String.format("LIMIT %d,%d", 
                (page.getCurrent() - 1) * page.getSize(), 
                page.getSize()))
        );
        
        // 转换为DTO
        List<RefundDTO> dtoList = records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
        // 构建返回结果
        Page<RefundDTO> dtoPage = new Page<>();
        dtoPage.setRecords(dtoList);
        dtoPage.setCurrent(page.getCurrent());
        dtoPage.setSize(page.getSize());
        dtoPage.setTotal(total);
        // 计算总页数
        long pages = (total + page.getSize() - 1) / page.getSize();
        dtoPage.setPages(pages);
        
        return dtoPage;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundDTO createRefund(RefundDTO refundDTO) {
        // 1. 参数校验
        if (refundDTO.getPaymentId() == null || refundDTO.getAmount() == null) {
            throw new BusinessException("支付记录ID和退款金额不能为空");
        }
        
        // 2. 获取支付记录信息
        PaymentRecordDTO payment = paymentService.getPaymentById(refundDTO.getPaymentId());
        if (payment == null) {
            throw new BusinessException("支付记录不存在");
        }
        
        // 3. 验证支付状态
        if (!"success".equalsIgnoreCase(payment.getStatus())) {
            throw new BusinessException("只有支付成功的记录才能申请退款");
        }
        
        // 4. 验证退款金额
        if (refundDTO.getAmount().compareTo(payment.getAmount()) > 0) {
            throw new BusinessException("退款金额不能大于支付金额");
        }
        
        // 5. 创建退款记录
        RefundRecord record = new RefundRecord();
        BeanUtils.copyProperties(refundDTO, record);
        
        // 6. 设置基本信息
        record.setId(UUID.randomUUID().toString().replace("-", ""));
        record.setUserId(SecurityUtil.getCurrentUserId());
        record.setStatus("PENDING");
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        
        // 7. 从支付记录复制业务信息
        record.setBusinessType(payment.getBusinessType());
        record.setBusinessId(payment.getBusinessId());
        
        // 8. 保存记录
        refundRecordMapper.insert(record);
        
        // 9. 返回结果
        return convertToDTO(record);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundDTO processRefund(String id, String action, String reason) {
        log.info("开始处理退款申请 - id: {}, action: {}, reason: {}", id, action, reason);
        
        // 1. 获取退款记录
        RefundRecord record = refundRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("退款记录不存在");
        }
        
        // 2. 验证当前状态
        validateStatusTransition(record.getStatus(), action);
        
        // 3. 获取原支付记录
        PaymentRecordDTO payment = paymentService.getPaymentById(record.getPaymentId());
        if (payment == null) {
            throw new BusinessException("原支付记录不存在");
        }
        
        // 4. 验证支付记录状态
        if ("REFUNDED".equals(payment.getStatus())) {
            throw new BusinessException("该订单已退款，不能重复退款");
        }
        
        // 5. 验证退款金额不超过支付金额
        if (record.getAmount().compareTo(payment.getAmount()) > 0) {
            throw new BusinessException("退款金额不能大于支付金额");
        }
        
        // 6. 更新退款记录状态
        record.setOperatorId(SecurityUtil.getCurrentUserId());
        record.setUpdateTime(LocalDateTime.now());
        
        if ("APPROVE".equals(action)) {
            record.setStatus("APPROVED");
            record.setRefundTime(LocalDateTime.now());
            // 更新支付记录状态
            payment.setStatus("REFUNDED");
            payment.setUpdateTime(LocalDateTime.now());
            paymentService.updatePaymentRecord(payment);
        } else if ("REJECT".equals(action)) {
            record.setStatus("REJECTED");
            record.setRejectReason(reason);
        } else {
            throw new BusinessException("无效的操作类型");
        }
        
        // 7. 保存更新
        if (refundRecordMapper.updateById(record) <= 0) {
            throw new BusinessException("更新退款记录失败");
        }
        
        log.info("退款申请处理完成 - id: {}, status: {}", id, record.getStatus());
        
        // 8. 返回更新后的记录
        return convertToDTO(record);
    }
    
    @Override
    public RefundDTO getRefundById(String id) {
        RefundRecord record = refundRecordMapper.selectById(id);
        return record != null ? convertToDTO(record) : null;
    }
    
    @Override
    public List<RefundDTO> getRefundsByUserId(String userId) {
        List<RefundRecord> records = refundRecordMapper.selectByUserId(userId);
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<RefundDTO> getRefundsByStatus(String status) {
        List<RefundRecord> records = refundRecordMapper.selectByStatus(status);
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<RefundDTO> getRefundsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        List<RefundRecord> records = refundRecordMapper.selectByTimeRange(startTime, endTime);
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 校验状态流转
     */
    private void validateStatusTransition(String currentStatus, String action) {
        if (!"PENDING".equals(currentStatus)) {
            throw new BusinessException("只能处理待处理状态的退款申请");
        }
        
        if (!Arrays.asList("APPROVED", "REJECTED").contains(action)) {
            throw new BusinessException("无效的处理动作");
        }
    }
    
    /**
     * 转换为DTO
     */
    private RefundDTO convertToDTO(RefundRecord record) {
        RefundDTO dto = new RefundDTO();
        BeanUtils.copyProperties(record, dto);
        return dto;
    }
} 