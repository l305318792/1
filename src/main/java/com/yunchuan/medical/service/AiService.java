package com.yunchuan.medical.service;

/**
 * AI服务接口
 */
public interface AiService {
    
    /**
     * 生成内容
     * @param prompt 提示词
     * @return 生成的内容
     */
    String generateContent(String prompt);
}