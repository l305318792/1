package com.yunchuan.medical.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Ollama AI配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ollama")
public class AIConfig {
    /**
     * Ollama服务地址
     */
    private String apiUrl = "http://localhost:11434/api/chat";
    
    /**
     * 模型名称
     */
    private String model = "deepseek-r1";
    
    /**
     * 温度参数
     */
    private Double temperature = 0.7;
    
    /**
     * 最大生成长度
     */
    private Integer maxLength = 2048;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 