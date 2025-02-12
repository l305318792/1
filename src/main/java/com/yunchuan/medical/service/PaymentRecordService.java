package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.dto.FinanceStatisticsDTO;
import com.yunchuan.medical.dto.DailyIncomeDTO;
import com.yunchuan.medical.dto.BusinessTypeIncomeDTO;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * 支付记录服务接口
 */
public interface PaymentRecordService {
    
    /**
     * 创建支付记录
     * @param paymentRecord 支付记录信息
     * @return 创建后的支付记录
     */
    PaymentRecordDTO createPayment(PaymentRecordDTO paymentRecord);
    
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

    /**
     * 获取财务统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 财务统计数据
     */
    FinanceStatisticsDTO getFinanceStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 获取每日收入统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 每日收入统计列表
     */
    List<DailyIncomeDTO> getDailyIncome(LocalDate startDate, LocalDate endDate);

    /**
     * 获取业务类型收入统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 业务类型收入统计列表
     */
    List<BusinessTypeIncomeDTO> getBusinessTypeStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 导出支付记录
     * @param userId 用户ID
     * @param businessType 业务类型
     * @param status 支付状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param response HTTP响应
     */
    void exportPaymentRecords(String userId, String businessType, String status, 
                            LocalDate startDate, LocalDate endDate, 
                            HttpServletResponse response);
} 