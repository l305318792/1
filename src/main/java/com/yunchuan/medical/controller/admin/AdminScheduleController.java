package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.result.Result;
import com.yunchuan.medical.dto.ScheduleDTO;
import com.yunchuan.medical.dto.ScheduleFormDTO;
import com.yunchuan.medical.entity.Schedule;
import com.yunchuan.medical.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员排班控制器
 */
@Slf4j
@Tag(name = "排班管理")
@RestController
@RequestMapping("/api/admin/schedules")
public class AdminScheduleController {

    private final ScheduleService scheduleService;

    public AdminScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(summary = "创建排班")
    @PostMapping
    public Result<ScheduleDTO> createSchedule(@RequestBody @Valid ScheduleFormDTO formDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(formDTO, schedule);
        return Result.success(scheduleService.createSchedule(schedule));
    }

    @Operation(summary = "获取排班")
    @GetMapping("/{id}")
    public Result<ScheduleDTO> getSchedule(@PathVariable String id) {
        return Result.success(scheduleService.getScheduleById(id));
    }

    @Operation(summary = "获取所有排班")
    @GetMapping
    public Result<List<ScheduleDTO>> listSchedules() {
        return Result.success(scheduleService.listSchedules());
    }

    @Operation(summary = "更新排班")
    @PutMapping("/{id}")
    public Result<ScheduleDTO> updateSchedule(@PathVariable String id, @RequestBody @Valid ScheduleFormDTO formDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(formDTO, schedule);
        schedule.setId(id);
        return Result.success(scheduleService.updateSchedule(schedule));
    }

    @Operation(summary = "删除排班")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteSchedule(@PathVariable String id) {
        return Result.success(scheduleService.deleteSchedule(id));
    }

    @Operation(summary = "更新排班状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable String id, @RequestParam String status) {
        scheduleService.updateStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "批量创建排班")
    @PostMapping("/batch")
    public Result<Void> batchCreateSchedule(@RequestBody @Valid ScheduleFormDTO formDTO) {
        scheduleService.batchCreateSchedule(formDTO);
        return Result.success();
    }

    @Operation(summary = "批量删除排班")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteSchedule(@RequestBody List<String> ids) {
        scheduleService.batchDeleteSchedule(ids);
        return Result.success();
    }

    @Operation(summary = "批量更新排班状态")
    @PutMapping("/batch/status")
    public Result<Void> batchUpdateStatus(@RequestBody List<String> ids, @RequestParam String status) {
        scheduleService.batchUpdateStatus(ids, status);
        return Result.success();
    }

    @Operation(summary = "获取医生排班")
    @GetMapping("/doctor/{doctorId}")
    public Result<List<ScheduleDTO>> getDoctorSchedules(@PathVariable String doctorId,
                                                      @RequestParam LocalDate startDate,
                                                      @RequestParam LocalDate endDate) {
        return Result.success(scheduleService.getDoctorSchedules(doctorId, startDate, endDate));
    }
} 