package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.dashboard.DashboardStatisticsDTO;
import com.yunchuan.medical.service.DashboardService;
import com.yunchuan.medical.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

/**
 * 管理员-统计数据
 */
@Slf4j
@Tag(name = "管理员-统计数据")
@RestController
@RequestMapping("/admin/statistics")
public class AdminStatisticsController {

    private final DashboardService dashboardService;
    private final RatingService ratingService;

    public AdminStatisticsController(DashboardService dashboardService, RatingService ratingService) {
        this.dashboardService = dashboardService;
        this.ratingService = ratingService;
    }

    @Operation(summary = "获取仪表盘统计数据")
    @GetMapping("/dashboard")
    public Result<DashboardStatisticsDTO> getDashboardStatistics(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        DashboardStatisticsDTO stats = dashboardService.getDashboardStatistics(startDate, endDate);
        return Result.ok(stats);
    }

    @Operation(summary = "获取医生评分统计")
    @GetMapping("/doctor/{doctorId}/ratings")
    public Result<Map<String, Object>> getDoctorRatingStats(@PathVariable String doctorId) {
        Map<String, Object> stats = ratingService.getDoctorRatingStats(doctorId);
        return Result.ok(stats);
    }
} 