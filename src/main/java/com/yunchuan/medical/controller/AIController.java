package com.yunchuan.medical.controller;

import com.yunchuan.medical.service.AiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yunchuan.medical.common.Result;

/**
 * AI对话控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Api(tags = "AI对话接口")
public class AIController {

    private final AiService aiService;
    
    private static final String WELCOME_MESSAGE = 
        "您好！我是AI智能问诊助手。请详细描述您的症状，我会为您提供初步诊断和建议。\n\n" +
        "请注意：AI问诊仅供参考，如有严重症状请及时就医。";

    @GetMapping("/init")
    @ApiOperation("获取AI问诊初始化问候语")
    public Result<String> getWelcomeMessage() {
        return Result.ok(WELCOME_MESSAGE);
    }

    @PostMapping("/chat")
    @ApiOperation("发送消息")
    public String chat(@RequestBody String message) {
        log.info("收到用户消息: {}", message);
        String response = aiService.generateContent(message);
        log.info("AI响应: {}", response);
        return response;
    }
} 