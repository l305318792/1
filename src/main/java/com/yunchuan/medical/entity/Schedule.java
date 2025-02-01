package com.yunchuan.medical.entity;

import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 排班实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("schedule")
public class Schedule {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**
     * 医生ID
     */
    @TableField("doctor_id")
    private String doctorId;
    
    /**
     * 科室ID
     */
    @TableField("department_id")
    private String departmentId;
    
    /**
     * 排班日期
     */
    @TableField("schedule_date")
    private LocalDate scheduleDate;
    
    /**
     * 排班时段（MORNING-上午，AFTERNOON-下午）
     */
    @TableField("period")
    private String period;
    
    /**
     * 最大预约人数
     */
    @TableField("max_appointments")
    private Integer maxAppointments;
    
    /**
     * 已预约人数
     */
    @TableField("appointed_count")
    private Integer appointedCount = 0;
    
    /**
     * 排班状态（ENABLED-启用，DISABLED-停诊）
     */
    @TableField("status")
    private String status;
    
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime = LocalDateTime.now();
    
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime = LocalDateTime.now();
} 