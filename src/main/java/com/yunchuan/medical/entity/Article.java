package com.yunchuan.medical.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章实体类
 */
@Data
@TableName("article")
public class Article {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 摘要
     */
    private String summary;
    
    /**
     * 内容
     */
    private String content;
    
    /**
     * 分类（HEALTH_GUIDE-健康指南，DISEASE_PREVENTION-疾病预防）
     */
    private String category;
    
    /**
     * 标签，多个标签用逗号分隔
     */
    private String tags;
    
    /**
     * 状态（DRAFT-草稿，PUBLISHED-已发布）
     */
    private String status;
    
    /**
     * 是否AI生成
     */
    @TableField("is_ai_generated")
    private Boolean isAiGenerated;
    
    /**
     * 发布时间
     */
    @TableField("publish_time")
    private LocalDateTime publishTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 