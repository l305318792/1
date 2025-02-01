package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.constant.Constants;
import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.entity.PaymentRecord;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.mapper.PaymentRecordMapper;
import com.yunchuan.medical.service.PaymentRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 支付记录服务实现类
 */
@Service
public class PaymentRecordServiceImpl implements PaymentRecordService {

    private final PaymentRecordMapper paymentRecordMapper;

    public PaymentRecordServiceImpl(PaymentRecordMapper paymentRecordMapper) {
        this.paymentRecordMapper = paymentRecordMapper;
    }

    @Override
    public List<PaymentRecordDTO> getPaymentRecordList(String userId) {
        List<PaymentRecord> records;
        if (userId != null) {
            records = paymentRecordMapper.selectByUserId(userId);
        } else {
            records = paymentRecordMapper.selectAll();
        }
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentRecordDTO getPaymentRecordById(String id) {
        PaymentRecord record = paymentRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }
        return convertToDTO(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentRecordDTO createPaymentRecord(PaymentRecordDTO recordDTO) {
        PaymentRecord record = new PaymentRecord();
        BeanUtils.copyProperties(recordDTO, record);
        
        // 设置基础字段
        record.setId(UUID.randomUUID().toString());
        record.setStatus(Constants.PAYMENT_PENDING);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        
        if (paymentRecordMapper.insert(record) > 0) {
            return convertToDTO(record);
        }
        throw new BusinessException("创建支付记录失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentRecordDTO updatePaymentRecord(PaymentRecordDTO recordDTO) {
        PaymentRecord record = paymentRecordMapper.selectById(recordDTO.getId());
        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }
        
        // 验证状态值
        if (recordDTO.getStatus() != null && 
            !Constants.PAYMENT_PENDING.equals(recordDTO.getStatus()) && 
            !Constants.PAYMENT_SUCCESS.equals(recordDTO.getStatus()) && 
            !Constants.PAYMENT_FAILED.equals(recordDTO.getStatus()) &&
            !Constants.PAYMENT_REFUNDED.equals(recordDTO.getStatus())) {
            throw new BusinessException("无效的状态值");
        }
        
        BeanUtils.copyProperties(recordDTO, record);
        record.setUpdateTime(LocalDateTime.now());
        
        if (paymentRecordMapper.updateById(record) > 0) {
            return convertToDTO(record);
        }
        throw new BusinessException("更新支付记录失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePaymentRecord(String id) {
        PaymentRecord record = paymentRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }
        
        if (paymentRecordMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除支付记录失败");
        }
    }

    private PaymentRecordDTO convertToDTO(PaymentRecord record) {
        if (record == null) {
            return null;
        }
        PaymentRecordDTO dto = new PaymentRecordDTO();
        BeanUtils.copyProperties(record, dto);
        return dto;
    }
} 