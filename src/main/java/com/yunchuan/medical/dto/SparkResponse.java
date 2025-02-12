package com.yunchuan.medical.dto;

import lombok.Data;
import java.util.List;

/**
 * 星火大模型响应实体类
 */
@Data
public class SparkResponse {
    private Header header;
    private Payload payload;
    
    @Data
    public static class Header {
        private int code;
        private String message;
        private String sid;
        private int status;
    }
    
    @Data
    public static class Payload {
        private List<Choice> choices;
        
        @Data
        public static class Choice {
            private List<Text> text;
            
            @Data
            public static class Text {
                private String role;
                private String content;
            }
        }
    }
} 