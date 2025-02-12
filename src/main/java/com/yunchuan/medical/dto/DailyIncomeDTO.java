package com.yunchuan.medical.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 每日收入统计DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyIncomeDTO {
    /**
     * 日期
     */
    private LocalDate date;
    
    /**
     * 总收入
     */
    private BigDecimal totalIncome;
    
    /**
     * 预约挂号收入
     */
    private BigDecimal appointmentIncome;
    
    /**
     * 在线问诊收入
     */
    private BigDecimal consultationIncome;
    
    /**
     * 处方收入
     */
    private BigDecimal prescriptionIncome;
    
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    
    /**
     * 实际收入
     */
    private BigDecimal actualIncome;
    
    /**
     * 交易笔数
     */
    private Integer transactionCount;
} 