package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.entity.PaymentRecord;
import com.yunchuan.medical.mapper.PaymentRecordMapper;
import com.yunchuan.medical.service.PaymentService;
import com.yunchuan.medical.util.SecurityUtil;
import com.yunchuan.medical.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public PaymentServiceImpl(PaymentRecordMapper paymentRecordMapper) {
        this.paymentRecordMapper = paymentRecordMapper;
    }

    @Override
    @Transactional
    public PaymentRecordDTO createPayment(PaymentRecordDTO paymentRecord) {
        PaymentRecord record = new PaymentRecord();
        BeanUtils.copyProperties(paymentRecord, record);
        
        // 手动生成ID
        String id = UUID.randomUUID().toString().replace("-", "");
        record.setId(id);
        record.setUserId("1");
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
        
        // 直接更新状态
        if (paymentRecordMapper.updateStatus(id, status) <= 0) {
            throw new BusinessException("更新支付状态失败");
        }
        
        // 查询并返回更新后的记录
        PaymentRecord record = paymentRecordMapper.selectById(id);
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
} 