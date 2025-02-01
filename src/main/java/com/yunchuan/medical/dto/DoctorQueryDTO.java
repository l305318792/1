package com.yunchuan.medical.dto;

import lombok.Data;

/**
 * 医生查询DTO
 * @author yunchuan
 * @since 1.0.0
 */
@Data
public class DoctorQueryDTO {
    /**
     * 科室ID
     */
    private String departmentId;
    
    /**
     * 关键词(医生姓名/科室名称)
     */
    private String keyword;
    
    /**
     * 状态(active/inactive)
     */
    private String status;
}