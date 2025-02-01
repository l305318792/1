package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.dto.LoginDTO;
import com.yunchuan.medical.dto.RegisterDTO;
import com.yunchuan.medical.dto.TokenDTO;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.mapper.UserMapper;
import com.yunchuan.medical.service.AuthService;
import com.yunchuan.medical.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthServiceImpl(UserMapper userMapper,
                         PasswordEncoder passwordEncoder,
                         JwtUtil jwtUtil,
                         UserDetailsService userDetailsService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public TokenDTO register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 创建新用户
        User user = User.builder()
            .username(registerDTO.getUsername())
            .password(passwordEncoder.encode(registerDTO.getPassword()))
            .name(registerDTO.getName())
            .phone(registerDTO.getPhone())
            .role("USER")
            .status("normal")
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();

        // 保存用户
        userMapper.insert(user);

        // 生成token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        
        return TokenDTO.builder()
            .token(token)
            .userId(user.getId())
            .username(user.getUsername())
            .name(user.getName())
            .role(user.getRole())
            .build();
    }

    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
            if (!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
                throw new BusinessException("密码错误");
            }

            String token = jwtUtil.generateToken(userDetails);
            User user = userMapper.selectByUsername(loginDTO.getUsername());
            
            return TokenDTO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            throw new BusinessException("登录失败");
        }
    }

    @Override
    public void logout(String token) {
        // 可以在这里实现token黑名单等逻辑
        log.info("用户退出登录");
    }
} 