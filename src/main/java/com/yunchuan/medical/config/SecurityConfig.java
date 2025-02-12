package com.yunchuan.medical.config;

import com.yunchuan.medical.security.JwtAuthenticationFilter;
import com.yunchuan.medical.security.JwtAuthenticationEntryPoint;
import com.yunchuan.medical.security.JwtAccessDeniedHandler;
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
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("开始配置安全过滤链");
        http
            .cors(cors -> cors.configure(http))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                try {
                    auth
                        .requestMatchers("/auth/**", "/common/**", "/files/**", "/api/ai/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/video.html", "/webjars/**", "/ws-video/**", "/topic/**", "/app/**", "/favicon.ico").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/doctor/list", "/doctor/*/schedule").permitAll()
                        .requestMatchers("/doctor/**").hasAuthority("DOCTOR")
                        .requestMatchers("/patient/medical-records/**").hasAuthority("PATIENT")
                        .requestMatchers("/patient/**").hasAuthority("PATIENT")
                        .requestMatchers("/payment/**").hasAnyAuthority("PATIENT", "ADMIN", "DOCTOR")
                        .requestMatchers("/appointment/**").hasAnyAuthority("PATIENT", "ADMIN")
                        .anyRequest().authenticated();
                    log.info("请求授权配置完成");
                } catch (Exception e) {
                    log.error("配置请求授权时发生错误", e);
                    throw e;
                }
            })
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .exceptionHandling(ex -> {
                ex.authenticationEntryPoint((request, response, authException) -> {
                    log.error("认证失败: {}", authException.getMessage());
                    jwtAuthenticationEntryPoint.commence(request, response, authException);
                });
                ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    log.error("访问被拒绝: {}", accessDeniedException.getMessage());
                    jwtAccessDeniedHandler.handle(request, response, accessDeniedException);
                });
            });
            
        log.info("安全配置完成");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 