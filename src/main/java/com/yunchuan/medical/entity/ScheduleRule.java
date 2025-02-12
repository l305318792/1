package com.yunchuan.medical.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 排班规则表
 * @author yunchuan
 * @since 2025-02-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("schedule_rule")
public class ScheduleRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId("id")
    private String id;

    /**
     * 医生ID
     */
    @TableField("doctor_id")
    private String doctorId;

    /**
     * 规则类型：FIXED-固定班次，ROTATION-轮转班次，FLEXIBLE-弹性排班
     */
    @TableField("rule_type")
    private String ruleType;

    /**
     * 适用星期（周一到周日：1-7）
     */
    @TableField("week_days")
    private String weekDays;

    /**
     * 时间段：MORNING-上午，AFTERNOON-下午
     */
    @TableField("periods")
    private String periods;

    /**
     * 规则生效开始日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 规则生效结束日期
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 最大预约数
     */
    @TableField("max_appointments")
    private Integer maxAppointments;

    /**
     * 规则优先级
     */
    @TableField("priority")
    private Integer priority = 0;

    /**
     * 状态：ENABLED-启用，DISABLED-禁用
     */
    @TableField("status")
    private String status = "ENABLED";

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
} 