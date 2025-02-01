package com.yunchuan.medical.util;

import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 * @author yunchuan
 * @since 1.0.0
 */
public class SecurityUtil {
    
    /**
     * 获取当前用户ID
     */
    public static String getCurrentUserId() {
        // TODO: 实际项目中应该从SecurityContext中获取
        return "user_001";
    }
    
    /**
     * 获取当前医生ID
     */
    public static String getCurrentDoctorId() {
        User user = getCurrentUser();
        if (!"DOCTOR".equals(user.getRole())) {
            throw new IllegalStateException("当前用户不是医生");
        }
        return user.getId();
    }
    
    /**
     * 获取当前用户
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("用户未登录");
        }
        String name = authentication.getName();
        // 处理 Bearer_ 前缀
        if (name.startsWith("Bearer_")) {
            name = name.substring(7);
        }
        if ("admin".equals(name)) {
            User user = new User();
            user.setId("1");
            user.setUsername("admin");
            user.setName("管理员");
            user.setRole("ADMIN");
            return user;
        }
        throw new IllegalStateException("无效的用户");
    }
} 