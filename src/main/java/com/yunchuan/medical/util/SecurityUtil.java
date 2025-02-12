package com.yunchuan.medical.util;

import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 安全工具类
 * @author yunchuan
 * @since 1.0.0
 */
public class SecurityUtil {
    
    /**
     * 获取当前登录用户ID
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                User user = ((CustomUserDetails) principal).getUser();
                if (user != null) {
                    return user.getId();
                }
            }
        }
        throw new BusinessException("用户未登录");
    }
    
    /**
     * 获取当前登录用户名
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
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
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        }
        
        throw new IllegalStateException("无效的用户认证信息");
    }
} 