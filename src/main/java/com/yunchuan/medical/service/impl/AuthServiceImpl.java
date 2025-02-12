package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.dto.LoginDTO;
import com.yunchuan.medical.dto.RegisterDTO;
import com.yunchuan.medical.dto.TokenDTO;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.mapper.UserMapper;
import com.yunchuan.medical.service.AuthService;
import com.yunchuan.medical.util.JwtUtil;
import com.yunchuan.medical.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final StringRedisTemplate redisTemplate;

    public AuthServiceImpl(UserMapper userMapper,
                         PasswordEncoder passwordEncoder,
                         JwtUtil jwtUtil,
                         UserDetailsService userDetailsService,
                         StringRedisTemplate redisTemplate) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
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
            log.info("开始登录，用户名: {}", loginDTO.getUsername());
            
            // 使用UserDetailsService加载用户
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
            log.info("加载到的用户信息: {}", userDetails);
            
            // 验证密码
            log.info("开始验证密码 - 输入密码: {}, 存储的密码: {}", loginDTO.getPassword(), userDetails.getPassword());
            if (!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
                log.error("密码错误，用户名: {}", loginDTO.getUsername());
                throw new BusinessException("密码错误");
            }
            log.info("密码验证成功");
            
            // 检查用户状态
            if (!userDetails.isEnabled()) {
                log.error("用户状态异常: {}", loginDTO.getUsername());
                throw new BusinessException("用户状态异常");
            }

            // 生成token
            String token = jwtUtil.generateToken(userDetails);
            log.info("Token生成成功: {}", token);
            
            // 从数据库获取完整用户信息
            User user = userMapper.selectByUsername(loginDTO.getUsername());
            log.info("获取到的完整用户信息: {}", user);
            
            return TokenDTO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
                
        } catch (BusinessException e) {
            log.error("业务异常：", e);
            throw e;
        } catch (Exception e) {
            log.error("登录异常", e);
            e.printStackTrace();
            throw new BusinessException("登录失败");
        }
    }

    /**
     * 验证token是否有效
     */
    private boolean isValidToken(String token) {
        try {
            String username = jwtUtil.getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtUtil.validateToken(token, userDetails);
        } catch (Exception e) {
            log.error("Token验证失败", e);
            return false;
        }
    }

    @Override
    public void logout(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 验证token是否有效
            if (!isValidToken(token)) {
                log.warn("无效的token");
                return;
            }
            
            // 获取token的过期时间
            long expirationTime = jwtUtil.getExpirationDateFromToken(token).getTime();
            long currentTime = System.currentTimeMillis();
            long ttl = expirationTime - currentTime;
            
            if (ttl > 0) {
                // 将token加入黑名单，过期时间与token剩余有效期一致
                String blacklistKey = TOKEN_BLACKLIST_PREFIX + token;
                redisTemplate.opsForValue().set(blacklistKey, "1", ttl, TimeUnit.MILLISECONDS);
                log.info("Token已加入黑名单，将在{}毫秒后过期", ttl);
            }
            
            log.info("用户成功退出登录");
        } catch (Exception e) {
            log.error("退出登录时发生错误", e);
            throw new BusinessException("退出登录失败");
        }
    }
} 