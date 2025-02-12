package com.yunchuan.medical.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 星火大模型配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "xunfei.spark")
public class SparkConfig {
    /**
     * 应用ID
     */
    private String appId = "6485bca0";
    
    /**
     * API密钥
     */
    private String apiSecret = "YTllNTlmNmVlZDYzYWMzNWViMzU0Yzgz";
    
    /**
     * API Key
     */
    private String apiKey = "2f1d9a9ed6299c7003e58c4ebe30ad9d";
    
    /**
     * 服务接口地址
     */
    private String apiUrl = "wss://spark-api.xf-yun.com/v4.0/chat";
} 