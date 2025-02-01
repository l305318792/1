package com.yunchuan.medical.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 问诊记录实体类
 */
@Data
@TableName("consultation")
public class Consultation {
    /**
     * 主键ID
     */
    @TableId
    private String id;

    /**
     * 患者ID
     */
    @TableField("user_id")
    private String userId;

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
     * 预约ID
     */
    @TableField("appointment_id")
    private String appointmentId;

    /**
     * 症状描述
     */
    @TableField("symptoms")
    private String symptoms;

    /**
     * 诊断结果
     */
    @TableField("diagnosis")
    private String diagnosis;

    /**
     * 治疗方案
     */
    @TableField("treatment")
    private String treatment;

    /**
     * 状态：PENDING-待就诊，IN_PROGRESS-就诊中，COMPLETED-已完成
     */
    @TableField("status")
    private String status;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
} 