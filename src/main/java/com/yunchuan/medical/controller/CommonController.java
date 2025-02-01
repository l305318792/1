package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.DepartmentDTO;
import com.yunchuan.medical.dto.DoctorDTO;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公共接口
 */
@Tag(name = "公共接口")
@RestController
@RequestMapping("/common")
public class CommonController {

    private final DepartmentService departmentService;

    public CommonController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 获取科室列表
     */
    @Operation(summary = "获取科室列表")
    @GetMapping("/department/list")
    public Result<List<DepartmentDTO>> getDepartmentList() {
        List<Department> departments = departmentService.list();
        List<DepartmentDTO> dtoList = departments.stream()
                .map(dept -> {
                    DepartmentDTO dto = new DepartmentDTO();
                    dto.setId(dept.getId());
                    dto.setName(dept.getName());
                    dto.setParentId(dept.getParentId());
                    dto.setIntroduction(dept.getIntroduction());
                    return dto;
                })
                .collect(Collectors.toList());
        return Result.ok(buildDepartmentTree(dtoList));
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
     * 获取科室下拉列表
     */
    @GetMapping("/department/select")
    public Result<List<DepartmentDTO>> getDepartmentSelect() {
        // 模拟一些科室数据
        List<DepartmentDTO> departments = Arrays.asList(
            DepartmentDTO.builder()
                .id("1")
                .name("内科")
                .build(),
            DepartmentDTO.builder()
                .id("2")
                .name("外科")
                .build(),
            DepartmentDTO.builder()
                .id("3")
                .name("儿科")
                .build()
        );
        return Result.ok(departments);
    }

    /**
     * 获取科室下的医生下拉列表
     */
    @GetMapping("/doctor/select")
    public Result<List<DoctorDTO>> getDoctorSelect(@RequestParam String departmentId) {
        // 模拟一些医生数据
        List<DoctorDTO> doctors = Arrays.asList(
            DoctorDTO.builder()
                .id("1")
                .name("张医生")
                .departmentId(departmentId)
                .build(),
            DoctorDTO.builder()
                .id("2")
                .name("李医生")
                .departmentId(departmentId)
                .build()
        );
        return Result.ok(doctors);
    }

    /**
     * 上传
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestBody Object file) {
        return Result.ok("");
    }

    /**
     * 下载
     */
    @GetMapping("/download/{id}")
    public Result<String> download(@PathVariable String id) {
        return Result.ok("");
    }

    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public Result<Object> captcha() {
        return Result.ok(new Object());
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms")
    public Result<Object> sendSms(@RequestBody Object phone) {
        return Result.ok(new Object());
    }
}