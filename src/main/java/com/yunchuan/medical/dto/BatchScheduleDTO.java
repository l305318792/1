package com.yunchuan.medical.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * 批量排班DTO
 * @author yunchuan
 * @since 1.0.0
 */
@Data
public class BatchScheduleDTO {
    /**
     * 医生ID
     */
    private String doctorId;
    
    /**
     * 科室ID
     */
    private String departmentId;
    
    /**
     * 排班日期列表
     */
    private List<LocalDate> scheduleDates;
    
    /**
     * 时段列表
     */
    private List<String> periods;
    
    /**
     * 最大预约人数
     */
    private Integer maxAppointments;
    
    /**
     * 备注
     */
    private String remark;
} 