package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.AppointmentDTO;
import com.yunchuan.medical.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 预约表 前端控制器
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Tag(name = "预约管理")
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * 创建预约
     */
    @Operation(summary = "创建预约")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Result<AppointmentDTO> createAppointment(@RequestBody @Valid AppointmentDTO appointment) {
        return Result.ok(appointmentService.createAppointment(appointment));
    }

    /**
     * 支付预约
     */
    @Operation(summary = "支付预约")
    @PostMapping("/{id}/pay")
    @PreAuthorize("hasRole('USER')")
    public Result<AppointmentDTO> payAppointment(@PathVariable String id) {
        return Result.ok(appointmentService.payAppointment(id));
    }

    /**
     * 取消预约
     */
    @Operation(summary = "取消预约")
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public Result<AppointmentDTO> cancelAppointment(@PathVariable String id) {
        return Result.ok(appointmentService.cancelAppointment(id));
    }

    /**
     * 获取预约详情
     */
    @Operation(summary = "获取预约详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'DOCTOR', 'ADMIN')")
    public Result<AppointmentDTO> getAppointment(@PathVariable String id) {
        return Result.ok(appointmentService.getAppointmentById(id));
    }

    /**
     * 获取我的预约列表
     */
    @Operation(summary = "获取我的预约列表")
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public Result<List<AppointmentDTO>> getMyAppointments() {
        return Result.ok(appointmentService.getMyAppointments());
    }
}
