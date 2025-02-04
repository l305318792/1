package com.yunchuan.medical.config;

import com.yunchuan.medical.security.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 安全配置类
 * @author yunchuan
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("配置安全过滤链");
        http
            .csrf(csrf -> {
                csrf.disable();
                log.info("CSRF 保护已禁用");
            })
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                log.info("会话管理策略设置为无状态");
            })
            .authorizeHttpRequests(auth -> {
                auth
                    .requestMatchers("/auth/**", "/common/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/admin/**").hasAuthority("ADMIN")
                    .requestMatchers("/doctor/list", "/doctor/*/schedule").hasAnyAuthority("DOCTOR", "PATIENT", "ADMIN")
                    .requestMatchers("/doctor/**").hasAuthority("DOCTOR")
                    .requestMatchers("/patient/**").hasAuthority("PATIENT")
                    .requestMatchers("/appointment/**").hasAnyAuthority("PATIENT", "ADMIN")
                    .anyRequest().authenticated();
                log.info("请求授权规则已配置");
            })
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> 
                headers.frameOptions(frame -> frame.disable())
            );
        log.info("安全配置完成");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("创建密码编码器");
        return new BCryptPasswordEncoder();
    }
} 