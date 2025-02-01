package com.yunchuan.medical.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 更新医生DTO
 */
@Data
public class UpdateDoctorDTO {
    
    /**
     * ID
     */
    @NotBlank(message = "ID不能为空")
    private String id;
    
    /**
     * 科室ID
     */
    private String departmentId;
    
    /**
     * 职称(chief-主任医师, associate_chief-副主任医师, attending-主治医师, resident-住院医师)
     */
    private String title;
    
    /**
     * 简介
     */
    private String introduction;
    
    /**
     * 擅长
     */
    private String specialty;
    
    /**
     * 咨询费用
     */
    private BigDecimal consultationFee;
    
    /**
     * 状态(active-在职, inactive-离职, on_leave-休假)
     */
    private String status;
} 