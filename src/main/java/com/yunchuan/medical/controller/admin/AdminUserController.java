package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 管理员用户管理
 */
@Tag(name = "管理员-用户管理")
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    @Operation(summary = "获取用户列表")
    @GetMapping
    public Result<Object> list() {
        // 模拟用户数据
        UserDTO admin = UserDTO.builder()
                .id("1")
                .username("admin")
                .phone("13800138000")
                .email("admin@example.com")
                .status("normal")
                .createTime(LocalDateTime.now())
                .build();
                
        UserDTO doctor = UserDTO.builder()
                .id("2")
                .username("doctor1")
                .phone("13800138001")
                .email("doctor1@example.com")
                .status("normal")
                .createTime(LocalDateTime.now())
                .build();
                
        return Result.ok(new Object() {
            public final List<UserDTO> list = Arrays.asList(admin, doctor);
            public final int total = 2;
        });
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public Result<UserDTO> get(@PathVariable String id) {
        UserDTO user = UserDTO.builder()
                .id(id)
                .username("admin")
                .phone("13800138000")
                .email("admin@example.com")
                .status("normal")
                .createTime(LocalDateTime.now())
                .build();
        return Result.ok(user);
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public Result<UserDTO> add(@RequestBody UserDTO user) {
        user.setId("3");
        user.setCreateTime(LocalDateTime.now());
        return Result.ok(user);
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public Result<UserDTO> update(@PathVariable String id, @RequestBody UserDTO user) {
        user.setId(id);
        user.setUpdateTime(LocalDateTime.now());
        return Result.ok(user);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable String id) {
        return Result.ok(true);
    }
} 