package com.yunchuan.medical.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 资讯标签关联实体类
 */
@Data
@TableName("news_tag")
public class NewsTag {
    
    private String newsId;
    
    private String tagId;
} 