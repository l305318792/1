package com.yunchuan.medical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 科室表单DTO
 * @author yunchuan
 * @since 1.0.0
 */
@Data
public class DepartmentFormDTO {
    /**
     * 科室ID
     */
    private String id;
    
    /**
     * 父级科室ID
     */
    private String parentId;
    
    /**
     * 科室名称
     */
    @NotBlank(message = "科室名称不能为空")
    private String name;
    
    /**
     * 科室介绍
     */
    private String introduction;
    
    /**
     * 排序号
     */
    @NotNull(message = "排序号不能为空")
    private Integer sortOrder;
} 