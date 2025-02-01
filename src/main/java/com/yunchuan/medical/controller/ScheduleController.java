package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ScheduleDTO;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 排班管理
 */
@Tag(name = "排班管理")
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    /**
     * 获取列表
     */
    @GetMapping
    public Result<List<ScheduleDTO>> list() {
        return Result.ok(new ArrayList<>());
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    public Result<ScheduleDTO> get(@PathVariable String id) {
        return Result.ok(new ScheduleDTO());
    }

    /**
     * 新增
     */
    @PostMapping
    public Result<ScheduleDTO> add(@RequestBody ScheduleDTO schedule) {
        return Result.ok(new ScheduleDTO());
    }

    /**
     * 更新
     */
    @PutMapping("/{id}")
    public Result<ScheduleDTO> update(@PathVariable String id, @RequestBody ScheduleDTO schedule) {
        return Result.ok(new ScheduleDTO());
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable String id) {
        return Result.ok(true);
    }
} 