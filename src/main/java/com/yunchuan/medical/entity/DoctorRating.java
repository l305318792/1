package com.yunchuan.medical.entity;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * 医生评分实体类
 */
@Data
@TableName("doctor_rating")
public class DoctorRating {
    
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    private String userId;
    
    private String doctorId;
    
    private String appointmentId;
    
    private BigDecimal serviceAttitude;
    
    private BigDecimal medicalSkill;
    
    private BigDecimal medicalEffect;
    
    private String comment;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 