package com.yunchuan.medical.security;

import com.yunchuan.medical.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JWT认证过滤器
 * 处理token验证和用户认证
 * @author yunchuan
 * @since 1.0.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private static final List<String> PUBLIC_URLS = Arrays.asList(
        "/auth/**",
        "/common/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/video.html",
        "/webjars/**",
        "/ws-video/**",
        "/topic/**",
        "/app/**",
        "/favicon.ico"
    );
    
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final StringRedisTemplate redisTemplate;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, StringRedisTemplate redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
            throws ServletException, IOException {
        try {
            String requestPath = request.getRequestURI();
            
            // 检查是否是公开URL
            if (isPublicUrl(requestPath)) {
                chain.doFilter(request, response);
                return;
            }

            // 获取Authorization header
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("未找到有效的Authorization header");
                chain.doFilter(request, response);
                return;
            }

            // 提取token
            String token = authHeader.substring(7);
            
            // 检查token是否在黑名单中
            String blacklistKey = TOKEN_BLACKLIST_PREFIX + token;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey))) {
                log.warn("Token在黑名单中");
                chain.doFilter(request, response);
                return;
            }

            try {
                String username = jwtUtil.getUsernameFromToken(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("认证成功，用户：{}，权限：{}", username, userDetails.getAuthorities());
                    } else {
                        log.warn("Token验证失败");
                    }
                }
            } catch (ExpiredJwtException e) {
                log.warn("Token已过期: {}", e.getMessage());
            } catch (Exception e) {
                log.error("Token处理异常", e);
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("过滤器异常", e);
            throw e;
        }
    }

    private boolean isPublicUrl(String requestPath) {
        return PUBLIC_URLS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }
} 