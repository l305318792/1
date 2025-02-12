package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.entity.PaymentRecord;
import com.yunchuan.medical.mapper.PaymentRecordMapper;
import com.yunchuan.medical.service.PaymentService;
import com.yunchuan.medical.service.AppointmentService;
import com.yunchuan.medical.service.ConsultationService;
import com.yunchuan.medical.util.SecurityUtil;
import com.yunchuan.medical.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.context.annotation.Lazy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 支付服务实现类
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRecordMapper paymentRecordMapper;
    
    @Lazy
    private final AppointmentService appointmentService;
    
    @Lazy
    private final ConsultationService consultationService;

    public PaymentServiceImpl(PaymentRecordMapper paymentRecordMapper,
                            @Lazy AppointmentService appointmentService,
                            @Lazy ConsultationService consultationService) {
        this.paymentRecordMapper = paymentRecordMapper;
        this.appointmentService = appointmentService;
        this.consultationService = consultationService;
    }

    @Override
    @Transactional
    public PaymentRecordDTO createPayment(PaymentRecordDTO paymentRecord) {
        PaymentRecord record = new PaymentRecord();
        BeanUtils.copyProperties(paymentRecord, record);
        
        // 手动生成ID
        String id = UUID.randomUUID().toString().replace("-", "");
        record.setId(id);
        record.setUserId(SecurityUtil.getCurrentUserId());
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        if (record.getStatus() == null) {
            record.setStatus("pending");
        }
        
        paymentRecordMapper.insert(record);
        BeanUtils.copyProperties(record, paymentRecord);
        return paymentRecord;
    }

    @Override
    @Transactional
    public PaymentRecordDTO updatePaymentStatus(String id, String status) {
        logger.info("更新支付状态: id={}, status={}", id, status);
        
        if (id == null || status == null) {
            throw new BusinessException("参数不能为空");
        }
        
        // 1. 获取支付记录
        PaymentRecord record = paymentRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }
        
        // 2. 更新支付状态
        record.setStatus(status);
        if ("SUCCESS".equals(status)) {
            record.setPaymentTime(LocalDateTime.now());
        }
        record.setUpdateTime(LocalDateTime.now());
        
        if (paymentRecordMapper.updateById(record) <= 0) {
            throw new BusinessException("更新支付状态失败");
        }
        
        // 3. 更新关联业务状态
        if ("SUCCESS".equals(status)) {
            switch (record.getBusinessType()) {
                case "APPOINTMENT":
                    // 更新预约状态为已支付
                    appointmentService.payAppointment(record.getBusinessId());
                    break;
                case "CONSULTATION":
                    // 更新问诊状态为已支付
                    consultationService.payConsultation(record.getBusinessId());
                    break;
                default:
                    logger.warn("未知的业务类型: {}", record.getBusinessType());
            }
        }
        
        // 4. 返回更新后的记录
        PaymentRecordDTO result = new PaymentRecordDTO();
        BeanUtils.copyProperties(record, result);
        return result;
    }

    @Override
    public PaymentRecordDTO getPaymentById(String id) {
        PaymentRecord record = paymentRecordMapper.selectById(id);
        if (record == null) {
            return null;
        }
        
        PaymentRecordDTO result = new PaymentRecordDTO();
        BeanUtils.copyProperties(record, result);
        return result;
    }

    @Override
    public List<PaymentRecordDTO> getPaymentsByUserId(String userId) {
        List<PaymentRecord> records = paymentRecordMapper.selectByUserId(userId);
        return records.stream().map(record -> {
            PaymentRecordDTO dto = new PaymentRecordDTO();
            BeanUtils.copyProperties(record, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PaymentRecordDTO> getAllPayments() {
        List<PaymentRecord> records = paymentRecordMapper.selectAll();
        return records.stream().map(record -> {
            PaymentRecordDTO dto = new PaymentRecordDTO();
            BeanUtils.copyProperties(record, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentRecordDTO refund(String paymentId, BigDecimal amount) {
        logger.info("开始处理退款请求: paymentId={}, amount={}", paymentId, amount);
        
        // 1. 查询支付记录
        PaymentRecord record = paymentRecordMapper.selectById(paymentId);
        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }
        
        // 2. 验证支付状态
        if (!"success".equalsIgnoreCase(record.getStatus())) {
            throw new BusinessException("只有支付成功的订单才能退款");
        }
        
        // 3. 验证退款金额
        if (amount.compareTo(record.getAmount()) > 0) {
            throw new BusinessException("退款金额不能大于支付金额");
        }
        
        // 4. 更新支付记录状态
        record.setStatus("REFUNDED");
        record.setUpdateTime(LocalDateTime.now());
        
        // 5. 保存更新
        if (paymentRecordMapper.updateById(record) <= 0) {
            throw new BusinessException("更新支付记录失败");
        }
        
        logger.info("退款处理完成: paymentId={}", paymentId);
        
        // 6. 返回更新后的记录
        PaymentRecordDTO result = new PaymentRecordDTO();
        BeanUtils.copyProperties(record, result);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentRecordDTO updatePaymentRecord(PaymentRecordDTO paymentRecordDTO) {
        logger.info("更新支付记录: id={}", paymentRecordDTO.getId());
        
        // 1. 验证支付记录是否存在
        PaymentRecord record = paymentRecordMapper.selectById(paymentRecordDTO.getId());
        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }
        
        // 2. 更新支付记录信息
        BeanUtils.copyProperties(paymentRecordDTO, record);
        record.setUpdateTime(LocalDateTime.now());
        
        // 3. 保存更新
        if (paymentRecordMapper.updateById(record) <= 0) {
            throw new BusinessException("更新支付记录失败");
        }
        
        logger.info("支付记录更新成功: id={}", record.getId());
        
        // 4. 返回更新后的记录
        PaymentRecordDTO result = new PaymentRecordDTO();
        BeanUtils.copyProperties(record, result);
        return result;
    }

    @Override
    public List<PaymentRecordDTO> getPaymentsByStatus(String userId, String status) {
        logger.info("查询支付记录 - userId: {}, status: {}", userId, status);
        
        // 构建查询条件
        LambdaQueryWrapper<PaymentRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PaymentRecord::getStatus, status);
        if (userId != null) {
            queryWrapper.eq(PaymentRecord::getUserId, userId);
        }
        queryWrapper.orderByDesc(PaymentRecord::getCreateTime);
        
        // 执行查询
        List<PaymentRecord> records = paymentRecordMapper.selectList(queryWrapper);
        
        // 转换为DTO
        return records.stream()
                .map(record -> {
                    PaymentRecordDTO dto = new PaymentRecordDTO();
                    BeanUtils.copyProperties(record, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
} 