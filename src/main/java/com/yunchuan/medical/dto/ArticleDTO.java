package com.yunchuan.medical.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    /**
     * 文章ID
     */
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
     * 标签列表
     */
    private List<String> tags;
    
    /**
     * 状态（DRAFT-草稿，PUBLISHED-已发布，HIDDEN-已隐藏）
     */
    private String status;
    
    /**
     * 发布时间
     */
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