package com.yunchuan.medical.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 数据备份数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackupDTO {
    /**
     * 备份ID
     */
    private String id;
    
    /**
     * 备份文件名
     */
    private String fileName;
    
    /**
     * 备份文件大小(字节)
     */
    private Long fileSize;
    
    /**
     * 备份类型(MANUAL-手动备份 AUTO-自动备份)
     */
    private String type;
    
    /**
     * 备份描述
     */
    private String description;
    
    /**
     * 备份状态(SUCCESS-成功 FAIL-失败)
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