package com.yunchuan.medical.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 医生数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    /**
     * ID
     */
    private String id;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 医生姓名
     */
    private String name;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 科室ID
     */
    private String departmentId;
    
    /**
     * 科室名称
     */
    private String departmentName;
    
    /**
     * 职称
     */
    private String title;
    
    /**
     * 专长
     */
    private String specialty;
    
    /**
     * 简介
     */
    private String introduction;

    /**
     * 问诊费用
     */
    private BigDecimal consultationFee;
    
    /**
     * 状态(1-正常，0-禁用)
     */
    private Integer status;
    
    /**
     * 评分
     */
    private BigDecimal rating;
    
    /**
     * 评分次数
     */
    private Integer ratingCount;

    /**
     * 问诊次数
     */
    private Integer consultCount;

    /**
     * 预约次数
     */
    private Integer appointmentCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}