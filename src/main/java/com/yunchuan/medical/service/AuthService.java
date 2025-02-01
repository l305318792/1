package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.LoginDTO;
import com.yunchuan.medical.dto.RegisterDTO;
import com.yunchuan.medical.dto.TokenDTO;

/**
 * 认证服务接口
 * @author yunchuan
 * @since 1.0.0
 */
public interface AuthService {

    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return token信息
     */
    TokenDTO register(RegisterDTO registerDTO);

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return token信息
     */
    TokenDTO login(LoginDTO loginDTO);
    
    /**
     * 退出登录
     * @param token JWT token
     */
    void logout(String token);
} 