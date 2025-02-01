package com.yunchuan.medical.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * 排班数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    /**
     * 主键ID
     */
    private String id;
    
    /**
     * 医生ID
     */
    private String doctorId;
    
    /**
     * 医生姓名
     */
    private String doctorName;
    
    /**
     * 科室ID
     */
    private String departmentId;
    
    /**
     * 科室名称
     */
    private String departmentName;
    
    /**
     * 排班日期
     */
    private LocalDate scheduleDate;
    
    /**
     * 排班时段
     */
    private String period;
    
    /**
     * 最大预约人数
     */
    private Integer maxAppointments;
    
    /**
     * 已预约人数
     */
    private Integer appointedCount;
    
    /**
     * 排班状态
     */
    private String status;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 