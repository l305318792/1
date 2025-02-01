package com.yunchuan.medical.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 评价表
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Getter
@Setter
@TableName("rating")
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价ID
     */
    @TableId("id")
    private String id;

    /**
     * 预约ID
     */
    @TableField("appointment_id")
    private String appointmentId;

    /**
     * 患者ID
     */
    @TableField("patient_id")
    private String patientId;

    /**
     * 医生ID
     */
    @TableField("doctor_id")
    private String doctorId;

    /**
     * 评分(1-5)
     */
    @TableField("score")
    private Integer score;

    /**
     * 评价内容
     */
    @TableField("comment")
    private String comment;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
