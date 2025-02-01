package com.yunchuan.medical.dto;

import lombok.Data;

/**
 * 资讯
 */
@Data
public class NewsDTO {
    
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
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private String tag;

    /**
     * 状态
     */
    private String status;
} 