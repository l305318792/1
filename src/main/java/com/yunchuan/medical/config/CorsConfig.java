package com.yunchuan.medical.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS配置类
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许的域名
        config.addAllowedOriginPattern("*");
        // 允许的头信息
        config.addAllowedHeader("*");
        // 允许的HTTP方法
        config.addAllowedMethod("*");
        // 允许发送Cookie
        config.setAllowCredentials(true);
        // 暴露的头信息
        config.addExposedHeader("Authorization");
        // 预检请求的有效期，单位为秒
        config.setMaxAge(3600L);
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
} 