package com.yunchuan.medical.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 医生评价表单DTO
 */
@Data
public class DoctorRatingFormDTO {
    @NotBlank(message = "预约ID不能为空")
    private String appointmentId;
    
    @NotNull(message = "服务态度评分不能为空")
    @DecimalMin(value = "1.0", message = "服务态度评分最小为1.0")
    @DecimalMax(value = "5.0", message = "服务态度评分最大为5.0")
    private BigDecimal serviceAttitude;
    
    @NotNull(message = "医疗技术评分不能为空")
    @DecimalMin(value = "1.0", message = "医疗技术评分最小为1.0")
    @DecimalMax(value = "5.0", message = "医疗技术评分最大为5.0")
    private BigDecimal medicalSkill;
    
    @NotNull(message = "治疗效果评分不能为空")
    @DecimalMin(value = "1.0", message = "治疗效果评分最小为1.0")
    @DecimalMax(value = "5.0", message = "治疗效果评分最大为5.0")
    private BigDecimal medicalEffect;
    
    @NotBlank(message = "评价内容不能为空")
    private String comment;
} 