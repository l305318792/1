package com.yunchuan.medical.service;

/**
 * AI服务接口
 */
public interface AiService {
    
    /**
     * 获取AI回复
     */
    String getAiReply(String content);
} 