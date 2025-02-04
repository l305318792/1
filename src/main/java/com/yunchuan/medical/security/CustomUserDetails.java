package com.yunchuan.medical.security;

import com.yunchuan.medical.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 自定义UserDetails实现
 * @author yunchuan
 * @since 1.0.0
 */
public class CustomUserDetails implements UserDetails {
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetails.class);

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
        log.info("创建CustomUserDetails，用户信息：{}", user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole();
        log.info("获取用户权限，原始角色：{}", role);
        
        // 移除可能存在的 ROLE_ 前缀
        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }
        
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        log.info("设置用户权限：{}", authority);
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        log.info("获取用户密码（已加密）");
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        log.info("获取用户名：{}", user.getUsername());
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        log.info("检查账户是否未过期");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        log.info("检查账户是否未锁定");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        log.info("检查凭证是否未过期");
        return true;
    }

    @Override
    public boolean isEnabled() {
        log.info("检查账户是否启用，状态：{}", user.getStatus());
        return "ENABLED".equals(user.getStatus()) || "ACTIVE".equals(user.getStatus());
    }

    public User getUser() {
        return user;
    }
} 