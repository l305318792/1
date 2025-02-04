package com.yunchuan.medical.utils;

import com.yunchuan.medical.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import com.yunchuan.medical.security.CustomUserDetails;

/**
 * 安全工具类
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户ID
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal).getUser().getId();
            }
        }
        throw new RuntimeException("用户未登录");
    }

    /**
     * 获取当前登录用户
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUser();
        }
        throw new RuntimeException("用户未登录");
    }
} 