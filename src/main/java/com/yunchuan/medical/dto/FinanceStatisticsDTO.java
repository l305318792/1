package com.yunchuan.medical.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 财务统计DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceStatisticsDTO {
    /**
     * 总收入
     */
    private BigDecimal totalIncome;
    
    /**
     * 今日收入
     */
    private BigDecimal todayIncome;
    
    /**
     * 本周收入
     */
    private BigDecimal weekIncome;
    
    /**
     * 本月收入
     */
    private BigDecimal monthIncome;
    
    /**
     * 支付方式统计
     * key: 支付方式（WECHAT-微信支付, ALIPAY-支付宝）
     * value: 金额
     */
    private Map<String, BigDecimal> paymentMethodStats;
    
    /**
     * 业务类型统计
     * key: 业务类型（APPOINTMENT-预约挂号, CONSULTATION-在线问诊）
     * value: 金额
     */
    private Map<String, BigDecimal> businessTypeStats;
    
    /**
     * 退款统计
     */
    private BigDecimal totalRefund;
    
    /**
     * 实际收入（总收入-退款）
     */
    private BigDecimal actualIncome;
} 