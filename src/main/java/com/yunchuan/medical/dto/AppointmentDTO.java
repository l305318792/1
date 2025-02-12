package com.yunchuan.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 预约数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "预约数据传输对象")
public class AppointmentDTO {
    @Schema(description = "预约ID")
    private String id;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "患者姓名")
    private String patientName;

    @Schema(description = "患者手机号")
    private String patientPhone;

    @NotBlank(message = "医生ID不能为空")
    @Schema(description = "医生ID")
    private String doctorId;

    @Schema(description = "医生姓名")
    private String doctorName;

    @NotBlank(message = "科室ID不能为空")
    @Schema(description = "科室ID")
    private String departmentId;

    @Schema(description = "科室名称")
    private String departmentName;

    @NotBlank(message = "排班ID不能为空")
    @Schema(description = "排班ID")
    private String scheduleId;

    @Schema(description = "预约状态：UNPAID-待支付，PAID-已支付，COMPLETED-已完成，CANCELLED-已取消")
    private String status;

    @Schema(description = "序号")
    private Integer sequenceNumber;

    @NotNull(message = "预约时间不能为空")
    @Schema(description = "预约时间")
    private LocalDateTime appointmentTime;

    @Schema(description = "就诊时间")
    private LocalDateTime visitTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "审核意见")
    private String reviewReason;

    @Schema(description = "预约金额")
    private BigDecimal amount;

    @Schema(description = "支付记录ID")
    private String paymentId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}