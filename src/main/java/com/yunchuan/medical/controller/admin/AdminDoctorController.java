package com.yunchuan.medical.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunchuan.medical.common.result.Result;
import com.yunchuan.medical.dto.DoctorDTO;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.service.DepartmentService;
import com.yunchuan.medical.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员-医生管理
 */
@Tag(name = "管理员-医生管理")
@RestController
@RequestMapping("/admin/doctors")
public class AdminDoctorController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;

    public AdminDoctorController(DoctorService doctorService, DepartmentService departmentService) {
        this.doctorService = doctorService;
        this.departmentService = departmentService;
    }

    /**
     * 获取医生列表
     */
    @Operation(summary = "获取医生列表")
    @GetMapping
    public Result<Page<DoctorDTO>> getDoctorList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Doctor> page = new Page<>(current, size);
        Page<Doctor> doctorPage = doctorService.page(page);
        
        Page<DoctorDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(doctorPage, dtoPage, "records");
        
        List<DoctorDTO> dtoList = doctorPage.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        dtoPage.setRecords(dtoList);
        return Result.success(dtoPage);
    }

    /**
     * 获取医生详情
     */
    @Operation(summary = "获取医生详情")
    @GetMapping("/{id}")
    public Result<DoctorDTO> getDoctorDetail(@PathVariable String id) {
        Doctor doctor = doctorService.getById(id);
        if (doctor == null) {
            return Result.error("医生不存在");
        }
        return Result.success(convertToDTO(doctor));
    }

    /**
     * 创建医生
     */
    @Operation(summary = "创建医生")
    @PostMapping
    public Result<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        // 验证科室是否存在
        Department department = departmentService.getById(doctorDTO.getDepartmentId());
        if (department == null) {
            return Result.error("科室不存在");
        }

        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(doctorDTO, doctor);
        // 设置初始值
        doctor.setAverageRating(new BigDecimal("5.0"));
        doctor.setRatingCount(0);
        doctor.setCreateTime(LocalDateTime.now());
        doctor.setUpdateTime(LocalDateTime.now());
        
        boolean success = doctorService.save(doctor);
        if (!success) {
            return Result.error("创建医生失败");
        }
        
        return Result.success(convertToDTO(doctor));
    }

    /**
     * 更新医生
     */
    @Operation(summary = "更新医生")
    @PutMapping("/{id}")
    public Result<DoctorDTO> updateDoctor(@PathVariable String id, @RequestBody DoctorDTO doctorDTO) {
        Doctor doctor = doctorService.getById(id);
        if (doctor == null) {
            return Result.error("医生不存在");
        }

        // 验证科室是否存在
        if (!doctor.getDepartmentId().equals(doctorDTO.getDepartmentId())) {
            Department department = departmentService.getById(doctorDTO.getDepartmentId());
            if (department == null) {
                return Result.error("科室不存在");
            }
        }

        BeanUtils.copyProperties(doctorDTO, doctor);
        doctor.setId(id); // 确保ID不被修改
        
        boolean success = doctorService.updateById(doctor);
        if (!success) {
            return Result.error("更新医生失败");
        }
        
        return Result.success(convertToDTO(doctor));
    }

    /**
     * 删除医生
     */
    @Operation(summary = "删除医生")
    @DeleteMapping("/{id}")
    public Result<Void> deleteDoctor(@PathVariable String id) {
        Doctor doctor = doctorService.getById(id);
        if (doctor == null) {
            return Result.error("医生不存在");
        }
        
        boolean success = doctorService.removeById(id);
        return success ? Result.success() : Result.error("删除医生失败");
    }

    /**
     * 更新医生状态
     */
    @Operation(summary = "更新医生状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable String id, @RequestParam String status) {
        Doctor doctor = doctorService.getById(id);
        if (doctor == null) {
            return Result.error("医生不存在");
        }
        
        doctor.setStatus(status);
        boolean success = doctorService.updateById(doctor);
        return success ? Result.success() : Result.error("更新状态失败");
    }

    /**
     * 将Doctor实体转换为DTO
     */
    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        BeanUtils.copyProperties(doctor, dto);
        
        // 获取科室名称
        Department department = departmentService.getById(doctor.getDepartmentId());
        if (department != null) {
            dto.setDepartmentName(department.getName());
        }
        
        return dto;
    }
} 