package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.service.AiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

/**
 * AI服务实现
 */
@Service
public class AiServiceImpl implements AiService {

    @Value("${ai.api-key}")
    private String apiKey;
    
    @Value("${ai.api-url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate;
    
    public AiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getAiReply(String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", new Object[]{
            Map.of(
                "role", "system",
                "content", "你是一个专业的医疗咨询助手,请用专业且友善的语气回答用户的医疗相关问题。"
            ),
            Map.of(
                "role", "user",
                "content", content
            )
        });

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        
        try {
            Map<String, Object> response = restTemplate.postForObject(apiUrl, request, Map.class);
            return extractReplyFromResponse(response);
        } catch (Exception e) {
            return "抱歉,AI助手暂时无法回答,请稍后再试。";
        }
    }
    
    private String extractReplyFromResponse(Map<String, Object> response) {
        try {
            return ((Map)((Map)((Object[])response.get("choices"))[0]).get("message")).get("content").toString();
        } catch (Exception e) {
            return "抱歉,AI助手暂时无法回答,请稍后再试。";
        }
    }
} 