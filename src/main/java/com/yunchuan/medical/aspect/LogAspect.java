package com.yunchuan.medical.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 日志切面
 * @author yunchuan
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    
    @Around("execution(* com.yunchuan.medical.controller..*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.info("开始执行方法: {}", methodName);
        
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        
        log.info("方法: {} 执行完成, 耗时: {}ms", methodName, (endTime - startTime));
        log.info("返回结果: {}", result);
        
        return result;
    }
} 