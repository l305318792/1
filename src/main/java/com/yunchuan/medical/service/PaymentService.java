package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.PaymentRecordDTO;

import java.util.List;

/**
 * 支付服务接口
 */
public interface PaymentService {
    
    /**
     * 创建支付记录
     */
    PaymentRecordDTO createPayment(PaymentRecordDTO paymentRecord);
    
    /**
     * 更新支付状态
     */
    PaymentRecordDTO updatePaymentStatus(String id, String status);
    
    /**
     * 获取支付记录
     */
    PaymentRecordDTO getPaymentById(String id);

    /**
     * 获取用户支付记录列表
     */
    List<PaymentRecordDTO> getPaymentsByUserId(String userId);

    /**
     * 获取所有支付记录
     */
    List<PaymentRecordDTO> getAllPayments();
} 