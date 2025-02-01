package com.yunchuan.medical.common;

import lombok.Data;

@Data
public class PageRequest {
    
    private Integer page = 1;
    private Integer size = 10;
    
    public Integer getOffset() {
        return (page - 1) * size;
    }
} 