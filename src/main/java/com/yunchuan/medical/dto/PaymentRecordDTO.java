package com.yunchuan.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "支付记录DTO")
public class PaymentRecordDTO {
    
    /**
     * ID
     */
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
     * 业务ID(关联ID)
     */
    private String businessId;

    /**
     * 业务类型(appointment-预约挂号, consultation-在线咨询)
     */
    private String businessType;
    
    /**
     * 支付金额
     */
    private BigDecimal amount;
    
    /**
     * 支付方式(alipay-支付宝, wechat-微信支付)
     */
    private String paymentMethod;
    
    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;
    
    /**
     * 状态(pending-待支付, success-支付成功, failed-支付失败, refunded-已退款)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 