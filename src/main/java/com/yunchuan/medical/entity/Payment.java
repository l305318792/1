package com.yunchuan.medical.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付实体类
 */
@Data
@TableName("payment")
public class Payment {
    
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
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
     * 支付金额
     */
    private BigDecimal amount;
    
    /**
     * 支付状态（PENDING-待支付，SUCCESS-支付成功，FAILED-支付失败）
     */
    private String status;
    
    /**
     * 支付方式（WECHAT-微信，ALIPAY-支付宝）
     */
    private String paymentMethod;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 