package com.yunchuan.medical.dto;

import jakarta.validation.constraints.NotBlank;

public class ConsultMessageFormDTO {
    
    @NotBlank(message = "咨询内容不能为空")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
} 