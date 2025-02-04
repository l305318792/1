package com.yunchuan.medical.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.DoctorDTO;
import com.yunchuan.medical.dto.ScheduleDTO;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.service.DoctorService;
import com.yunchuan.medical.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 医生表 前端控制器
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Slf4j
@Tag(name = "医生管理")
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;

    public DoctorController(DoctorService doctorService, DepartmentService departmentService) {
        this.doctorService = doctorService;
        this.departmentService = departmentService;
    }

    /**
     * 获取医生列表
     */
    @Operation(summary = "获取医生列表")
    @GetMapping("/list")
    public Result<Page<DoctorDTO>> getDoctorList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("获取医生列表 - current: {}, size: {}", current, size);
        
        Page<Doctor> page = new Page<>(current, size);
        Page<Doctor> doctorPage = doctorService.page(page);
        
        log.info("查询到医生数量: {}", doctorPage.getRecords().size());
        
        Page<DoctorDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(doctorPage, dtoPage, "records");
        
        List<DoctorDTO> dtoList = doctorPage.getRecords().stream()
                .map(doctor -> {
                    DoctorDTO dto = DoctorDTO.builder()
                        .id(doctor.getId())
                        .userId(doctor.getUserId())
                        .name(doctor.getName())
                        .phone(doctor.getPhone())
                        .departmentId(doctor.getDepartmentId())
                        .title(doctor.getTitle())
                        .specialty(doctor.getSpecialty())
                        .introduction(doctor.getIntroduction())
                        .consultationFee(doctor.getConsultationFee())
                        .rating(doctor.getRating())
                        .ratingCount(doctor.getRatingCount())
                        .status(doctor.getStatus())
                        .consultCount(doctor.getConsultCount())
                        .build();
                    
                    // 获取科室名称
                    Department department = departmentService.getById(doctor.getDepartmentId());
                    if (department != null) {
                        dto.setDepartmentName(department.getName());
                        log.info("设置科室名称: {} -> {}", doctor.getDepartmentId(), department.getName());
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
        
        dtoPage.setRecords(dtoList);
        return Result.ok(dtoPage);
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
            return Result.ok(doctorService.getDoctorSchedule(Long.parseLong(doctorId), start, end));
        }
    }
}
