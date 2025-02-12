package com.yunchuan.medical.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yunchuan.medical.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.cache.annotation.Cacheable;
import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;

/**
 * AI服务实现类 - 使用本地Ollama模型
 */
@Service
public class AiServiceImpl implements AiService {

    private static final Logger log = LoggerFactory.getLogger(AiServiceImpl.class);
    
    @Value("${ai.model:deepseek-r1}")
    private String model;
    
    @Value("${ai.api-url:http://localhost:11434/api/generate}")
    private String apiUrl;
    
    private final RestTemplate restTemplate;
    
    // 使用信号量限制并发请求数
    private final Semaphore requestSemaphore = new Semaphore(3); // 最多3个并发请求
    
    // 重试相关配置
    private static final int MAX_RETRIES = 2;
    private static final long RETRY_DELAY_MS = 1000; // 1秒后重试
    
    // 使用LRU缓存存储最近的问答
    private static final int CACHE_SIZE = 100;
    private static final Map<String, CachedResponse> responseCache = Collections.synchronizedMap(
        new LinkedHashMap<String, CachedResponse>(CACHE_SIZE + 1, .75F, true) {
            protected boolean removeEldestEntry(Map.Entry<String, CachedResponse> eldest) {
                return size() > CACHE_SIZE;
            }
        }
    );
    
    // 缓存响应的包装类
    private static class CachedResponse {
        final String response;
        final long timestamp;
        
        CachedResponse(String response) {
            this.response = response;
            this.timestamp = System.currentTimeMillis();
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > TimeUnit.HOURS.toMillis(1);
        }
    }
    
    // 医疗问诊提示词模板
    private static final String MEDICAL_PROMPT_TEMPLATE = 
        "你是医生。病人说：%s\n" +
        "请按以下格式简短回复：\n" +
        "【初步诊断】睡眠状况评估\n" +
        "【建议】具体改善建议\n" +
        "【注意事项】需要注意的健康问题";
    
    private static final String GENERAL_PROMPT_TEMPLATE = 
        "用户说：%s\n" +
        "请给出有意义的回应。要求：\n" +
        "1. 不要重复用户的话\n" +
        "2. 给出合适的建议\n" +
        "3. 回答要简短，不超过20字";
    
    public AiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        // 预热模型
        warmupModel();
    }

    private void warmupModel() {
        log.info("开始预热AI模型...");
        try {
            String warmupPrompt = "你好";
            generateContentInternal(warmupPrompt, 0);
            log.info("AI模型预热完成");
        } catch (Exception e) {
            log.warn("AI模型预热失败，但不影响正常使用: {}", e.getMessage());
        }
    }

    private String generateContentInternal(String prompt, int retryCount) {
        boolean permitAcquired = false;
        try {
            // 尝试获取信号量，最多等待2秒
            permitAcquired = requestSemaphore.tryAcquire(2, TimeUnit.SECONDS);
            if (!permitAcquired) {
                return "系统繁忙，请稍后重试。";
            }

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("prompt", prompt);
            requestBody.put("stream", false);
            requestBody.put("temperature", 0.3);
            requestBody.put("top_p", 0.2);
            requestBody.put("max_tokens", 300);
            
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // 设置超时时间
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(3000);
            factory.setReadTimeout(7000);
            RestTemplate timeoutTemplate = new RestTemplate(factory);
            
            // 发送请求
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = timeoutTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                request,
                String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JSONObject jsonResponse = JSON.parseObject(response.getBody());
                return jsonResponse.getString("response");
            }
            
            throw new RuntimeException("AI服务响应异常: " + response.getStatusCode());
            
        } catch (Exception e) {
            log.error("AI请求失败 (重试次数: {}): {}", retryCount, e.getMessage());
            
            if (retryCount < MAX_RETRIES) {
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                    return generateContentInternal(prompt, retryCount + 1);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            
            throw new RuntimeException("AI服务调用失败", e);
        } finally {
            if (permitAcquired) {
                requestSemaphore.release();
            }
        }
    }

    @Override
    public String generateContent(String prompt) {
        try {
            log.info("开始生成AI内容，提示词：{}", prompt);
            
            if (prompt == null || prompt.trim().length() < 2) {
                return "请详细描述您的问题。";
            }
            
            // 检查缓存
            String cacheKey = prompt.trim().toLowerCase();
            CachedResponse cached = responseCache.get(cacheKey);
            if (cached != null && !cached.isExpired()) {
                log.info("命中缓存响应");
                return cached.response;
            }
            
            // 判断是否是医疗问题
            boolean isMedical = isMedicalQuestion(prompt);
            String finalPrompt = isMedical ? 
                String.format(MEDICAL_PROMPT_TEMPLATE, prompt) :
                String.format(GENERAL_PROMPT_TEMPLATE, prompt);
            
            // 调用AI服务并处理响应
            String content = generateContentInternal(finalPrompt, 0);
            
            if (content != null && !content.isEmpty()) {
                // 清理响应内容
                content = content.replaceAll("(?s)<think>.*?</think>", "")
                               .replaceAll("让我.*?。", "")
                               .replaceAll("我理解.*?。", "")
                               .replaceAll("首先.*?。", "")
                               .replaceAll("好的.*?。", "")
                               .replaceAll("这样.*?。", "")
                               .replaceAll("对于.*?。", "")
                               .replaceAll("您说.*?。", "")
                               .replaceAll("\\n{3,}", "\n\n")
                               .trim();
                
                // 如果内容与输入完全相同，返回默认回复
                if (content.trim().equalsIgnoreCase(prompt.trim())) {
                    content = isMedical ? 
                        "【初步诊断】需要改善作息\n\n【建议】尽量保持规律作息\n\n【注意事项】长期熬夜可能影响健康" :
                        "建议您调整作息，早点休息。";
                }
                
                // 缓存响应
                responseCache.put(cacheKey, new CachedResponse(content));
                return content;
            }
            
            return "抱歉，请重新描述您的问题。";
            
        } catch (Exception e) {
            log.error("AI服务异常: {}", e.getMessage());
            return "抱歉，请稍后再试。";
        }
    }

    private boolean isMedicalQuestion(String question) {
        String[] medicalKeywords = {
            "头痛", "头晕", "恶心", "呕吐", "发烧", "咳嗽", "感冒", "发热",
            "胸痛", "腹痛", "疼痛", "症状", "病", "医", "药", "治疗",
            "检查", "手术", "住院", "门诊", "化验", "患者", "病人",
            "不适", "难受", "疼", "痒", "肿", "麻", "痛", "心慌",
            "心跳", "呼吸", "血压", "血糖", "体温", "脉搏", "消化",
            "睡眠", "饮食", "大便", "小便", "体重", "食欲", "疲劳",
            "乏力", "虚弱", "眩晕", "晕厥", "过敏", "感染", "饿",
            "胃", "肠", "消化", "吃饭", "饭后", "饥饿"
        };
        
        return Arrays.stream(medicalKeywords)
                    .anyMatch(question::contains);
    }
}