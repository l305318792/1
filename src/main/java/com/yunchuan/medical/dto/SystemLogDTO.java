package com.yunchuan.medical.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统日志数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemLogDTO {
    /**
     * 日志ID
     */
    private String id;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 操作类型(LOGIN-登录 LOGOUT-登出 CREATE-创建 UPDATE-更新 DELETE-删除)
     */
    private String operationType;
    
    /**
     * 操作描述
     */
    private String description;
    
    /**
     * 操作模块
     */
    private String module;
    
    /**
     * IP地址
     */
    private String ip;
    
    /**
     * 操作状态(SUCCESS-成功 FAIL-失败)
     */
    private String status;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 