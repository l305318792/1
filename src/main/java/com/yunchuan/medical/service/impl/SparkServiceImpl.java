package com.yunchuan.medical.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunchuan.medical.config.SparkConfig;
import com.yunchuan.medical.dto.SparkRequest;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.websocket.SparkWebSocketClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.net.URLEncoder;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;
import java.util.Arrays;

/**
 * 星火大模型服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SparkServiceImpl {
    private final SparkConfig config;
    private final ObjectMapper objectMapper;
    
    /**
     * 发送消息
     */
    public String chat(String message) {
        try {
            // 1. 创建鉴权URL
            String authUrl = createAuthUrl();
            
            // 2. 创建WebSocket客户端
            SparkWebSocketClient client = new SparkWebSocketClient(
                authUrl,
                content -> log.debug("收到消息: {}", content)
            );
            
            // 3. 建立连接
            client.connectBlocking();
            
            // 4. 发送消息
            String requestJson = buildRequestJson(message);
            client.send(requestJson);
            
            // 5. 等待响应
            String response = client.getResponse();
            
            // 6. 关闭连接
            client.close();
            
            return response;
            
        } catch (Exception e) {
            log.error("调用星火大模型失败", e);
            throw new BusinessException("AI服务暂时不可用");
        }
    }
    
    /**
     * 创建鉴权URL
     */
    private String createAuthUrl() {
        try {
            // 将wss://转换为https://以便解析
            String apiUrl = config.getApiUrl().replace("wss://", "https://");
            URL url = new URL(apiUrl);
            String host = url.getHost();
            String path = url.getPath();
            
            // 使用固定的日期进行测试
            String date = "Sun, 9 Feb 2025 10:00:00 GMT";
            
            // 拼接签名字符串 - 严格按照顺序：host、date、request-line
            StringBuilder signatureOrigin = new StringBuilder();
            signatureOrigin.append("host: ").append(host).append("\n");
            signatureOrigin.append("date: ").append(date).append("\n");
            signatureOrigin.append("GET ").append(path).append(" HTTP/1.1");
            
            log.info("原始签名字符串:\n{}", signatureOrigin);
            
            // 使用apiSecret对签名字符串进行hmac-sha256加密
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                Base64.getDecoder().decode(config.getApiSecret()),
                "HmacSHA256"
            ));
            
            byte[] signData = mac.doFinal(signatureOrigin.toString().getBytes("UTF-8"));
            String signature = Base64.getEncoder().encodeToString(signData);
            log.info("生成的签名: {}", signature);
            
            // 拼接authorization字符串
            StringBuilder authorizationOrigin = new StringBuilder();
            authorizationOrigin.append("api_key='").append(config.getApiKey()).append("', ");
            authorizationOrigin.append("algorithm='hmac-sha256', ");
            authorizationOrigin.append("headers='host date request-line', ");
            authorizationOrigin.append("signature='").append(signature).append("'");
            
            log.info("authorization原始字符串: {}", authorizationOrigin);
            
            String authorization = Base64.getEncoder().encodeToString(authorizationOrigin.toString().getBytes("UTF-8"));
            log.info("Base64编码后的authorization: {}", authorization);
            
            // 拼接最终的URL
            String authUrl = config.getApiUrl() +
                   "?authorization=" + URLEncoder.encode(authorization, "UTF-8") +
                   "&date=" + URLEncoder.encode(date, "UTF-8") +
                   "&host=" + URLEncoder.encode(host, "UTF-8");
                   
            log.info("最终的鉴权URL: {}", authUrl);
            return authUrl;
                   
        } catch (Exception e) {
            log.error("创建鉴权URL失败", e);
            throw new BusinessException("创建鉴权URL失败: " + e.getMessage());
        }
    }
    
    /**
     * 构建请求JSON
     */
    private String buildRequestJson(String message) throws JsonProcessingException {
        // 构建system角色的引导消息
        SparkRequest.Payload.Message.Text systemMsg = SparkRequest.Payload.Message.Text.builder()
            .role("system")
            .content("你现在是一位经验丰富的医生，请用专业、友善的方式回答患者的问题。在回答时要注意：1. 先表达对患者的关心 2. 详细解释病情 3. 给出具体的建议 4. 提醒需要注意的事项。")
            .build();
            
        // 构建用户消息
        SparkRequest.Payload.Message.Text userMsg = SparkRequest.Payload.Message.Text.builder()
            .role("user")
            .content(message)
            .build();
            
        // 构建完整请求
        SparkRequest request = SparkRequest.builder()
            .header(SparkRequest.Header.builder()
                .appId(config.getAppId())
                .uid(UUID.randomUUID().toString())
                .build())
            .parameter(SparkRequest.Parameter.builder()
                .chat(SparkRequest.Parameter.Chat.builder()
                    .domain("general")
                    .temperature(0.5)
                    .maxTokens(2048)
                    .build())
                .build())
            .payload(SparkRequest.Payload.builder()
                .message(SparkRequest.Payload.Message.builder()
                    .text(Arrays.asList(systemMsg, userMsg))
                    .build())
                .build())
            .build();
            
        String json = objectMapper.writeValueAsString(request);
        log.debug("请求JSON: {}", json);
        return json;
    }
} 