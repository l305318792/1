package com.yunchuan.medical.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 预约表
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Data
@TableName("appointment")
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 排班ID
     */
    private String scheduleId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 医生ID
     */
    private String doctorId;

    /**
     * 科室ID
     */
    private String departmentId;

    /**
     * 序号
     */
    private Integer sequenceNumber;

    /**
     * 状态
     */
    private String status;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 就诊时间
     */
    @TableField("visit_time")
    private LocalDateTime visitTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
