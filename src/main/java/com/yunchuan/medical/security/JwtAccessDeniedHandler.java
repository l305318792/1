package com.yunchuan.medical.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunchuan.medical.common.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JWT访问拒绝处理器
 * 处理无权限的请求
 * @author yunchuan
 * @since 1.0.0
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    
    private final ObjectMapper objectMapper;
    
    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        Result<?> result = Result.error(HttpServletResponse.SC_FORBIDDEN, "无权限访问该资源");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
} 