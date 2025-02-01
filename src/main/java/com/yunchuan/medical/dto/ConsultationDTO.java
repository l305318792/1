package com.yunchuan.medical.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 问诊记录数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDTO {
    /**
     * 问诊ID
     */
    private String id;
    
    /**
     * 患者ID
     */
    private String userId;
    
    /**
     * 患者姓名
     */
    private String userName;
    
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
     * 预约ID
     */
    private String appointmentId;
    
    /**
     * 症状描述
     */
    private String symptoms;
    
    /**
     * 诊断结果
     */
    private String diagnosis;
    
    /**
     * 治疗方案
     */
    private String treatment;
    
    /**
     * 状态：PENDING-待开始，IN_PROGRESS-进行中，COMPLETED-已完成，CANCELLED-已取消
     */
    private String status;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 