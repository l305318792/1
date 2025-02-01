package com.yunchuan.medical.common;

import lombok.Data;

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
    
    private Result() {}
    
    private Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> Result<T> ok() {
        return new Result<>(SUCCESS, "操作成功");
    }
    
    public static <T> Result<T> ok(T data) {
        return new Result<>(SUCCESS, "操作成功", data);
    }
    
    public static <T> Result<T> ok(String message) {
        return new Result<>(SUCCESS, message);
    }
    
    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(SUCCESS, message, data);
    }
    
    public static <T> Result<T> error() {
        return new Result<>(ERROR, "系统异常，请联系系统管理员");
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(BAD_REQUEST, message);
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }
    
    public static <T> Result<T> error(String message, T data) {
        return new Result<>(BAD_REQUEST, message, data);
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