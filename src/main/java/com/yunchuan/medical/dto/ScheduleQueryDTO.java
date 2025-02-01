package com.yunchuan.medical.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 排班查询条件DTO
 */
@Data
public class ScheduleQueryDTO {
    /**
     * 医生ID
     */
    private String doctorId;
    
    /**
     * 科室ID
     */
    private String departmentId;
    
    /**
     * 开始日期
     */
    private LocalDate startDate;
    
    /**
     * 结束日期
     */
    private LocalDate endDate;
    
    /**
     * 排班状态
     */
    private String status;
} 