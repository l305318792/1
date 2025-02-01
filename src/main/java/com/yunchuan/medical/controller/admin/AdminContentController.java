package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.AnnouncementDTO;
import com.yunchuan.medical.dto.ArticleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 管理员内容管理
 */
@Tag(name = "管理员-内容管理")
@RestController
@RequestMapping("/admin/content")
public class AdminContentController {

    @Operation(summary = "获取公告列表")
    @GetMapping("/announcements")
    public Result<Object> getAnnouncementList() {
        // 模拟公告数据
        AnnouncementDTO announcement1 = AnnouncementDTO.builder()
                .id("1")
                .title("门诊时间调整通知")
                .content("因系统升级，1月30日下午门诊暂停服务，给您带来不便敬请谅解。")
                .type("NOTICE")  // 通知公告
                .status("PUBLISHED")
                .publishTime(LocalDateTime.now().minusDays(1))
                .createTime(LocalDateTime.now().minusDays(1))
                .build();
                
        AnnouncementDTO announcement2 = AnnouncementDTO.builder()
                .id("2")
                .title("新增专家门诊")
                .content("自2月1日起，新增心内科张教授专家门诊，每周三上午。")
                .type("NEWS")  // 新闻动态
                .status("DRAFT")
                .createTime(LocalDateTime.now())
                .build();
                
        return Result.ok(new Object() {
            public final List<AnnouncementDTO> list = Arrays.asList(announcement1, announcement2);
            public final int total = 2;
        });
    }

    @Operation(summary = "创建公告")
    @PostMapping("/announcements")
    public Result<AnnouncementDTO> createAnnouncement(@RequestBody AnnouncementDTO announcement) {
        announcement.setId("3");
        announcement.setStatus("DRAFT");
        announcement.setCreateTime(LocalDateTime.now());
        return Result.ok(announcement);
    }

    @Operation(summary = "更新公告")
    @PutMapping("/announcements/{id}")
    public Result<AnnouncementDTO> updateAnnouncement(
            @PathVariable String id,
            @RequestBody AnnouncementDTO announcement) {
        announcement.setId(id);
        announcement.setUpdateTime(LocalDateTime.now());
        return Result.ok(announcement);
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/announcements/{id}")
    public Result<Boolean> deleteAnnouncement(@PathVariable String id) {
        return Result.ok(true);
    }

    @Operation(summary = "发布公告")
    @PostMapping("/announcements/{id}/publish")
    public Result<AnnouncementDTO> publishAnnouncement(@PathVariable String id) {
        AnnouncementDTO announcement = AnnouncementDTO.builder()
                .id(id)
                .title("门诊时间调整通知")
                .content("因系统升级，1月30日下午门诊暂停服务，给您带来不便敬请谅解。")
                .type("NOTICE")
                .status("PUBLISHED")
                .publishTime(LocalDateTime.now())
                .createTime(LocalDateTime.now().minusDays(1))
                .updateTime(LocalDateTime.now())
                .build();
        return Result.ok(announcement);
    }

    @Operation(summary = "获取文章列表")
    @GetMapping("/articles")
    public Result<Object> getArticleList() {
        // 模拟文章数据
        ArticleDTO article1 = ArticleDTO.builder()
                .id("1")
                .title("高血压患者的饮食指南")
                .summary("本文介绍了高血压患者在日常生活中应该注意的饮食事项...")
                .content("高血压是一种常见的慢性病，合理的饮食管理对控制血压至关重要...")
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
                .content("随着气温逐渐降低，儿童呼吸道疾病的发病率明显升高...")
                .category("DISEASE_PREVENTION")  // 疾病预防
                .tags(Arrays.asList("儿科", "呼吸道", "预防保健"))
                .status("DRAFT")
                .createTime(LocalDateTime.now())
                .build();
                
        return Result.ok(new Object() {
            public final List<ArticleDTO> list = Arrays.asList(article1, article2);
            public final int total = 2;
        });
    }

    @Operation(summary = "创建文章")
    @PostMapping("/articles")
    public Result<ArticleDTO> createArticle(@RequestBody ArticleDTO article) {
        article.setId("3");
        article.setStatus("DRAFT");
        article.setCreateTime(LocalDateTime.now());
        return Result.ok(article);
    }

    @Operation(summary = "更新文章")
    @PutMapping("/articles/{id}")
    public Result<ArticleDTO> updateArticle(
            @PathVariable String id,
            @RequestBody ArticleDTO article) {
        article.setId(id);
        article.setUpdateTime(LocalDateTime.now());
        return Result.ok(article);
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("/articles/{id}")
    public Result<Boolean> deleteArticle(@PathVariable String id) {
        return Result.ok(true);
    }

    @Operation(summary = "发布文章")
    @PostMapping("/articles/{id}/publish")
    public Result<ArticleDTO> publishArticle(@PathVariable String id) {
        ArticleDTO article = ArticleDTO.builder()
                .id(id)
                .title("高血压患者的饮食指南")
                .summary("本文介绍了高血压患者在日常生活中应该注意的饮食事项...")
                .content("高血压是一种常见的慢性病，合理的饮食管理对控制血压至关重要...")
                .category("HEALTH_GUIDE")
                .tags(Arrays.asList("高血压", "饮食", "健康管理"))
                .status("PUBLISHED")
                .publishTime(LocalDateTime.now())
                .createTime(LocalDateTime.now().minusDays(1))
                .updateTime(LocalDateTime.now())
                .build();
        return Result.ok(article);
    }
} 