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
     * 医生姓名
     */
    private String name;
    
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
     * 状态(NORMAL-正常，DISABLED-禁用)
     */
    private String status;
    
    /**
     * 平均评分
     */
    private BigDecimal averageRating;
    
    /**
     * 评分次数
     */
    private Integer ratingCount;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}