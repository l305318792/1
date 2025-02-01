package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.PaymentRecordDTO;
import java.util.List;

/**
 * 支付记录服务接口
 */
public interface PaymentRecordService {
    
    /**
     * 获取支付记录列表
     * @param userId 用户ID
     * @return 支付记录列表
     */
    List<PaymentRecordDTO> getPaymentRecordList(String userId);
    
    /**
     * 根据ID获取支付记录
     * @param id 支付记录ID
     * @return 支付记录信息
     */
    PaymentRecordDTO getPaymentRecordById(String id);
    
    /**
     * 创建支付记录
     * @param paymentRecord 支付记录信息
     * @return 创建后的支付记录
     */
    PaymentRecordDTO createPaymentRecord(PaymentRecordDTO paymentRecord);
    
    /**
     * 更新支付记录
     * @param paymentRecord 支付记录信息
     * @return 更新后的支付记录
     */
    PaymentRecordDTO updatePaymentRecord(PaymentRecordDTO paymentRecord);
    
    /**
     * 删除支付记录
     * @param id 支付记录ID
     */
    void deletePaymentRecord(String id);
} 