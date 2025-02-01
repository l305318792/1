package com.yunchuan.medical.dto;

import lombok.Data;

/**
 * 内容
 */
@Data
public class ContentDTO {
    
    /**
     * ID
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private String status;
} 