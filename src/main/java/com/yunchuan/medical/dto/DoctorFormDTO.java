package com.yunchuan.medical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 医生表单DTO
 * @author yunchuan
 * @since 1.0.0
 */
@Data
public class DoctorFormDTO {
    private String id;

    @NotBlank(message = "医生姓名不能为空")
    private String name;

    @NotBlank(message = "所属科室不能为空")
    private String departmentId;

    @NotBlank(message = "职称不能为空")
    private String title;

    private String introduction;

    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,16}$", message = "用户名必须是4-16位字母、数字或下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{6,16}$", message = "密码必须是6-16位字母、数字或下划线")
    private String password;
} 