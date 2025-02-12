package com.yunchuan.medical.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunchuan.medical.common.result.Result;
import com.yunchuan.medical.dto.DepartmentDTO;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 科室管理
 */
@Tag(name = "科室管理")
@RestController
@RequestMapping("/admin/department")
public class AdminDepartmentController {

    private final DepartmentService departmentService;

    public AdminDepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 获取科室列表
     */
    @Operation(summary = "获取科室列表")
    @GetMapping("/list")
    public Result<List<DepartmentDTO>> getDepartmentList(@RequestParam(required = false) String keyword) {
        // 使用QueryWrapper进行条件查询
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.like("name", keyword)
                       .or()
                       .like("introduction", keyword);
        }
        // 添加排序条件
        queryWrapper.orderByAsc("sort_order", "create_time");
        
        List<Department> departments = departmentService.list(queryWrapper);
        List<DepartmentDTO> dtoList = departments.stream()
                .map(dept -> {
                    DepartmentDTO dto = DepartmentDTO.builder()
                            .id(dept.getId())
                            .name(dept.getName())
                            .parentId(dept.getParentId())
                            .introduction(dept.getIntroduction())
                            .status(dept.getStatus())
                            .createTime(dept.getCreateTime())
                            .updateTime(dept.getUpdateTime())
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());
        return Result.success(buildDepartmentTree(dtoList));
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
    public Result<DepartmentDTO> getDepartmentDetail(@PathVariable String id) {
        Department department = departmentService.getById(id);
        if (department == null) {
            return Result.error("科室不存在");
        }
        DepartmentDTO dto = DepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .parentId(department.getParentId())
                .introduction(department.getIntroduction())
                .status(department.getStatus())
                .createTime(department.getCreateTime())
                .updateTime(department.getUpdateTime())
                .build();
        return Result.success(dto);
    }

    /**
     * 创建科室
     */
    @Operation(summary = "创建科室")
    @PostMapping
    public Result<Void> createDepartment(@RequestBody DepartmentDTO dto) {
        Department department = new Department();
        BeanUtils.copyProperties(dto, department);
        department.setCreateTime(LocalDateTime.now());
        department.setUpdateTime(LocalDateTime.now());
        boolean success = departmentService.save(department);
        return success ? Result.success() : Result.error("创建科室失败");
    }

    /**
     * 更新科室
     */
    @Operation(summary = "更新科室")
    @PutMapping("/{id}")
    public Result<Void> updateDepartment(@PathVariable String id, @RequestBody DepartmentDTO dto) {
        Department department = departmentService.getById(id);
        if (department == null) {
            return Result.error("科室不存在");
        }
        BeanUtils.copyProperties(dto, department);
        boolean success = departmentService.updateById(department);
        return success ? Result.success() : Result.error("更新科室失败");
    }

    /**
     * 删除科室
     */
    @Operation(summary = "删除科室")
    @DeleteMapping("/{id}")
    public Result<Void> deleteDepartment(@PathVariable String id) {
        boolean success = departmentService.removeById(id);
        return success ? Result.success() : Result.error("删除科室失败");
    }

    /**
     * 更新科室状态
     */
    @Operation(summary = "更新科室状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateDepartmentStatus(@PathVariable String id, @RequestParam String status) {
        Department department = departmentService.getById(id);
        if (department == null) {
            return Result.error("科室不存在");
        }
        department.setStatus(status);
        department.setUpdateTime(LocalDateTime.now());
        boolean success = departmentService.updateById(department);
        return success ? Result.success() : Result.error("更新科室状态失败");
    }

    /**
     * 批量删除科室
     */
    @Operation(summary = "批量删除科室")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteDepartments(@RequestBody List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的科室");
        }
        boolean success = departmentService.removeByIds(ids);
        return success ? Result.success() : Result.error("批量删除科室失败");
    }

    /**
     * 更新科室排序
     */
    @Operation(summary = "更新科室排序")
    @PutMapping("/{id}/sort")
    public Result<Void> updateDepartmentSort(@PathVariable String id, @RequestParam Integer sortOrder) {
        Department department = departmentService.getById(id);
        if (department == null) {
            return Result.error("科室不存在");
        }
        department.setSortOrder(sortOrder);
        department.setUpdateTime(LocalDateTime.now());
        boolean success = departmentService.updateById(department);
        return success ? Result.success() : Result.error("更新科室排序失败");
    }
} 