package com.yunchuan.medical.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 科室树形结构DTO
 */
@Data
public class DepartmentTreeDTO {
    
    /**
     * 科室ID
     */
    private String id;
    
    /**
     * 科室名称
     */
    private String name;
    
    /**
     * 父级科室ID
     */
    private String parentId;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 科室介绍
     */
    private String introduction;
    
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
    private List<DepartmentTreeDTO> children;
} 