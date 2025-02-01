package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.BackupDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员-数据备份
 */
@Tag(name = "管理员-数据备份")
@RestController
@RequestMapping("/admin/backups")
public class AdminBackupController {

    @Operation(summary = "获取备份列表")
    @GetMapping
    public Result<Object> list() {
        // 模拟备份数据
        List<BackupDTO> backups = new ArrayList<>();
        
        // 自动备份
        BackupDTO backup1 = BackupDTO.builder()
                .id("1")
                .fileName("medical_auto_20240125_000000.sql")
                .fileSize(1024L * 1024L) // 1MB
                .type("AUTO")
                .description("系统自动备份")
                .status("SUCCESS")
                .createTime(LocalDateTime.now().minusDays(1))
                .build();
        backups.add(backup1);
        
        // 手动备份
        BackupDTO backup2 = BackupDTO.builder()
                .id("2")
                .fileName("medical_manual_20240125_140000.sql")
                .fileSize(1024L * 1024L * 2L) // 2MB
                .type("MANUAL")
                .description("更新系统配置前的手动备份")
                .status("SUCCESS")
                .createTime(LocalDateTime.now().minusHours(2))
                .build();
        backups.add(backup2);
        
        // 失败的备份
        BackupDTO backup3 = BackupDTO.builder()
                .id("3")
                .fileName("medical_auto_20240125_120000.sql")
                .fileSize(0L)
                .type("AUTO")
                .description("系统自动备份")
                .status("FAIL")
                .errorMessage("磁盘空间不足")
                .createTime(LocalDateTime.now().minusHours(4))
                .build();
        backups.add(backup3);
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", backups.size());
        result.put("list", backups);
        
        return Result.ok(result);
    }
    
    @Operation(summary = "创建备份")
    @PostMapping
    public Result<BackupDTO> create(@RequestBody BackupDTO backupDTO) {
        // 模拟创建备份
        backupDTO.setId("4");
        backupDTO.setFileName("medical_manual_" + LocalDateTime.now().toString().replace(":", "") + ".sql");
        backupDTO.setFileSize(1024L * 1024L * 3L); // 3MB
        backupDTO.setType("MANUAL");
        backupDTO.setStatus("SUCCESS");
        backupDTO.setCreateTime(LocalDateTime.now());
        
        return Result.ok(backupDTO);
    }
    
    @Operation(summary = "恢复数据")
    @PostMapping("/{id}/restore")
    public Result<Boolean> restore(@PathVariable String id) {
        // 模拟数据恢复
        return Result.ok(true);
    }
    
    @Operation(summary = "删除备份")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable String id) {
        // 模拟删除备份
        return Result.ok(true);
    }
} 