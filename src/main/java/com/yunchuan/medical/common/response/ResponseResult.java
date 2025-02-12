package com.yunchuan.medical.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 统一响应结果
 */
@Data
@Schema(description = "统一响应结果")
public class ResponseResult<T> {
    
    @Schema(description = "状态码")
    private Integer code;
    
    @Schema(description = "提示信息")
    private String message;
    
    @Schema(description = "数据")
    private T data;
    
    private ResponseResult() {}
    
    private ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(200, "操作成功", null);
    }
    
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "操作成功", data);
    }
    
    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(200, message, data);
    }
    
    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(500, message, null);
    }
    
    public static <T> ResponseResult<T> error(Integer code, String message) {
        return new ResponseResult<>(code, message, null);
    }
} 