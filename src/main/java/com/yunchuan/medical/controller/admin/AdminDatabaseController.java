package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;

/**
 * 管理员数据库操作
 */
@Slf4j
@Tag(name = "管理员-数据库操作")
@RestController
@RequestMapping("/api/admin/database")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDatabaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Operation(summary = "执行SQL语句")
    @PostMapping("/execute")
    public Result<Object> executeSql(@RequestBody SqlRequest request) {
        log.info("执行SQL语句: {}", request.getSql());
        String sql = request.getSql().trim().toLowerCase();
        
        try {
            if (sql.startsWith("select")) {
                // 对于SELECT查询,使用queryForList
                List<Map<String, Object>> result = jdbcTemplate.queryForList(request.getSql());
                return Result.ok(result);
            } else {
                // 对于非SELECT语句(INSERT/UPDATE/DELETE等),使用update
                int result = jdbcTemplate.update(request.getSql());
                return Result.ok(result);
            }
        } catch (Exception e) {
            log.error("SQL执行失败", e);
            return Result.error("SQL执行失败: " + e.getMessage());
        }
    }

    public static class SqlRequest {
        private String sql;

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }
    }
} 