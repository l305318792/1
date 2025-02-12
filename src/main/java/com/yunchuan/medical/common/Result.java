package com.yunchuan.medical.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 通用返回结果
 * @author yunchuan
 * @since 1.0.0
 */
@Data
public class Result<T> {
    
    private static final Integer SUCCESS = 200;
    private static final Integer ERROR = 500;
    private static final Integer BAD_REQUEST = 400;
    
    private Integer code;
    private String message;
    private T data;
    
    public Result() {
    }
    
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }
    
    public static <T> Result<T> ok() {
        return new Result<>(200, "success", null);
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
    
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
    
    // Getters and Setters
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
} 