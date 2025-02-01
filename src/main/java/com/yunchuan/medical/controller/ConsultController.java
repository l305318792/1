package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ConsultMessageDTO;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 咨询管理
 */
@RestController
@RequestMapping("/consult")
public class ConsultController {

    /**
     * 获取列表
     */
    @GetMapping
    public Result<List<ConsultMessageDTO>> list() {
        return Result.ok(new ArrayList<>());
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    public Result<ConsultMessageDTO> get(@PathVariable String id) {
        return Result.ok(new ConsultMessageDTO());
    }

    /**
     * 新增
     */
    @PostMapping
    public Result<ConsultMessageDTO> add(@RequestBody ConsultMessageDTO message) {
        return Result.ok(new ConsultMessageDTO());
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    public Result<ConsultMessageDTO> update(@PathVariable String id, @RequestBody ConsultMessageDTO message) {
        return Result.ok(new ConsultMessageDTO());
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable String id) {
        return Result.ok(true);
    }
} 