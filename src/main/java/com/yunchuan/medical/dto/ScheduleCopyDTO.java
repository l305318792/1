package com.yunchuan.medical.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 排班复制DTO
 * @author yunchuan
 * @since 1.0.0
 */
@Data
public class ScheduleCopyDTO {
    /**
     * 源开始日期
     */
    @NotNull(message = "源开始日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sourceStartDate;
    
    /**
     * 源结束日期
     */
    @NotNull(message = "源结束日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sourceEndDate;
    
    /**
     * 目标开始日期
     */
    @NotNull(message = "目标开始日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetStartDate;
    
    /**
     * 医生ID(可选)
     */
    private String doctorId;
    
    /**
     * 科室ID(可选)
     */
    private String departmentId;
} 