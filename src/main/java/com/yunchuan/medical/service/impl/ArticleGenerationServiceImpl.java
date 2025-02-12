package com.yunchuan.medical.service.impl;

import com.alibaba.fastjson2.JSON;
import com.yunchuan.medical.dto.ArticleDTO;
import com.yunchuan.medical.entity.Article;
import com.yunchuan.medical.mapper.ArticleMapper;
import com.yunchuan.medical.service.ArticleGenerationService;
import com.yunchuan.medical.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 文章生成服务实现类
 */
@Service
public class ArticleGenerationServiceImpl implements ArticleGenerationService {

    private static final Logger log = LoggerFactory.getLogger(ArticleGenerationServiceImpl.class);
    
    private final ArticleMapper articleMapper;
    private final AiService aiService;
    
    // 文章主题列表
    private static final List<String> ARTICLE_TOPICS = Arrays.asList(
        "高血压预防与管理",
        "糖尿病饮食指南",
        "儿童常见疾病预防",
        "心脏病预防保健",
        "老年人健康管理",
        "慢性病日常护理",
        "季节性疾病预防",
        "中医养生保健",
        "运动健康指南",
        "营养均衡饮食"
    );

    public ArticleGenerationServiceImpl(ArticleMapper articleMapper, AiService aiService) {
        this.articleMapper = articleMapper;
        this.aiService = aiService;
    }

    @Override
    @Transactional
    public ArticleDTO generateHealthArticle() {
        try {
            // 1. 随机选择一个主题
            String topic = getRandomTopic();
            
            // 2. 构建AI提示
            String prompt = String.format(
                "请以'%s'为主题，生成一篇健康资讯文章。要求：\n" +
                "1. 包含标题、摘要和正文\n" +
                "2. 正文应包含具体的建议和注意事项\n" +
                "3. 使用通俗易懂的语言\n" +
                "4. 内容要专业、实用\n" +
                "5. 分点列出关键信息\n" +
                "请按以下格式返回：\n" +
                "标题：\n" +
                "摘要：\n" +
                "正文：\n" +
                "标签：（3-5个关键词）",
                topic
            );
            
            // 3. 调用AI生成内容
            String aiResponse = aiService.generateContent(prompt);
            
            // 4. 解析AI响应
            String[] parts = aiResponse.split("\n");
            String title = parts[0].replace("标题：", "").trim();
            String summary = parts[1].replace("摘要：", "").trim();
            
            // 提取正文（去掉标题、摘要和标签部分）
            StringBuilder content = new StringBuilder();
            boolean isContent = false;
            for (String line : parts) {
                if (line.startsWith("正文：")) {
                    isContent = true;
                    continue;
                }
                if (line.startsWith("标签：")) {
                    isContent = false;
                    continue;
                }
                if (isContent) {
                    content.append(line).append("\n");
                }
            }
            
            // 提取标签
            String tagsLine = parts[parts.length - 1].replace("标签：", "").trim();
            List<String> tags = Arrays.asList(tagsLine.split("、|，|,"));
            
            // 5. 创建文章实体
            Article article = new Article();
            article.setTitle(title);
            article.setSummary(summary);
            article.setContent(content.toString().trim());
            article.setCategory("HEALTH_GUIDE");
            article.setTags(JSON.toJSONString(tags));
            article.setStatus("PUBLISHED");
            article.setIsAiGenerated(true);
            article.setPublishTime(LocalDateTime.now());
            
            // 6. 保存到数据库
            articleMapper.insert(article);
            
            // 7. 转换为DTO并返回
            ArticleDTO articleDTO = new ArticleDTO();
            BeanUtils.copyProperties(article, articleDTO);
            articleDTO.setTags(tags);
            
            log.info("成功生成AI文章：{}", title);
            return articleDTO;
            
        } catch (Exception e) {
            log.error("生成文章失败", e);
            throw new RuntimeException("生成文章失败：" + e.getMessage());
        }
    }

    @Override
    @Scheduled(cron = "0 0 8 * * ?") // 每天早上8点执行
    public void scheduleArticleGeneration() {
        log.info("开始执行定时文章生成任务");
        try {
            generateHealthArticle();
            log.info("定时文章生成任务执行完成");
        } catch (Exception e) {
            log.error("定时文章生成任务执行失败", e);
        }
    }
    
    /**
     * 随机获取一个文章主题
     */
    private String getRandomTopic() {
        Random random = new Random();
        return ARTICLE_TOPICS.get(random.nextInt(ARTICLE_TOPICS.size()));
    }
} 