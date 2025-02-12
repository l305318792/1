package com.yunchuan.medical.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.context.annotation.Bean;

/**
 * WebSocket配置类
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用简单的消息代理，用于点对点和广播消息
        config.enableSimpleBroker("/topic", "/queue");
        // 设置应用程序的目标前缀
        config.setApplicationDestinationPrefixes("/app");
        // 设置用户目标的前缀
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-video")
                // 允许所有来源
                .setAllowedOriginPatterns("*")
                // 启用SockJS后备选项
                .withSockJS()
                // 设置心跳间隔为25秒
                .setHeartbeatTime(25000)
                // 设置客户端断开连接后的重试间隔
                .setDisconnectDelay(5000)
                // 设置WebSocket握手选项
                .setWebSocketEnabled(true)
                // 设置SockJS cookie不需要
                .setSessionCookieNeeded(false);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration
            // 设置消息大小限制为64KB
            .setMessageSizeLimit(64 * 1024)
            // 设置发送超时时间为15秒
            .setSendTimeLimit(15 * 1000)
            // 设置发送缓冲区大小限制为512KB
            .setSendBufferSizeLimit(512 * 1024);
    }

    @Bean
    public DefaultHandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler();
    }
} 