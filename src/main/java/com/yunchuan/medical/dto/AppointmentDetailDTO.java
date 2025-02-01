package com.yunchuan.medical.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预约详情DTO
 * @author yunchuan
 * @since 1.0.0
 */
@Data
public class AppointmentDetailDTO {
    /**
     * 预约ID
     */
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