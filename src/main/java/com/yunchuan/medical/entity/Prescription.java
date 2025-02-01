package com.yunchuan.medical.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 处方实体类
 */
@Data
@TableName("prescription")
public class Prescription {
    @TableId
    private String id;

    @TableField("consultation_id")
    private String consultationId;

    @TableField("doctor_id")
    private String doctorId;

    @TableField("patient_id")
    private String patientId;

    @TableField("diagnosis")
    private String diagnosis;

    @TableField("medications")
    private String medications;

    @TableField("dosage")
    private String dosage;

    @TableField("instructions")
    private String instructions;

    @TableField("status")
    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
} 