package com.yunchuan.medical.entity;

import java.time.LocalDateTime;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * 资讯实体类
 */
@Data
@TableName("news")
public class News {
    
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    private String title;
    
    private String content;
    
    private String categoryId;
    
    private String status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 