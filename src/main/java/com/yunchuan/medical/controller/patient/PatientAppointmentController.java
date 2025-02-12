package com.yunchuan.medical.controller.patient;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.AppointmentDTO;
import com.yunchuan.medical.service.AppointmentService;
import com.yunchuan.medical.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 患者预约管理
 */
@Tag(name = "患者-预约管理")
@RestController
@RequestMapping("/patient/appointments")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class PatientAppointmentController {

    private final AppointmentService appointmentService;

    public PatientAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * 获取我的预约列表
     */
    @Operation(summary = "获取我的预约列表")
    @GetMapping("/my")
    public Result<List<AppointmentDTO>> getMyAppointments() {
        return Result.ok(appointmentService.getMyAppointments());
    }

    /**
     * 创建预约
     */
    @Operation(summary = "创建预约")
    @PostMapping
    public Result<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        appointmentDTO.setUserId(userId);
        return Result.ok(appointmentService.createAppointment(appointmentDTO));
    }

    /**
     * 取消预约
     */
    @Operation(summary = "取消预约")
    @PostMapping("/{id}/cancel")
    public Result<AppointmentDTO> cancelAppointment(@PathVariable String id) {
        return Result.ok(appointmentService.cancelAppointment(id));
    }

    /**
     * 获取预约详情
     */
    @Operation(summary = "获取预约详情")
    @GetMapping("/{id}")
    public Result<AppointmentDTO> getAppointment(@PathVariable String id) {
        return Result.ok(appointmentService.getAppointmentById(id));
    }
} 