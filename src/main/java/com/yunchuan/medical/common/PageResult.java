package com.yunchuan.medical.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    
    private List<T> list;
    private Long total;
    private Integer pages;
    
    public PageResult(List<T> list, Long total, Integer size) {
        this.list = list;
        this.total = total;
        this.pages = (int) Math.ceil((double) total / size);
    }
} 