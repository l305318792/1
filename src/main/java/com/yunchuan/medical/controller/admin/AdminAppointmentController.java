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
import com.yunchuan.medical.dto.AppointmentStatisticsDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * 管理员预约管理
 */
@Tag(name = "管理员-预约管理")
@RestController
@RequestMapping("/admin/appointment")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    public AdminAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(summary = "获取所有预约列表")
    @GetMapping("/list")
    public Result<List<AppointmentDTO>> getAllAppointments() {
        return Result.ok(appointmentService.getAllAppointments());
    }

    @Operation(summary = "获取预约列表（分页和筛选）")
    @GetMapping("/page")
    public Result<IPage<AppointmentDTO>> getAppointmentsByPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String patientPhone) {
        
        Page<Appointment> page = new Page<>(current, size);
        AppointmentQueryDTO query = new AppointmentQueryDTO();
        query.setCurrent((int)current);
        query.setSize((int)size);
        query.setDepartmentId(departmentId);
        query.setDoctorId(doctorId);
        query.setStatus(status);
        query.setPatientName(patientName);
        query.setPatientPhone(patientPhone);
        
        return Result.ok(appointmentService.getAppointmentsByPage(page, query));
    }

    @Operation(summary = "获取预约详情")
    @GetMapping("/{id}")
    public Result<AppointmentDTO> getAppointment(@PathVariable String id) {
        return Result.ok(appointmentService.getAppointmentById(id));
    }

    @Operation(summary = "更新预约信息")
    @PutMapping("/{id}")
    public Result<AppointmentDTO> updateAppointment(@PathVariable String id, @RequestBody AppointmentDTO appointmentDTO) {
        appointmentDTO.setId(id);
        return Result.ok(appointmentService.updateAppointment(appointmentDTO));
    }

    @Operation(summary = "更新预约状态")
    @PutMapping("/{id}/status")
    public Result<AppointmentDTO> updateStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestParam(required = false) String reviewReason) {
        return Result.ok(appointmentService.updateAppointmentStatus(id, status, reviewReason));
    }

    @Operation(summary = "获取预约统计数据")
    @GetMapping("/statistics")
    public Result<AppointmentStatisticsDTO> getStatistics() {
        return Result.ok(appointmentService.getAppointmentStatistics());
    }

    @Operation(summary = "批量更新预约金额")
    @PutMapping("/batch/amount")
    public Result<Integer> batchUpdateAmount(@RequestParam BigDecimal amount) {
        return Result.ok(appointmentService.batchUpdateAmount(amount));
    }
} 