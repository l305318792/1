package com.yunchuan.medical.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.yunchuan.medical.security.CustomUserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT工具类
 * 用于生成和验证JWT token
 * @author yunchuan
 * @since 1.0.0
 */
@Component
public class JwtUtil {

    private final SecretKey key;
    private final Long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret, 
                  @Value("${jwt.expiration:86400000}") Long expiration) {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = expiration;
    }

    /**
     * 从token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从token中获取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从token中获取指定的claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 获取token中的所有claim
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 检查token是否过期
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 生成token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        // 获取用户角色，不添加ROLE_前缀
        String role = userDetails.getAuthorities().stream()
            .findFirst()
            .map(authority -> authority.getAuthority().replace("ROLE_", ""))
            .orElse("USER");
            
        claims.put("role", role);
        
        // 如果UserDetails是CustomUserDetails，则添加用户ID
        if (userDetails instanceof CustomUserDetails) {
            claims.put("userId", ((CustomUserDetails) userDetails).getUser().getId());
        }
        
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * 生成token的具体实现
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    /**
     * 验证token
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 从token中获取用户ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("userId", String.class);
    }
} 