package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import org.springframework.web.bind.annotation.*;

/**
 * 首页管理
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    /**
     * 获取数据
     */
    @GetMapping
    public Result<Object> getData() {
        return Result.ok(new Object());
    }
} 