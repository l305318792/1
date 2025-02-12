package com.yunchuan.medical.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * 业务类型收入统计DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessTypeIncomeDTO {
    /**
     * 业务类型
     */
    private String businessType;
    
    /**
     * 业务类型名称
     */
    private String businessTypeName;
    
    /**
     * 收入金额
     */
    private BigDecimal amount;
    
    /**
     * 占比
     */
    private BigDecimal percentage;
    
    /**
     * 交易笔数
     */
    private Integer transactionCount;
} 