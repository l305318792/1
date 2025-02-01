package com.yunchuan.medical.common;

import lombok.Getter;

@Getter
public enum StatusEnum {
    
    ENABLE("1", "启用"),
    DISABLE("0", "禁用"),
    
    PENDING("0", "待处理"),
    PROCESSING("1", "处理中"),
    COMPLETED("2", "已完成"),
    CANCELLED("3", "已取消");
    
    private final String code;
    private final String message;
    
    StatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
} 