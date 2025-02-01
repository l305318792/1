package com.yunchuan.medical.entity;

import java.time.LocalDateTime;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * 病历实体类
 */
@Data
@TableName("medical_record")
public class MedicalRecord {
    
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    private String patientId;
    
    private String doctorId;
    
    private String diagnosis;
    
    private String treatment;
    
    private String chiefComplaint;
    
    private String presentIllness;
    
    private String pastHistory;
    
    private String physicalExam;
    
    private String remark;
    
    private LocalDateTime visitTime;
    
    private String status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 