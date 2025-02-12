package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ArticleDTO;
import com.yunchuan.medical.service.ArticleGenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 管理员-文章管理
 */
@Slf4j
@Tag(name = "管理员-文章管理")
@RestController
@RequestMapping("/admin/articles")
@PreAuthorize("hasRole('ADMIN')")
public class AdminArticleController {

    private final ArticleGenerationService articleGenerationService;

    public AdminArticleController(ArticleGenerationService articleGenerationService) {
        this.articleGenerationService = articleGenerationService;
    }

    /**
     * 生成一篇AI文章
     */
    @Operation(summary = "生成一篇AI文章")
    @PostMapping("/generate")
    public Result<ArticleDTO> generateArticle() {
        try {
            log.info("开始生成AI文章");
            ArticleDTO article = articleGenerationService.generateHealthArticle();
            log.info("AI文章生成成功：{}", article.getTitle());
            return Result.ok(article);
        } catch (Exception e) {
            log.error("AI文章生成失败", e);
            return Result.error("AI文章生成失败：" + e.getMessage());
        }
    }
} 