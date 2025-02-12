package com.yunchuan.medical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "医生数据传输对象")
public class DoctorDTO {
    /**
     * ID
     */
    @Schema(description = "医生ID")
    private String id;
    
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String userId;
    
    /**
     * 医生姓名
     */
    @Schema(description = "医生姓名")
    private String name;
    
    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;
    
    /**
     * 科室ID
     */
    @Schema(description = "科室ID")
    private String departmentId;
    
    /**
     * 科室名称
     */
    @Schema(description = "科室名称")
    private String departmentName;
    
    /**
     * 职称
     */
    @Schema(description = "职称")
    private String title;
    
    /**
     * 专长
     */
    @Schema(description = "专长")
    private String specialty;
    
    /**
     * 简介
     */
    @Schema(description = "简介")
    private String introduction;

    /**
     * 挂号费
     */
    @Schema(description = "挂号费")
    private BigDecimal consultationFee;
    
    /**
     * 状态(1-正常，0-禁用)
     */
    @Schema(description = "状态(1-正常，0-禁用)")
    private Integer status;
    
    /**
     * 评分
     */
    @Schema(description = "评分")
    private BigDecimal rating;
    
    /**
     * 评分次数
     */
    @Schema(description = "评分次数")
    private Integer ratingCount;

    /**
     * 问诊次数
     */
    @Schema(description = "问诊次数")
    private Integer consultCount;

    /**
     * 预约次数
     */
    @Schema(description = "预约次数")
    private Integer appointmentCount;
    
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}