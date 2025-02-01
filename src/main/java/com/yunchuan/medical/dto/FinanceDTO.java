package com.yunchuan.medical.dto;

import lombok.Data;

/**
 * 财务
 */
@Data
public class FinanceDTO {
    
    /**
     * ID
     */
    private String id;

    /**
     * 类型
     */
    private String type;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private String status;
} 