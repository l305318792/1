package com.yunchuan.medical.controller.doctor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.DoctorProfileDTO;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.service.DoctorService;
import com.yunchuan.medical.service.DepartmentService;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import com.yunchuan.medical.service.FileService;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Map;

/**
 * 医生个人信息管理
 */
@Tag(name = "医生-个人信息管理")
@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
@Slf4j
public class DoctorProfileController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "获取个人信息")
    @GetMapping("/profile")
    public Result<DoctorProfileDTO> getProfile() {
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("获取医生个人信息, username: {}", username);
        
        // 先获取用户信息
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
                
        if (user == null) {
            log.error("用户信息不存在, username: {}", username);
            return Result.error("用户信息不存在");
        }
        
        // 从数据库获取医生信息
        Doctor doctor = doctorService.getOne(new LambdaQueryWrapper<Doctor>()
                .eq(Doctor::getUserId, user.getId()));
                
        if (doctor == null) {
            log.error("医生信息不存在, userId: {}", user.getId());
            return Result.error("医生信息不存在");
        }

        // 获取科室信息
        Department department = departmentService.getById(doctor.getDepartmentId());
        String departmentName = department != null ? department.getName() : null;

        // 构建返回数据
        DoctorProfileDTO profile = DoctorProfileDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .title(doctor.getTitle())
                .departmentId(doctor.getDepartmentId())
                .departmentName(departmentName)
                .specialty(doctor.getSpecialty())
                .introduction(doctor.getIntroduction())
                .avatar(doctor.getAvatar())
                .phone(doctor.getPhone())
                .consultationCount(doctor.getConsultCount())
                .rating(doctor.getRating().doubleValue())
                .status(doctor.getStatus() == 1 ? "ACTIVE" : "INACTIVE")
                .createTime(doctor.getCreateTime())
                .updateTime(doctor.getUpdateTime())
                .build();

        log.info("获取医生个人信息成功, doctorId: {}", doctor.getId());
        return Result.ok(profile);
    }

    @Operation(summary = "更新个人信息")
    @PutMapping("/profile")
    public Result<DoctorProfileDTO> updateProfile(@RequestBody DoctorProfileDTO profile) {
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("更新医生个人信息, username: {}", username);
        
        // 先获取用户信息
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
                
        if (user == null) {
            log.error("用户信息不存在, username: {}", username);
            return Result.error("用户信息不存在");
        }
        
        // 从数据库获取医生信息
        Doctor doctor = doctorService.getOne(new LambdaQueryWrapper<Doctor>()
                .eq(Doctor::getUserId, user.getId()));
                
        if (doctor == null) {
            log.error("医生信息不存在, userId: {}", user.getId());
            return Result.error("医生信息不存在");
        }

        // 更新医生信息
        doctor.setName(profile.getName());
        doctor.setTitle(profile.getTitle());
        doctor.setSpecialty(profile.getSpecialty());
        doctor.setIntroduction(profile.getIntroduction());
        doctor.setPhone(profile.getPhone());
        doctor.setUpdateTime(LocalDateTime.now());
        
        doctorService.updateById(doctor);
        log.info("更新医生信息成功, doctorId: {}", doctor.getId());

        // 获取科室信息
        Department department = departmentService.getById(doctor.getDepartmentId());
        String departmentName = department != null ? department.getName() : null;

        // 返回更新后的完整信息
        DoctorProfileDTO updatedProfile = DoctorProfileDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .title(doctor.getTitle())
                .departmentId(doctor.getDepartmentId())
                .departmentName(departmentName)
                .specialty(doctor.getSpecialty())
                .introduction(doctor.getIntroduction())
                .avatar(doctor.getAvatar())
                .phone(doctor.getPhone())
                .consultationCount(doctor.getConsultCount())
                .rating(doctor.getRating().doubleValue())
                .status(doctor.getStatus() == 1 ? "ACTIVE" : "INACTIVE")
                .createTime(doctor.getCreateTime())
                .updateTime(doctor.getUpdateTime())
                .build();

        return Result.ok(updatedProfile);
    }

    @Operation(summary = "更新头像")
    @PostMapping("/avatar")
    public Result<String> updateAvatar(@RequestParam("file") MultipartFile file) {
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // 先获取用户信息
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
                
        if (user == null) {
            return Result.error("用户信息不存在");
        }
        
        // 从数据库获取医生信息
        Doctor doctor = doctorService.getOne(new LambdaQueryWrapper<Doctor>()
                .eq(Doctor::getUserId, user.getId()));
                
        if (doctor == null) {
            return Result.error("医生信息不存在");
        }

        // 上传头像文件
        String avatarUrl = fileService.uploadFile(file, "AVATAR");

        // 更新头像
        doctor.setAvatar(avatarUrl);
        doctor.setUpdateTime(LocalDateTime.now());
        doctorService.updateById(doctor);

        return Result.ok(avatarUrl);
    }

    @Operation(summary = "修改密码")
    @PostMapping("/password")
    public Result<Boolean> updatePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        
        if (oldPassword == null || newPassword == null) {
            return Result.error("密码参数不能为空");
        }
        
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("修改密码, username: {}", username);
        
        // 获取用户信息
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
                
        if (user == null) {
            log.error("用户信息不存在, username: {}", username);
            return Result.error("用户信息不存在");
        }
        
        log.info("当前密码: {}", user.getPassword());
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.error("旧密码错误, username: {}", username);
            return Result.error("旧密码错误");
        }
        
        // 加密新密码
        String encodedPassword = passwordEncoder.encode(newPassword);
        log.info("新密码加密后: {}", encodedPassword);
        
        // 更新密码
        user.setPassword(encodedPassword);
        user.setUpdateTime(LocalDateTime.now());
        boolean updated = userService.updateById(user);
        
        if (!updated) {
            log.error("密码更新失败, username: {}", username);
            return Result.error("密码更新失败");
        }
        
        log.info("密码修改成功, username: {}", username);
        return Result.ok(true);
    }
} 