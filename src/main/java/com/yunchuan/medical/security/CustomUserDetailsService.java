package com.yunchuan.medical.security;

import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 自定义UserDetailsService实现
 * 用于加载用户信息
 * @author yunchuan
 * @since 1.0.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserMapper userMapper;

    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("开始加载用户信息，用户名: {}", username);
        
        User user = userMapper.selectByUsername(username);
        log.info("从数据库查询到的用户信息: {}", user);
        
        if (user == null) {
            log.error("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 直接返回CustomUserDetails实例
        CustomUserDetails userDetails = new CustomUserDetails(user);
        log.info("创建的CustomUserDetails: {}", userDetails);
        log.info("用户ID: {}", user.getId());
        log.info("用户角色: {}", user.getRole());
        log.info("用户权限: {}", userDetails.getAuthorities());
        log.info("用户状态: enabled={}", userDetails.isEnabled());
        
        return userDetails;
    }
}