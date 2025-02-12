package com.yunchuan.medical.controller.doctor;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ScheduleRuleDTO;
import com.yunchuan.medical.service.ScheduleRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 医生排班规则控制器
 */
@Slf4j
@Tag(name = "医生-排班规则管理")
@RestController
@RequestMapping("/api/doctor/schedule-rules")
public class DoctorScheduleRuleController {

    private final ScheduleRuleService scheduleRuleService;

    public DoctorScheduleRuleController(ScheduleRuleService scheduleRuleService) {
        this.scheduleRuleService = scheduleRuleService;
    }

    @Operation(summary = "创建排班规则")
    @PostMapping
    public Result<ScheduleRuleDTO> createRule(@RequestBody @Valid ScheduleRuleDTO ruleDTO) {
        return Result.success(scheduleRuleService.createRule(ruleDTO));
    }

    @Operation(summary = "更新排班规则")
    @PutMapping("/{id}")
    public Result<ScheduleRuleDTO> updateRule(
            @PathVariable String id,
            @RequestBody @Valid ScheduleRuleDTO ruleDTO) {
        return Result.success(scheduleRuleService.updateRule(id, ruleDTO));
    }

    @Operation(summary = "删除排班规则")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRule(@PathVariable String id) {
        scheduleRuleService.deleteRule(id);
        return Result.success();
    }

    @Operation(summary = "获取排班规则")
    @GetMapping("/{id}")
    public Result<ScheduleRuleDTO> getRule(@PathVariable String id) {
        return Result.success(scheduleRuleService.getRule(id));
    }

    @Operation(summary = "获取医生的排班规则列表")
    @GetMapping
    public Result<List<ScheduleRuleDTO>> getDoctorRules(@RequestParam String doctorId) {
        return Result.success(scheduleRuleService.getDoctorRules(doctorId));
    }

    @Operation(summary = "生成排班表")
    @PostMapping("/{id}/generate")
    public Result<Void> generateSchedules(
            @PathVariable String id,
            @RequestParam String doctorId) {
        scheduleRuleService.generateSchedules(doctorId, id);
        return Result.success();
    }

    @Operation(summary = "预览排班表")
    @GetMapping("/{id}/preview")
    public Result<List<ScheduleRuleDTO>> previewSchedules(
            @PathVariable String id,
            @RequestParam String doctorId) {
        return Result.success(scheduleRuleService.previewSchedules(doctorId, id));
    }

    @Operation(summary = "更新排班规则状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateRuleStatus(
            @PathVariable String id,
            @RequestParam String status) {
        scheduleRuleService.updateRuleStatus(id, status);
        return Result.success();
    }
} 