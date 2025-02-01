package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.LoginDTO;
import com.yunchuan.medical.dto.RegisterDTO;
import com.yunchuan.medical.dto.TokenDTO;
import com.yunchuan.medical.service.AuthService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 认证管理
 */
@Tag(name = "用户认证")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<TokenDTO> register(@RequestBody RegisterDTO register) {
        return Result.ok(authService.register(register));
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<TokenDTO> login(@RequestBody LoginDTO login) {
        return Result.ok(authService.login(login));
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result<Boolean> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return Result.ok(true);
    }
} 