package com.yunchuan.medical.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 科室表
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 科室ID
     */
    @TableId("id")
    private String id;

    /**
     * 科室名称
     */
    @TableField("name")
    private String name;

    /**
     * 父级科室ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 科室介绍
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 排序号，值越小越靠前
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @Builder.Default
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * 更新时间
     */
    @Builder.Default
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime = LocalDateTime.now();
}
