package com.yunchuan.medical.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 预约查询DTO
 * @author yunchuan
 * @since 1.0.0
 */
@Data
public class AppointmentQueryDTO {
    /**
     * 用户ID
     */
    private String userId;
    
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
     * 状态
     */
    private String status;

    /**
     * 当前页码
     */
    private Integer current = 1;
    
    /**
     * 每页大小
     */
    private Integer size = 10;
} 