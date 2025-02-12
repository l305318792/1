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

/**
 * <p>
 * 医生表
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("doctor")
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医生ID
     */
    @TableId("id")
    private String id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 医生姓名
     */
    @TableField("name")
    private String name;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 科室ID
     */
    @TableField("department_id")
    private String departmentId;

    /**
     * 职称
     */
    @TableField("title")
    private String title;

    /**
     * 专长
     */
    @TableField("specialty")
    private String specialty;

    /**
     * 简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 问诊费用
     */
    @TableField("consultation_fee")
    private BigDecimal consultationFee;

    /**
     * 评分
     */
    @TableField("rating")
    private BigDecimal rating = new BigDecimal("5.0");

    /**
     * 评分次数
     */
    @TableField("rating_count")
    private Integer ratingCount = 0;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status = 1;

    /**
     * 问诊次数
     */
    @TableField("consult_count")
    private Integer consultCount = 0;

    /**
     * 预约次数
     */
    @TableField("appointment_count")
    private Integer appointmentCount = 0;

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
