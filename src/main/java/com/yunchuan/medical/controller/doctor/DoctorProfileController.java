package com.yunchuan.medical.controller.doctor;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.DoctorProfileDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

/**
 * 医生个人信息管理
 */
@Tag(name = "医生-个人信息管理")
@RestController
@RequestMapping("/doctor/profile")
public class DoctorProfileController {

    @Operation(summary = "获取个人信息")
    @GetMapping
    public Result<DoctorProfileDTO> getProfile() {
        // 模拟医生个人信息数据
        DoctorProfileDTO profile = DoctorProfileDTO.builder()
                .id("1")
                .name("张医生")
                .title("主任医师")
                .departmentId("1")
                .departmentName("内科")
                .specialty("心血管疾病")
                .introduction("从事心血管疾病临床诊疗工作20年，具有丰富的临床经验...")
                .avatar("https://example.com/avatar.jpg")
                .phone("13800138000")
                .email("doctor@example.com")
                .consultationCount(1000)
                .rating(4.8)
                .status("ACTIVE")
                .createTime(LocalDateTime.now().minusYears(1))
                .updateTime(LocalDateTime.now())
                .build();
        return Result.ok(profile);
    }

    @Operation(summary = "更新个人信息")
    @PutMapping
    public Result<DoctorProfileDTO> updateProfile(@RequestBody DoctorProfileDTO profile) {
        profile.setId("1");
        profile.setUpdateTime(LocalDateTime.now());
        return Result.ok(profile);
    }

    @Operation(summary = "更新头像")
    @PostMapping("/avatar")
    public Result<String> updateAvatar(@RequestParam String avatarUrl) {
        return Result.ok(avatarUrl);
    }

    @Operation(summary = "修改密码")
    @PostMapping("/password")
    public Result<Boolean> updatePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        // 模拟密码修改
        return Result.ok(true);
    }
} 