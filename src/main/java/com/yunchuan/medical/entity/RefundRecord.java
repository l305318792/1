package com.yunchuan.medical.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款记录实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("refund_record")
public class RefundRecord {
    
    /**
     * 退款记录ID
     */
    @TableId("id")
    private String id;
    
    /**
     * 支付记录ID
     */
    @TableField("payment_id")
    private String paymentId;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    
    /**
     * 退款金额
     */
    @TableField("amount")
    private BigDecimal amount;
    
    /**
     * 业务类型
     */
    @TableField("business_type")
    private String businessType;
    
    /**
     * 业务ID
     */
    @TableField("business_id")
    private String businessId;
    
    /**
     * 退款原因
     */
    @TableField("reason")
    private String reason;
    
    /**
     * 状态
     */
    @TableField("status")
    private String status;
    
    /**
     * 拒绝原因
     */
    @TableField("reject_reason")
    private String rejectReason;
    
    /**
     * 处理人ID
     */
    @TableField("operator_id")
    private String operatorId;
    
    /**
     * 退款时间
     */
    @TableField("refund_time")
    private LocalDateTime refundTime;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
} 