package com.yunchuan.medical.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 医生评价DTO
 */
@Data
public class DoctorRatingDTO {
    private String id;
    private String appointmentId;
    private String doctorId;
    private String doctorName;
    private String userId;
    private String userName;
    private BigDecimal serviceAttitude;
    private BigDecimal medicalSkill;
    private BigDecimal medicalEffect;
    private String comment;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 