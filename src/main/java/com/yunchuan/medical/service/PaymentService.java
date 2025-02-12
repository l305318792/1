package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.PaymentRecordDTO;
import java.math.BigDecimal;
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

    /**
     * 退款
     * @param paymentId 支付记录ID
     * @param amount 退款金额
     * @return 更新后的支付记录
     */
    PaymentRecordDTO refund(String paymentId, BigDecimal amount);

    /**
     * 更新支付记录
     * @param paymentRecord 支付记录信息
     * @return 更新后的支付记录
     */
    PaymentRecordDTO updatePaymentRecord(PaymentRecordDTO paymentRecord);

    /**
     * 根据状态获取支付记录列表
     * @param userId 用户ID，可选
     * @param status 支付状态
     * @return 支付记录列表
     */
    List<PaymentRecordDTO> getPaymentsByStatus(String userId, String status);
} 