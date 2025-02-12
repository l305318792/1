package com.yunchuan.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 文章数据访问层
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    
    /**
     * 获取已发布的文章列表
     */
    @Select("SELECT * FROM article WHERE status = 'PUBLISHED' ORDER BY publish_time DESC")
    List<Article> selectPublishedArticles();
    
    /**
     * 获取AI生成的文章列表
     */
    @Select("SELECT * FROM article WHERE is_ai_generated = true ORDER BY create_time DESC")
    List<Article> selectAiGeneratedArticles();
    
    /**
     * 根据分类获取文章列表
     */
    @Select("SELECT * FROM article WHERE category = #{category} AND status = 'PUBLISHED' ORDER BY publish_time DESC")
    List<Article> selectByCategory(String category);
}