package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.LoginDTO;
import com.yunchuan.medical.dto.RegisterDTO;
import com.yunchuan.medical.dto.TokenDTO;

/**
 * 认证服务接口
 */
public interface AuthService {
    /**
     * 用户注册
     */
    TokenDTO register(RegisterDTO registerDTO);

    /**
     * 用户登录
     */
    TokenDTO login(LoginDTO loginDTO);

    /**
     * 退出登录
     */
    void logout(String token);
}