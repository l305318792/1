package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.NewsDTO;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 健康资讯管理
 */
@Tag(name = "健康资讯")
@RestController
@RequestMapping("/news")
public class NewsController {

    /**
     * 获取列表
     */
    @GetMapping
    public Result<List<NewsDTO>> list() {
        return Result.ok(new ArrayList<>());
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    public Result<NewsDTO> get(@PathVariable String id) {
        return Result.ok(new NewsDTO());
    }

    /**
     * 新增
     */
    @PostMapping
    public Result<NewsDTO> add(@RequestBody NewsDTO news) {
        return Result.ok(new NewsDTO());
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    public Result<NewsDTO> update(@PathVariable String id, @RequestBody NewsDTO news) {
        return Result.ok(new NewsDTO());
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable String id) {
        return Result.ok(true);
    }
} 