package com.yunchuan.medical.controller.patient;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ArticleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 患者-健康资讯
 */
@Tag(name = "患者-健康资讯")
@RestController
@RequestMapping("/patient/articles")
public class PatientArticleController {

    /**
     * 获取健康资讯列表
     */
    @Operation(summary = "获取健康资讯列表")
    @GetMapping
    public Result<Object> getArticleList() {
        // 模拟文章数据
        ArticleDTO article1 = ArticleDTO.builder()
                .id("1")
                .title("高血压患者的饮食指南")
                .summary("本文介绍了高血压患者在日常生活中应该注意的饮食事项...")
                .content("高血压是一种常见的慢性病，合理的饮食管理对控制血压至关重要。\n\n" +
                        "1. 限制盐分摄入：每日食盐摄入量应控制在6克以下\n" +
                        "2. 增加钾的摄入：多吃富含钾的食物，如香蕉、土豆等\n" +
                        "3. 控制脂肪摄入：选择低脂肪食品，避免油炸食品\n" +
                        "4. 多吃蔬菜水果：每天保证足够的蔬菜水果摄入\n" +
                        "5. 戒烟限酒：避免吸烟，适量饮酒")
                .category("HEALTH_GUIDE")  // 健康指南
                .tags(Arrays.asList("高血压", "饮食", "健康管理"))
                .status("PUBLISHED")
                .publishTime(LocalDateTime.now().minusDays(1))
                .createTime(LocalDateTime.now().minusDays(1))
                .build();
                
        ArticleDTO article2 = ArticleDTO.builder()
                .id("2")
                .title("秋季儿童呼吸道疾病预防")
                .summary("秋季是儿童呼吸道疾病的高发季节，本文为家长提供实用的预防建议...")
                .content("随着气温逐渐降低，儿童呼吸道疾病的发病率明显升高。以下是几点重要的预防措施：\n\n" +
                        "1. 注意保暖：适时增减衣物，避免受凉\n" +
                        "2. 保持室内通风：每天开窗通风2-3次，每次15-20分钟\n" +
                        "3. 勤洗手：培养孩子良好的卫生习惯\n" +
                        "4. 规律作息：保证充足的睡眠时间\n" +
                        "5. 均衡饮食：补充充足的维生素和蛋白质")
                .category("DISEASE_PREVENTION")  // 疾病预防
                .tags(Arrays.asList("儿科", "呼吸道", "预防保健"))
                .status("PUBLISHED")
                .publishTime(LocalDateTime.now().minusDays(2))
                .createTime(LocalDateTime.now().minusDays(2))
                .build();
                
        return Result.ok(new Object() {
            public final List<ArticleDTO> list = Arrays.asList(article1, article2);
            public final int total = 2;
        });
    }

    /**
     * 获取健康资讯详情
     */
    @Operation(summary = "获取健康资讯详情")
    @GetMapping("/{id}")
    public Result<ArticleDTO> getArticle(@PathVariable String id) {
        // 模拟文章详情数据
        ArticleDTO article = ArticleDTO.builder()
                .id(id)
                .title("高血压患者的饮食指南")
                .summary("本文介绍了高血压患者在日常生活中应该注意的饮食事项...")
                .content("高血压是一种常见的慢性病，合理的饮食管理对控制血压至关重要。\n\n" +
                        "1. 限制盐分摄入：每日食盐摄入量应控制在6克以下\n" +
                        "2. 增加钾的摄入：多吃富含钾的食物，如香蕉、土豆等\n" +
                        "3. 控制脂肪摄入：选择低脂肪食品，避免油炸食品\n" +
                        "4. 多吃蔬菜水果：每天保证足够的蔬菜水果摄入\n" +
                        "5. 戒烟限酒：避免吸烟，适量饮酒\n\n" +
                        "注意事项：\n" +
                        "1. 定期监测血压\n" +
                        "2. 保持规律作息\n" +
                        "3. 适量运动\n" +
                        "4. 保持心情愉悦")
                .category("HEALTH_GUIDE")
                .tags(Arrays.asList("高血压", "饮食", "健康管理"))
                .status("PUBLISHED")
                .publishTime(LocalDateTime.now().minusDays(1))
                .createTime(LocalDateTime.now().minusDays(1))
                .build();
                
        return Result.ok(article);
    }
} 