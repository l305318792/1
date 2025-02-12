package com.yunchuan.medical.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * <p>
 * 评价表
 * </p>
 *
 * @author yunchuan
 * @since 2025-02-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("doctor_rating")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 预约ID（可选，用于预约评价）
     */
    @TableField("appointment_id")
    private String appointmentId;

    /**
     * 问诊ID（可选，用于问诊评价）
     */
    @TableField("consultation_id")
    private String consultationId;

    /**
     * 医生ID
     */
    @TableField("doctor_id")
    private String doctorId;

    /**
     * 患者ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 服务态度评分
     */
    @TableField("service_attitude")
    private BigDecimal serviceAttitude;

    /**
     * 医疗技术评分
     */
    @TableField("medical_skill")
    private BigDecimal medicalSkill;

    /**
     * 治疗效果评分
     */
    @TableField("medical_effect")
    private BigDecimal medicalEffect;

    /**
     * 评价内容
     */
    @TableField("comment")
    private String comment;

    /**
     * 状态：NORMAL-正常，HIDDEN-隐藏
     */
    @TableField("status")
    private String status = "NORMAL";

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
