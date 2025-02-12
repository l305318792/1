package com.yunchuan.medical.websocket;

import com.alibaba.fastjson2.JSON;
import com.yunchuan.medical.dto.SparkResponse;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 星火WebSocket客户端
 */
@Slf4j
public class SparkWebSocketClient extends WebSocketClient {
    private final CountDownLatch latch;
    private final StringBuilder responseBuilder;
    private final Consumer<String> messageHandler;
    
    public SparkWebSocketClient(String url, Consumer<String> messageHandler) throws URISyntaxException {
        super(new URI(url));
        this.latch = new CountDownLatch(1);
        this.responseBuilder = new StringBuilder();
        this.messageHandler = messageHandler;
        
        // 添加必要的请求头
        Map<String, String> headers = new HashMap<>();
        String date = "Sun, 9 Feb 2025 10:00:00 GMT";
        headers.put("Date", date);
        headers.put("Host", new URI(url).getHost());
        this.addHeader(headers);
    }
    
    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("WebSocket连接已建立");
    }
    
    @Override
    public void onMessage(String message) {
        try {
            SparkResponse response = JSON.parseObject(message, SparkResponse.class);
            if (response.getHeader().getStatus() == 2) {
                // 所有数据接收完毕
                latch.countDown();
            } else {
                String content = response.getPayload().getChoices().get(0).getText().get(0).getContent();
                responseBuilder.append(content);
                // 实时处理消息
                messageHandler.accept(content);
            }
        } catch (Exception e) {
            log.error("处理消息失败", e);
        }
    }
    
    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("WebSocket连接已关闭: code={}, reason={}", code, reason);
        latch.countDown();
    }
    
    @Override
    public void onError(Exception ex) {
        log.error("WebSocket发生错误", ex);
        latch.countDown();
    }
    
    public String getResponse() throws InterruptedException {
        latch.await(30, TimeUnit.SECONDS);
        return responseBuilder.toString();
    }
    
    private void addHeader(Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.addHeader(entry.getKey(), entry.getValue());
        }
    }
} 