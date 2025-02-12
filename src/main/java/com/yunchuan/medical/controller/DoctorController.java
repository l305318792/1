package com.yunchuan.medical.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.util.HashMap;
import java.util.Map;

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
    public Result<Map<String, Object>> getDoctorList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String departmentId) {
        log.info("获取医生列表 - current: {}, size: {}, departmentId: {}", current, size, departmentId);
        
        // 构建查询条件
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        // 只查询状态正常的医生
        queryWrapper.eq("status", 1);
        
        if (departmentId != null && !departmentId.isEmpty()) {
            queryWrapper.eq("department_id", departmentId);
        }
        
        // 按评分和问诊次数降序排序
        queryWrapper.orderByDesc("rating", "consult_count");
        
        // 先查询总数
        long total = doctorService.count(queryWrapper);
        
        // 执行分页查询
        Page<Doctor> page = new Page<>(current, size);
        Page<Doctor> doctorPage = doctorService.page(page, queryWrapper);
        
        // 计算总页数
        long pages = (total + size - 1) / size;
        
        log.info("查询到医生数量: {}, 总记录数: {}, 总页数: {}", 
            doctorPage.getRecords().size(), total, pages);
        
        // 转换记录
        List<DoctorDTO> dtoList = doctorPage.getRecords().stream()
                .map(doctor -> {
                    DoctorDTO dto = new DoctorDTO();
                    BeanUtils.copyProperties(doctor, dto);
                    
                    // 获取科室名称
                    Department department = departmentService.getById(doctor.getDepartmentId());
                    if (department != null) {
                        dto.setDepartmentName(department.getName());
                        log.info("设置科室名称: {} -> {}", doctor.getDepartmentId(), department.getName());
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());
        
        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("records", dtoList);
        result.put("total", total);
        result.put("size", size);
        result.put("current", current);
        result.put("pages", pages);
        
        return Result.success(result);
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
