package com.yunchuan.medical.controller.doctor;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.AppointmentDTO;
import com.yunchuan.medical.service.AppointmentService;
import com.yunchuan.medical.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 医生预约管理控制器
 * @author yunchuan
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "医生-预约管理")
@RestController
@RequestMapping("/api/doctor/appointments")
public class DoctorAppointmentController {

    private final AppointmentService appointmentService;

    public DoctorAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * 获取医生的预约列表
     */
    @Operation(summary = "获取医生的预约列表")
    @GetMapping
    public Result<List<AppointmentDTO>> getDoctorAppointments() {
        String doctorId = SecurityUtil.getCurrentUserId();
        return Result.ok(appointmentService.getDoctorAppointments(doctorId));
    }

    /**
     * 获取预约详情
     */
    @Operation(summary = "获取预约详情")
    @GetMapping("/{id}")
    public Result<AppointmentDTO> getAppointment(@PathVariable String id) {
        return Result.ok(appointmentService.getAppointmentById(id));
    }

    /**
     * 更新预约状态
     */
    @Operation(summary = "更新预约状态")
    @PutMapping("/{id}/status")
    public Result<AppointmentDTO> updateStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestParam(required = false) String reason) {
        return Result.ok(appointmentService.updateAppointmentStatus(id, status, reason));
    }
} 