package com.yunchuan.medical.controller.patient;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.UserDTO;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 患者个人信息管理
 */
@Tag(name = "患者-个人信息管理")
@RestController
@RequestMapping("/patient/profile")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class PatientProfileController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public PatientProfileController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "获取个人信息")
    @GetMapping
    public Result<UserDTO> getProfile() {
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = userService.getUserByUsername(username);
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

    @Operation(summary = "更新个人信息")
    @PutMapping
    public Result<UserDTO> updateProfile(@RequestBody UserDTO userDTO) {
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User existingUser = userService.getUserByUsername(username);
        if (existingUser == null) {
            return Result.error("用户不存在");
        }
        
        // 更新用户信息
        existingUser.setName(userDTO.getName());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setEmail(userDTO.getEmail());
        
        userService.updateById(existingUser);
        
        userDTO.setId(existingUser.getId());
        userDTO.setUsername(existingUser.getUsername());
        userDTO.setStatus(existingUser.getStatus());
        userDTO.setCreateTime(existingUser.getCreateTime());
        userDTO.setUpdateTime(existingUser.getUpdateTime());
        
        return Result.ok(userDTO);
    }

    @Operation(summary = "修改密码")
    @PostMapping("/password")
    public Result<Boolean> updatePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.error("旧密码错误");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateById(user);
        
        return Result.ok(true);
    }
} 