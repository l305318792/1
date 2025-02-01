package com.yunchuan.medical.dto;

import lombok.Data;

@Data
public class AddDepartmentDTO {
    private String parentId;    // 父级科室ID，可选
    private String name;        // 科室名称，必填
    private String introduction;// 科室介绍，可选
    private Integer sortOrder;  // 排序号，必填
} 