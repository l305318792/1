package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.FinanceDTO;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 财务管理
 */
@Tag(name = "财务管理")
@RestController
@RequestMapping("/finance")
public class FinanceController {

    /**
     * 获取列表
     */
    @GetMapping
    public Result<List<FinanceDTO>> list() {
        return Result.ok(new ArrayList<>());
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    public Result<FinanceDTO> get(@PathVariable String id) {
        return Result.ok(new FinanceDTO());
    }

    /**
     * 新增
     */
    @PostMapping
    public Result<FinanceDTO> add(@RequestBody FinanceDTO finance) {
        return Result.ok(new FinanceDTO());
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    public Result<FinanceDTO> update(@PathVariable String id, @RequestBody FinanceDTO finance) {
        return Result.ok(new FinanceDTO());
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable String id) {
        return Result.ok(true);
    }
} 