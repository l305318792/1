package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员-系统统计
 */
@Tag(name = "管理员-系统统计")
@RestController
@RequestMapping("/admin/statistics")
public class AdminStatisticsController {

    @Operation(summary = "获取系统统计数据")
    @GetMapping
    public Result<Object> getStatistics() {
        // 模拟统计数据
        Map<String, Object> statistics = new HashMap<>();
        
        // 用户统计
        Map<String, Object> userStats = new HashMap<>();
        userStats.put("total", 100);
        userStats.put("todayNew", 5);
        userStats.put("weekNew", 20);
        userStats.put("monthNew", 50);
        statistics.put("userStats", userStats);
        
        // 问诊统计
        Map<String, Object> consultationStats = new HashMap<>();
        consultationStats.put("total", 500);
        consultationStats.put("completed", 400);
        consultationStats.put("pending", 50);
        consultationStats.put("cancelled", 50);
        consultationStats.put("todayNew", 10);
        consultationStats.put("weekNew", 50);
        consultationStats.put("monthNew", 150);
        statistics.put("consultationStats", consultationStats);
        
        // 科室统计
        Map<String, Object> departmentStats = new HashMap<>();
        departmentStats.put("total", 10);
        departmentStats.put("active", 8);
        departmentStats.put("inactive", 2);
        statistics.put("departmentStats", departmentStats);
        
        // 医生统计
        Map<String, Object> doctorStats = new HashMap<>();
        doctorStats.put("total", 50);
        doctorStats.put("active", 45);
        doctorStats.put("inactive", 5);
        statistics.put("doctorStats", doctorStats);
        
        // 评价统计
        Map<String, Object> ratingStats = new HashMap<>();
        ratingStats.put("total", 300);
        ratingStats.put("avgRating", 4.5);
        ratingStats.put("todayNew", 8);
        ratingStats.put("weekNew", 35);
        ratingStats.put("monthNew", 100);
        statistics.put("ratingStats", ratingStats);
        
        // 收入统计
        Map<String, Object> incomeStats = new HashMap<>();
        incomeStats.put("total", 50000.00);
        incomeStats.put("today", 1000.00);
        incomeStats.put("week", 5000.00);
        incomeStats.put("month", 15000.00);
        statistics.put("incomeStats", incomeStats);
        
        // 每日问诊趋势(近7天)
        List<Map<String, Object>> dailyTrend = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 6; i >= 0; i--) {
            Map<String, Object> daily = new HashMap<>();
            daily.put("date", now.minusDays(i).toLocalDate().toString());
            daily.put("total", 10 + i * 2);
            daily.put("completed", 8 + i);
            daily.put("cancelled", 2 + i);
            dailyTrend.add(daily);
        }
        statistics.put("dailyTrend", dailyTrend);
        
        return Result.ok(statistics);
    }
} 