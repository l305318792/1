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
 * 节假日表
 * @author yunchuan
 * @since 2025-02-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("holiday")
public class Holiday implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 节假日ID
     */
    @TableId("id")
    private String id;

    /**
     * 节假日日期
     */
    @TableField("holiday_date")
    private LocalDate holidayDate;

    /**
     * 节假日名称
     */
    @TableField("holiday_name")
    private String holidayName;

    /**
     * 类型：HOLIDAY-法定节假日，WORKDAY-调休工作日
     */
    @TableField("holiday_type")
    private String holidayType;

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