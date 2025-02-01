package com.yunchuan.medical.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款记录数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundDTO {
    /**
     * ID
     */
    private String id;
    
    /**
     * 支付记录ID
     */
    private String paymentId;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 用户姓名
     */
    private String userName;
    
    /**
     * 订单类型（APPOINTMENT-预约挂号，PRESCRIPTION-处方药品）
     */
    private String orderType;
    
    /**
     * 订单ID
     */
    private String orderId;
    
    /**
     * 退款金额
     */
    private BigDecimal amount;
    
    /**
     * 退款原因
     */
    private String reason;
    
    /**
     * 拒绝原因
     */
    private String rejectReason;
    
    /**
     * 退款状态（PENDING-待处理，COMPLETED-已完成，REJECTED-已拒绝）
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 退款时间
     */
    private LocalDateTime refundTime;
} 