package com.yunchuan.medical.dto;

import lombok.Data;
import lombok.Builder;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 星火大模型请求实体类
 */
@Data
@Builder
public class SparkRequest {
    private Header header;
    private Parameter parameter;
    private Payload payload;
    
    @Data
    @Builder
    public static class Header {
        @JsonProperty("app_id")
        private String appId;
        private String uid;
    }
    
    @Data
    @Builder
    public static class Parameter {
        private Chat chat;
        
        @Data
        @Builder
        public static class Chat {
            @Builder.Default
            private String domain = "generalv4.0";
            @Builder.Default
            private Double temperature = 0.5;
            @JsonProperty("max_tokens")
            @Builder.Default
            private Integer maxTokens = 2048;
        }
    }
    
    @Data
    @Builder
    public static class Payload {
        private Message message;
        
        @Data
        @Builder
        public static class Message {
            private List<Text> text;
            
            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Text {
                private String role;
                private String content;
            }
        }
    }
} 