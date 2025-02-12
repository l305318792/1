package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.response.ResponseResult;
import com.yunchuan.medical.dto.dashboard.DashboardStatisticsDTO;
import com.yunchuan.medical.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 管理员仪表盘控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
@Tag(name = "管理员仪表盘接口")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    @Operation(summary = "获取仪表盘统计数据")
    public ResponseResult<DashboardStatisticsDTO> getDashboardStatistics(
            @Parameter(description = "开始日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("获取仪表盘统计数据: startDate={}, endDate={}", startDate, endDate);
        return ResponseResult.success(dashboardService.getDashboardStatistics(startDate, endDate));
    }

    @GetMapping("/user-trend")
    @Operation(summary = "获取用户趋势数据")
    public ResponseResult<DashboardStatisticsDTO.UserTrendDTO> getUserTrend(
            @Parameter(description = "开始日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("获取用户趋势数据: startDate={}, endDate={}", startDate, endDate);
        return ResponseResult.success(dashboardService.getUserTrend(startDate, endDate));
    }

    @GetMapping("/appointment-stats")
    @Operation(summary = "获取预约统计数据")
    public ResponseResult<DashboardStatisticsDTO.AppointmentStatsDTO> getAppointmentStats(
            @Parameter(description = "开始日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("获取预约统计数据: startDate={}, endDate={}", startDate, endDate);
        return ResponseResult.success(dashboardService.getAppointmentStats(startDate, endDate));
    }

    @GetMapping("/department-ranks")
    @Operation(summary = "获取科室排行数据")
    public ResponseResult<java.util.List<DashboardStatisticsDTO.DepartmentRankDTO>> getDepartmentRanks(
            @Parameter(description = "开始日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("获取科室排行数据: startDate={}, endDate={}", startDate, endDate);
        return ResponseResult.success(dashboardService.getDepartmentRanks(startDate, endDate));
    }

    @GetMapping("/appointment-dynamics")
    @Operation(summary = "获取实时预约动态")
    public ResponseResult<java.util.List<DashboardStatisticsDTO.AppointmentDynamicDTO>> getAppointmentDynamics(
            @Parameter(description = "限制条数") 
            @RequestParam(defaultValue = "10") int limit) {
        log.info("获取实时预约动态: limit={}", limit);
        return ResponseResult.success(dashboardService.getAppointmentDynamics(limit));
    }

    @GetMapping("/today-overview")
    @Operation(summary = "获取今日概览数据")
    public ResponseResult<DashboardStatisticsDTO.TodayOverviewDTO> getTodayOverview() {
        log.info("获取今日概览数据");
        return ResponseResult.success(dashboardService.getTodayOverview());
    }
} 