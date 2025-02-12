package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.entity.Holiday;
import com.yunchuan.medical.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * 管理员-节假日管理
 */
@Slf4j
@Tag(name = "管理员-节假日管理")
@RestController
@RequestMapping("/admin/holidays")
public class AdminHolidayController {

    private final HolidayService holidayService;

    public AdminHolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @Operation(summary = "创建节假日")
    @PostMapping
    public Result<Holiday> createHoliday(@RequestBody @Valid Holiday holiday) {
        return Result.success(holidayService.createHoliday(holiday));
    }

    @Operation(summary = "更新节假日")
    @PutMapping("/{id}")
    public Result<Holiday> updateHoliday(
            @PathVariable String id,
            @RequestBody @Valid Holiday holiday) {
        return Result.success(holidayService.updateHoliday(id, holiday));
    }

    @Operation(summary = "删除节假日")
    @DeleteMapping("/{id}")
    public Result<Void> deleteHoliday(@PathVariable String id) {
        holidayService.deleteHoliday(id);
        return Result.success();
    }

    @Operation(summary = "获取节假日")
    @GetMapping("/{id}")
    public Result<Holiday> getHoliday(@PathVariable String id) {
        return Result.success(holidayService.getHoliday(id));
    }

    @Operation(summary = "获取指定日期范围内的节假日")
    @GetMapping("/range")
    public Result<List<Holiday>> getHolidaysByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return Result.success(holidayService.getHolidaysByDateRange(startDate, endDate));
    }

    @Operation(summary = "判断指定日期是否为节假日")
    @GetMapping("/check/holiday")
    public Result<Boolean> isHoliday(@RequestParam LocalDate date) {
        return Result.success(holidayService.isHoliday(date));
    }

    @Operation(summary = "判断指定日期是否为工作日")
    @GetMapping("/check/workday")
    public Result<Boolean> isWorkday(@RequestParam LocalDate date) {
        return Result.success(holidayService.isWorkday(date));
    }

    @Operation(summary = "更新节假日状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateHolidayStatus(
            @PathVariable String id,
            @RequestParam String status) {
        holidayService.updateHolidayStatus(id, status);
        return Result.success();
    }
} 