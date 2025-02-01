package com.yunchuan.medical.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunchuan.medical.common.result.Result;
import com.yunchuan.medical.dto.DepartmentDTO;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 科室表 前端控制器
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Tag(name = "科室管理")
@RestController
@RequestMapping(value = "/department", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@CrossOrigin
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 获取科室列表（树形结构）
     */
    @Operation(summary = "获取科室列表")
    @GetMapping("/list")
    public ResponseEntity<Result<List<DepartmentDTO>>> getDepartmentList() {
        List<Department> departments = departmentService.getDepartmentList();
        List<DepartmentDTO> dtoList = departments.stream()
                .map(dept -> {
                    DepartmentDTO dto = new DepartmentDTO();
                    BeanUtils.copyProperties(dept, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Result.success(buildDepartmentTree(dtoList)));
    }

    /**
     * 构建科室树形结构
     */
    private List<DepartmentDTO> buildDepartmentTree(List<DepartmentDTO> deptList) {
        List<DepartmentDTO> tree = new ArrayList<>();
        for (DepartmentDTO dept : deptList) {
            if (dept.getParentId() == null || dept.getParentId().isEmpty()) {
                tree.add(findChildren(dept, deptList));
            }
        }
        return tree;
    }

    /**
     * 递归查找子科室
     */
    private DepartmentDTO findChildren(DepartmentDTO dept, List<DepartmentDTO> deptList) {
        dept.setChildren(new ArrayList<>());
        for (DepartmentDTO item : deptList) {
            if (dept.getId().equals(item.getParentId())) {
                dept.getChildren().add(findChildren(item, deptList));
            }
        }
        return dept;
    }

    /**
     * 获取科室详情
     */
    @Operation(summary = "获取科室详情")
    @GetMapping("/{id}")
    public Result<Department> getDepartmentDetail(@PathVariable String id) {
        return Result.success(departmentService.getById(id));
    }

    /**
     * 创建科室
     */
    @Operation(summary = "创建科室")
    @PostMapping
    public Result<Void> createDepartment(@RequestBody Department department) {
        boolean success = departmentService.save(department);
        return success ? Result.success() : Result.error("创建科室失败");
    }

    /**
     * 更新科室
     */
    @Operation(summary = "更新科室")
    @PutMapping("/{id}")
    public Result<Department> updateDepartment(@PathVariable String id, @RequestBody Department department) {
        department.setId(id);
        departmentService.updateById(department);
        return Result.success(department);
    }

    /**
     * 删除科室
     */
    @Operation(summary = "删除科室")
    @DeleteMapping("/{id}")
    public Result<Department> deleteDepartment(@PathVariable String id) {
        Department department = departmentService.getById(id);
        if (department == null) {
            return Result.error("科室不存在");
        }
        departmentService.removeById(id);
        return Result.success(department);
    }

    /**
     * 分页查询科室列表
     */
    @Operation(summary = "分页查询科室列表")
    @GetMapping("/page")
    public Result<Page<Department>> getDepartmentPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Department> page = new Page<>(current, size);
        return Result.success(departmentService.page(page));
    }
}
