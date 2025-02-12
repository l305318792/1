package com.yunchuan.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款申请DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "退款申请DTO")
public class RefundDTO {
    
    @Schema(description = "退款记录ID")
    private String id;
    
    @NotBlank(message = "支付记录ID不能为空")
    @Schema(description = "支付记录ID")
    private String paymentId;
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "用户姓名")
    private String userName;
    
    @NotNull(message = "退款金额不能为空")
    @DecimalMin(value = "0.01", message = "退款金额必须大于0")
    @Schema(description = "退款金额")
    private BigDecimal amount;
    
    @Schema(description = "业务类型(APPOINTMENT-预约挂号,CONSULTATION-在线问诊,PRESCRIPTION-处方支付)")
    private String businessType;
    
    @Schema(description = "业务ID(关联的业务记录ID)")
    private String businessId;
    
    @NotBlank(message = "退款原因不能为空")
    @Schema(description = "退款原因")
    private String reason;
    
    @Schema(description = "退款状态(PENDING-待处理,APPROVED-已同意,REJECTED-已拒绝)")
    private String status;
    
    @Schema(description = "拒绝原因")
    private String rejectReason;
    
    @Schema(description = "处理人ID")
    private String operatorId;
    
    @Schema(description = "退款时间")
    private LocalDateTime refundTime;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 