package com.yunchuan.medical.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 密码工具类
 */
@Component
public class PasswordUtil {
    private static final Logger log = LoggerFactory.getLogger(PasswordUtil.class);
    private final PasswordEncoder passwordEncoder;

    public PasswordUtil(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 生成加密密码
     */
    public String encode(String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        log.info("原始密码: {}, 加密后: {}", rawPassword, encodedPassword);
        return encodedPassword;
    }

    /**
     * 验证密码
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        log.info("密码验证结果: {}", matches);
        return matches;
    }

    /**
     * 生成测试密码
     */
    public String generateTestPassword(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        log.info("测试密码生成 - 原始密码: {}, 加密后: {}", rawPassword, encodedPassword);
        return encodedPassword;
    }

    public static void main(String[] args) {
        // 使用BCryptPasswordEncoder直接测试
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String encodedPassword = encoder.encode(password);
        System.out.println("原始密码: " + password);
        System.out.println("加密密码: " + encodedPassword);
        System.out.println("验证结果: " + encoder.matches(password, encodedPassword));
    }
} 