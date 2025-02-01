package com.yunchuan.medical.entity;

import java.time.LocalDateTime;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * 健康资讯分类实体类
 */
@Data
@TableName("health_category")
public class HealthCategory {
    
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    private String name;
    
    private Integer sortOrder;
    
    private String status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 