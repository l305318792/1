package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.AppointmentDTO;
import com.yunchuan.medical.dto.AppointmentQueryDTO;
import com.yunchuan.medical.entity.Appointment;
import com.yunchuan.medical.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 管理员预约管理
 */
@Tag(name = "管理员-预约管理")
@RestController
@RequestMapping("/admin/appointments")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    public AdminAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(summary = "获取预约列表")
    @GetMapping
    public Result<IPage<AppointmentDTO>> list(AppointmentQueryDTO query) {
        Page<Appointment> page = new Page<>(query.getCurrent(), query.getSize());
        IPage<AppointmentDTO> appointments = appointmentService.getAppointmentsByPage(page, query);
        return Result.ok(appointments);
    }

    @Operation(summary = "获取预约详情")
    @GetMapping("/{id}")
    public Result<AppointmentDTO> getAppointment(@PathVariable String id) {
        return Result.ok(appointmentService.getAppointmentById(id));
    }

    @Operation(summary = "更新预约状态")
    @PutMapping("/{id}/status")
    public Result<AppointmentDTO> updateStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestParam(required = false) String reviewReason) {
        return Result.ok(appointmentService.updateAppointmentStatus(id, status, reviewReason));
    }
} 