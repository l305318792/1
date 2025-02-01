package com.yunchuan.medical.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 科室数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    /**
     * ID
     */
    private String id;
    
    /**
     * 科室名称
     */
    private String name;
    
    /**
     * 父科室ID
     */
    private String parentId;
    
    /**
     * 科室介绍
     */
    private String introduction;
    
    /**
     * 状态(normal-正常，disabled-禁用)
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

    /**
     * 子科室列表
     */
    private List<DepartmentDTO> children;
}