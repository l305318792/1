package com.yunchuan.medical.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 处方数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {
    /**
     * 处方ID
     */
    private String id;
    
    /**
     * 问诊ID
     */
    private String consultationId;
    
    /**
     * 医生ID
     */
    private String doctorId;
    
    /**
     * 患者ID
     */
    private String patientId;
    
    /**
     * 诊断结果
     */
    private String diagnosis;
    
    /**
     * 药品清单
     */
    private List<String> medications;
    
    /**
     * 用药说明
     */
    private String dosage;
    
    /**
     * 注意事项
     */
    private String instructions;
    
    /**
     * 状态：PENDING-待支付，PAID-已支付，DISPENSED-已发药，COMPLETED-已完成
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 