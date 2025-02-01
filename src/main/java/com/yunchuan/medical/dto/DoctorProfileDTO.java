package com.yunchuan.medical.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 医生个人信息数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorProfileDTO {
    /**
     * ID
     */
    private String id;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 职称
     */
    private String title;
    
    /**
     * 科室ID
     */
    private String departmentId;
    
    /**
     * 科室名称
     */
    private String departmentName;
    
    /**
     * 专长
     */
    private String specialty;
    
    /**
     * 简介
     */
    private String introduction;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 问诊次数
     */
    private Integer consultationCount;
    
    /**
     * 评分
     */
    private Double rating;
    
    /**
     * 状态（ACTIVE-在职，INACTIVE-离职，SUSPENDED-停诊）
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