package com.yunchuan.medical.controller.patient;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ScheduleDTO;
import com.yunchuan.medical.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 患者-医生管理
 */
@Tag(name = "患者-医生管理")
@RestController
@RequestMapping("/patient/doctors")
public class PatientDoctorController {

    private final DoctorService doctorService;

    public PatientDoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(summary = "获取医生排班")
    @GetMapping("/{doctorId}/schedule")
    public Result<List<ScheduleDTO>> getDoctorSchedule(
            @PathVariable String doctorId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        if (startDate == null || endDate == null) {
            // 如果没有指定日期范围，则返回一周内的排班
            return Result.ok(doctorService.getDoctorSchedule(doctorId));
        } else {
            // 如果指定了日期范围，则返回指定范围内的排班
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return Result.ok(doctorService.getDoctorSchedule(doctorId, start, end));
        }
    }
} 