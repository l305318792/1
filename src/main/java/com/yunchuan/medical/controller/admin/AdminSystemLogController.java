package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.SystemLogDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员-系统日志
 */
@Tag(name = "管理员-系统日志")
@RestController
@RequestMapping("/admin/logs")
public class AdminSystemLogController {

    @Operation(summary = "获取系统日志列表")
    @GetMapping
    public Result<Object> list(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String status) {
        // 模拟日志数据
        List<SystemLogDTO> logs = new ArrayList<>();
        
        // 日志1 - 用户登录
        SystemLogDTO log1 = SystemLogDTO.builder()
                .id("1")
                .userId("1")
                .username("admin")
                .operationType("LOGIN")
                .description("管理员登录系统")
                .module("认证模块")
                .ip("192.168.1.100")
                .status("SUCCESS")
                .createTime(LocalDateTime.now().minusHours(2))
                .build();
        logs.add(log1);
        
        // 日志2 - 创建医生
        SystemLogDTO log2 = SystemLogDTO.builder()
                .id("2")
                .userId("1")
                .username("admin")
                .operationType("CREATE")
                .description("创建医生账号：李医生")
                .module("医生管理")
                .ip("192.168.1.100")
                .status("SUCCESS")
                .createTime(LocalDateTime.now().minusHours(1))
                .build();
        logs.add(log2);
        
        // 日志3 - 更新部门
        SystemLogDTO log3 = SystemLogDTO.builder()
                .id("3")
                .userId("1")
                .username("admin")
                .operationType("UPDATE")
                .description("更新科室信息：内科")
                .module("科室管理")
                .ip("192.168.1.100")
                .status("SUCCESS")
                .createTime(LocalDateTime.now().minusMinutes(30))
                .build();
        logs.add(log3);
        
        // 日志4 - 删除用户失败
        SystemLogDTO log4 = SystemLogDTO.builder()
                .id("4")
                .userId("1")
                .username("admin")
                .operationType("DELETE")
                .description("删除用户：zhang")
                .module("用户管理")
                .ip("192.168.1.100")
                .status("FAIL")
                .errorMessage("用户不存在")
                .createTime(LocalDateTime.now().minusMinutes(15))
                .build();
        logs.add(log4);
        
        // 根据查询参数过滤日志
        if (username != null && !username.isEmpty()) {
            logs = logs.stream()
                    .filter(log -> log.getUsername().equals(username))
                    .collect(Collectors.toList());
        }
        
        if (operationType != null && !operationType.isEmpty()) {
            logs = logs.stream()
                    .filter(log -> log.getOperationType().equals(operationType))
                    .collect(Collectors.toList());
        }
        
        if (module != null && !module.isEmpty()) {
            logs = logs.stream()
                    .filter(log -> log.getModule().equals(module))
                    .collect(Collectors.toList());
        }
        
        if (status != null && !status.isEmpty()) {
            logs = logs.stream()
                    .filter(log -> log.getStatus().equals(status))
                    .collect(Collectors.toList());
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", logs.size());
        result.put("list", logs);
        
        return Result.ok(result);
    }
}
