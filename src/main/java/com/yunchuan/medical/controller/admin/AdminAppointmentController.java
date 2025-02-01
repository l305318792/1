package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.AppointmentDTO;
import com.yunchuan.medical.dto.AppointmentQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员预约管理
 */
@Tag(name = "管理员-预约管理")
@RestController
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

    @Operation(summary = "获取预约列表")
    @GetMapping
    public Result<Object> list(AppointmentQueryDTO query) {
        // 模拟预约数据
        AppointmentDTO appointment1 = AppointmentDTO.builder()
                .id("1")
                .userId("u1")
                .doctorId("1")
                .doctorName("张医生")
                .departmentId("1")
                .departmentName("内科")
                .scheduleId("1")
                .appointmentTime(LocalDateTime.now().plusDays(1))
                .status("PENDING")
                .remark("头痛，想做个检查")
                .createTime(LocalDateTime.now())
                .build();
                
        AppointmentDTO appointment2 = AppointmentDTO.builder()
                .id("2")
                .userId("u2")
                .doctorId("2")
                .doctorName("李医生")
                .departmentId("2")
                .departmentName("外科")
                .scheduleId("2")
                .appointmentTime(LocalDateTime.now().plusDays(2))
                .status("CONFIRMED")
                .remark("复查")
                .createTime(LocalDateTime.now())
                .build();
                
        return Result.ok(new Object() {
            public final List<AppointmentDTO> list = Arrays.asList(appointment1, appointment2);
            public final int total = 2;
        });
    }

    @Operation(summary = "获取预约统计")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        // 模拟统计数据
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", 100);
        statistics.put("pending", 20);
        statistics.put("confirmed", 60);
        statistics.put("completed", 15);
        statistics.put("cancelled", 5);
        
        // 各科室预约数量
        Map<String, Integer> departmentStats = new HashMap<>();
        departmentStats.put("内科", 40);
        departmentStats.put("外科", 30);
        departmentStats.put("儿科", 30);
        statistics.put("departmentStats", departmentStats);
        
        return Result.ok(statistics);
    }

    @Operation(summary = "审核预约")
    @PostMapping("/{id}/review")
    public Result<AppointmentDTO> reviewAppointment(
            @PathVariable String id,
            @RequestParam String action,  // CONFIRM-确认, REJECT-拒绝
            @RequestParam(required = false) String reason) {
        
        AppointmentDTO appointment = AppointmentDTO.builder()
                .id(id)
                .userId("u1")
                .doctorId("1")
                .doctorName("张医生")
                .departmentId("1")
                .departmentName("内科")
                .scheduleId("1")
                .appointmentTime(LocalDateTime.now().plusDays(1))
                .status(action.equals("CONFIRM") ? "CONFIRMED" : "REJECTED")
                .remark("头痛，想做个检查")
                .reviewReason(reason)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
                
        return Result.ok(appointment);
    }
} 