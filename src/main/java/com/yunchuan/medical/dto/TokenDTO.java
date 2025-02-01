package com.yunchuan.medical.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Token DTO
 * @author yunchuan
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    
    /**
     * 访问令牌
     */
    private String token;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户姓名
     */
    private String name;
    
    /**
     * 角色
     */
    private String role;
    
    /**
     * 医生ID(如果是医生用户)
     */
    private String doctorId;
} 