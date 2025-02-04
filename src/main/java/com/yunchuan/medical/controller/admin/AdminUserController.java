package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.UserDTO;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员用户管理
 */
@Tag(name = "管理员-用户管理")
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminUserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "获取用户列表")
    @GetMapping
    public Result<Object> list() {
        List<User> users = userService.list();
        List<UserDTO> userDTOs = users.stream()
            .map(user -> UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .status(user.getStatus())
                .createTime(user.getCreateTime())
                .updateTime(user.getUpdateTime())
                .build())
            .collect(Collectors.toList());
                
        return Result.ok(new Object() {
            public final List<UserDTO> list = userDTOs;
            public final int total = userDTOs.size();
        });
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public Result<UserDTO> get(@PathVariable String id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        UserDTO userDTO = UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .name(user.getName())
            .phone(user.getPhone())
            .email(user.getEmail())
            .status(user.getStatus())
            .createTime(user.getCreateTime())
            .updateTime(user.getUpdateTime())
            .build();
            
        return Result.ok(userDTO);
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public Result<UserDTO> add(@RequestBody UserDTO userDTO) {
        // 生成用户ID
        String userId = "U" + System.currentTimeMillis();
        
        User user = User.builder()
            .id(userId)
            .username(userDTO.getUsername())
            .password(passwordEncoder.encode(userDTO.getPassword()))
            .name(userDTO.getName())
            .phone(userDTO.getPhone())
            .email(userDTO.getEmail())
            .role(userDTO.getRole())
            .status("ACTIVE")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
            
        userService.save(user);
        
        userDTO.setId(user.getId());
        userDTO.setStatus(user.getStatus());
        userDTO.setCreateTime(user.getCreateTime());
        userDTO.setUpdateTime(user.getUpdateTime());
        
        return Result.ok(userDTO);
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public Result<UserDTO> update(@PathVariable String id, @RequestBody UserDTO userDTO) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }
        
        User user = User.builder()
            .id(id)
            .username(userDTO.getUsername())
            .password(userDTO.getPassword() != null ? 
                     passwordEncoder.encode(userDTO.getPassword()) : 
                     existingUser.getPassword())
            .name(userDTO.getName())
            .phone(userDTO.getPhone())
            .email(userDTO.getEmail())
            .role(userDTO.getRole() != null ? userDTO.getRole() : existingUser.getRole())
            .status(userDTO.getStatus() != null ? userDTO.getStatus() : existingUser.getStatus())
            .createTime(existingUser.getCreateTime())
            .updateTime(LocalDateTime.now())
            .build();
            
        userService.updateById(user);
        
        userDTO.setId(user.getId());
        userDTO.setStatus(user.getStatus());
        userDTO.setCreateTime(user.getCreateTime());
        userDTO.setUpdateTime(user.getUpdateTime());
        
        return Result.ok(userDTO);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable String id) {
        return Result.ok(userService.removeById(id));
    }
} 