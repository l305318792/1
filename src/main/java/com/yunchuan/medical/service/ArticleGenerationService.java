package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.ArticleDTO;

/**
 * 文章生成服务接口
 */
public interface ArticleGenerationService {
    
    /**
     * 使用AI生成一篇健康资讯文章
     * @return 生成的文章
     */
    ArticleDTO generateHealthArticle();
    
    /**
     * 定时生成文章的任务
     */
    void scheduleArticleGeneration();
} 