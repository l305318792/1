package com.yunchuan.medical.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 添加医生DTO
 */
@Data
public class AddDoctorDTO {
    
    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    private String userId;
    
    /**
     * 科室ID
     */
    @NotBlank(message = "科室ID不能为空")
    private String departmentId;
    
    /**
     * 职称(chief-主任医师, associate_chief-副主任医师, attending-主治医师, resident-住院医师)
     */
    @NotBlank(message = "职称不能为空")
    private String title;
    
    /**
     * 简介
     */
    @NotBlank(message = "简介不能为空")
    private String introduction;
    
    /**
     * 擅长
     */
    @NotBlank(message = "擅长领域不能为空")
    private String specialty;
    
    /**
     * 咨询费用
     */
    private BigDecimal consultationFee;
} 