package com.yunchuan.medical.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * 排班表单DTO
 * @author yunchuan
 * @since 1.0.0
 */
@Data
public class ScheduleFormDTO {
    /**
     * 医生ID
     */
    private String doctorId;
    
    /**
     * 科室ID
     */
    private String departmentId;
    
    /**
     * 排班日期
     */
    private LocalDate scheduleDate;
    
    /**
     * 时段
     */
    private String period;
    
    /**
     * 最大预约人数
     */
    private Integer maxAppointments;
    
    /**
     * 重复类型（ONCE-单次，DAILY-每天，WEEKLY-每周）
     */
    private String repeatType;
    
    /**
     * 重复次数
     */
    private Integer repeatCount;
    
    /**
     * 重复日期（用于每周重复时指定星期几）
     */
    private List<Integer> repeatDays;
    
    /**
     * 备注
     */
    private String remark;
} 