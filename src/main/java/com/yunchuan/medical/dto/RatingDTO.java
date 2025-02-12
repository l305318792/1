package com.yunchuan.medical.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

/**
 * 评价数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    /**
     * 评价ID
     */
    private String id;
    
    /**
     * 预约ID（可选，用于预约评价）
     */
    private String appointmentId;
    
    /**
     * 问诊ID（可选，用于问诊评价）
     */
    private String consultationId;
    
    /**
     * 医生ID
     */
    private String doctorId;
    
    /**
     * 医生姓名
     */
    private String doctorName;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 用户姓名
     */
    private String userName;
    
    @NotNull(message = "服务态度评分不能为空")
    @DecimalMin(value = "1.0", message = "服务态度评分最低为1分")
    @DecimalMax(value = "5.0", message = "服务态度评分最高为5分")
    private BigDecimal serviceAttitude;
    
    @NotNull(message = "医疗技术评分不能为空")
    @DecimalMin(value = "1.0", message = "医疗技术评分最低为1分")
    @DecimalMax(value = "5.0", message = "医疗技术评分最高为5分")
    private BigDecimal medicalSkill;
    
    @NotNull(message = "治疗效果评分不能为空")
    @DecimalMin(value = "1.0", message = "治疗效果评分最低为1分")
    @DecimalMax(value = "5.0", message = "治疗效果评分最高为5分")
    private BigDecimal medicalEffect;
    
    /**
     * 评价内容
     */
    private String comment;
    
    /**
     * 状态(normal-正常 hidden-隐藏)
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    /**
     * 计算综合评分
     */
    public BigDecimal getOverallRating() {
        if (serviceAttitude == null || medicalSkill == null || medicalEffect == null) {
            return BigDecimal.ZERO;
        }
        return serviceAttitude.add(medicalSkill).add(medicalEffect)
                .divide(new BigDecimal("3"), 1, BigDecimal.ROUND_HALF_UP);
    }
} 