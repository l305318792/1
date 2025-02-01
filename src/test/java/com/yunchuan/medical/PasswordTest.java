package com.yunchuan.medical;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PasswordTest {
    
    @Test
    public void testPassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 1. 生成新的加密密码
        String rawPassword = "admin123";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("新生成的密码: " + encodedPassword);
        
        // 2. 验证数据库中的密码
        String dbPassword = "$2a$10$x6xqFZ4JOT0vxKtAQESbPuR/yRlkrTqUFE5LBEGQtB7YtFYpPV3Uu";
        boolean matches = encoder.matches(rawPassword, dbPassword);
        System.out.println("验证结果: " + matches);
    }
} 