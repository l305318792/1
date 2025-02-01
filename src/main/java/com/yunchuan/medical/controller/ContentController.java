package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ContentDTO;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * 内容管理
 */
@Tag(name = "内容管理")
@RestController
@RequestMapping("/content")
public class ContentController {

    /**
     * 获取列表
     */
    @GetMapping
    public Result<List<ContentDTO>> list() {
        return Result.ok(new ArrayList<>());
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    public Result<ContentDTO> get(@PathVariable String id) {
        return Result.ok(new ContentDTO());
    }

    /**
     * 新增
     */
    @PostMapping
    public Result<ContentDTO> add(@RequestBody ContentDTO content) {
        return Result.ok(new ContentDTO());
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    public Result<ContentDTO> update(@PathVariable String id, @RequestBody ContentDTO content) {
        return Result.ok(new ContentDTO());
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable String id) {
        return Result.ok(true);
    }
} 