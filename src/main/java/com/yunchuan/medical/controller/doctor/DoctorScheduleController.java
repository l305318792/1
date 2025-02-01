package com.yunchuan.medical.controller.doctor;

import com.yunchuan.medical.common.result.Result;
import com.yunchuan.medical.dto.ScheduleDTO;
import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 医生排班控制器
 */
@Slf4j
@Tag(name = "医生-排班管理")
@RestController
@RequestMapping("/api/doctor/schedules")
public class DoctorScheduleController {

    private final ScheduleService scheduleService;

    public DoctorScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(summary = "获取我的排班列表")
    @GetMapping
    public Result<List<ScheduleDTO>> getDoctorSchedules(
            @RequestParam String doctorId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return Result.success(scheduleService.getDoctorSchedules(doctorId, startDate, endDate));
    }

    @Operation(summary = "获取排班信息")
    @GetMapping("/{id}")
    public Result<ScheduleDTO> getSchedule(@PathVariable String id) {
        return Result.success(scheduleService.getScheduleById(id));
    }

    @Operation(summary = "更新排班信息")
    @PutMapping("/{id}")
    public Result<ScheduleDTO> updateSchedule(
            @PathVariable String id,
            @RequestBody ScheduleDTO scheduleDTO) {
        // 模拟更新排班
        scheduleDTO.setId(id);
        scheduleDTO.setDoctorId("1");
        scheduleDTO.setDoctorName("张医生");
        scheduleDTO.setDepartmentId("1");
        scheduleDTO.setDepartmentName("内科");
        scheduleDTO.setUpdateTime(LocalDateTime.now());
        return Result.success(scheduleDTO);
    }

    @Operation(summary = "获取待处理问诊列表")
    @GetMapping("/consultations/pending")
    public Result<Object> getPendingConsultations() {
        // 模拟待处理问诊数据
        ConsultationDTO consultation1 = ConsultationDTO.builder()
                .id("1")
                .userId("u1")
                .userName("张三")
                .doctorId("1")
                .doctorName("张医生")
                .departmentId("1")
                .departmentName("内科")
                .appointmentId("1")
                .symptoms("头痛，发烧38度")
                .status("PENDING")
                .createTime(LocalDateTime.now().minusHours(2))
                .build();
                
        ConsultationDTO consultation2 = ConsultationDTO.builder()
                .id("2")
                .userId("u2")
                .userName("李四")
                .doctorId("1")
                .doctorName("张医生")
                .departmentId("1")
                .departmentName("内科")
                .appointmentId("2")
                .symptoms("咳嗽，胸闷")
                .status("PENDING")
                .createTime(LocalDateTime.now().minusHours(1))
                .build();
                
        return Result.success(new Object() {
            public final List<ConsultationDTO> list = Arrays.asList(consultation1, consultation2);
            public final int total = 2;
        });
    }

    @Operation(summary = "获取问诊统计信息")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        // 模拟统计数据
        Map<String, Object> statistics = new HashMap<>();
        
        // 总体统计
        statistics.put("totalConsultations", 100);
        statistics.put("completedConsultations", 80);
        statistics.put("pendingConsultations", 15);
        statistics.put("cancelledConsultations", 5);
        
        // 本月统计
        Map<String, Integer> monthlyStats = new HashMap<>();
        monthlyStats.put("total", 30);
        monthlyStats.put("completed", 25);
        monthlyStats.put("pending", 4);
        monthlyStats.put("cancelled", 1);
        statistics.put("monthlyStats", monthlyStats);
        
        // 满意度统计
        Map<String, Object> satisfactionStats = new HashMap<>();
        satisfactionStats.put("averageRating", 4.8);
        satisfactionStats.put("totalRatings", 75);
        satisfactionStats.put("fiveStarRatings", 60);
        satisfactionStats.put("fourStarRatings", 10);
        satisfactionStats.put("threeStarRatings", 5);
        statistics.put("satisfactionStats", satisfactionStats);
        
        // 每日问诊数量（近7天）
        Map<String, Integer> dailyStats = new HashMap<>();
        dailyStats.put("2024-01-24", 4);
        dailyStats.put("2024-01-25", 5);
        dailyStats.put("2024-01-26", 3);
        dailyStats.put("2024-01-27", 6);
        dailyStats.put("2024-01-28", 4);
        dailyStats.put("2024-01-29", 5);
        dailyStats.put("2024-01-30", 3);
        statistics.put("dailyStats", dailyStats);
        
        return Result.success(statistics);
    }
} 