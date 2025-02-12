package com.yunchuan.medical.controller.patient;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.DepartmentDTO;
import com.yunchuan.medical.dto.DoctorDTO;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.service.DepartmentService;
import com.yunchuan.medical.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 患者-科室管理
 */
@Tag(name = "患者-科室管理")
@RestController
@RequestMapping("/patient/departments")
public class PatientDepartmentController {

    private final DepartmentService departmentService;
    private final DoctorService doctorService;

    public PatientDepartmentController(DepartmentService departmentService, DoctorService doctorService) {
        this.departmentService = departmentService;
        this.doctorService = doctorService;
    }

    @Operation(summary = "获取科室列表")
    @GetMapping
    public Result<List<DepartmentDTO>> getDepartmentList() {
        List<Department> departments = departmentService.getDepartmentList();
        List<DepartmentDTO> dtoList = departments.stream()
                .map(dept -> {
                    DepartmentDTO dto = new DepartmentDTO();
                    BeanUtils.copyProperties(dept, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        return Result.ok(dtoList);
    }

    @Operation(summary = "获取科室下的医生列表")
    @GetMapping("/{departmentId}/doctors")
    public Result<List<DoctorDTO>> getDoctorsByDepartment(@PathVariable String departmentId) {
        List<DoctorDTO> doctors = doctorService.getDoctorsByDepartment(departmentId);
        return Result.ok(doctors);
    }
} 