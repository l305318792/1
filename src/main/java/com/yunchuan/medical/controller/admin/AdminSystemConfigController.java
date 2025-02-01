package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.SystemConfigDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员-系统配置
 */
@Tag(name = "管理员-系统配置")
@RestController
@RequestMapping("/admin/configs")
public class AdminSystemConfigController {

    @Operation(summary = "获取系统配置列表")
    @GetMapping
    public Result<Object> list() {
        // 模拟配置数据
        List<SystemConfigDTO> configs = new ArrayList<>();
        
        // 系统配置
        SystemConfigDTO config1 = SystemConfigDTO.builder()
                .id("1")
                .configKey("SYSTEM_NAME")
                .configValue("运城市移动医疗咨询平台")
                .description("系统名称")
                .type("SYSTEM")
                .editable(false)
                .createTime(LocalDateTime.now().minusDays(30))
                .updateTime(LocalDateTime.now().minusDays(30))
                .build();
        configs.add(config1);
        
        SystemConfigDTO config2 = SystemConfigDTO.builder()
                .id("2")
                .configKey("SYSTEM_LOGO")
                .configValue("/static/images/logo.png")
                .description("系统Logo图片路径")
                .type("SYSTEM")
                .editable(true)
                .createTime(LocalDateTime.now().minusDays(30))
                .updateTime(LocalDateTime.now().minusDays(15))
                .build();
        configs.add(config2);
        
        // 业务配置
        SystemConfigDTO config3 = SystemConfigDTO.builder()
                .id("3")
                .configKey("DEFAULT_CONSULTATION_FEE")
                .configValue("50")
                .description("默认问诊费用(元)")
                .type("BUSINESS")
                .editable(true)
                .createTime(LocalDateTime.now().minusDays(30))
                .updateTime(LocalDateTime.now().minusDays(10))
                .build();
        configs.add(config3);
        
        SystemConfigDTO config4 = SystemConfigDTO.builder()
                .id("4")
                .configKey("MAX_APPOINTMENTS_PER_DAY")
                .configValue("20")
                .description("每日最大预约数")
                .type("BUSINESS")
                .editable(true)
                .createTime(LocalDateTime.now().minusDays(30))
                .updateTime(LocalDateTime.now().minusDays(5))
                .build();
        configs.add(config4);
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", configs.size());
        result.put("list", configs);
        
        return Result.ok(result);
    }
    
    @Operation(summary = "更新系统配置")
    @PutMapping("/{id}")
    public Result<SystemConfigDTO> update(@PathVariable String id, @RequestBody SystemConfigDTO configDTO) {
        // 模拟更新配置
        configDTO.setId(id);
        configDTO.setUpdateTime(LocalDateTime.now());
        
        return Result.ok(configDTO);
    }
} 