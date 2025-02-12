package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.dashboard.DashboardStatisticsDTO;
import java.time.LocalDate;
import java.util.List;

/**
 * 仪表盘服务接口
 */
public interface DashboardService {
    
    /**
     * 获取仪表盘统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 仪表盘统计数据
     */
    DashboardStatisticsDTO getDashboardStatistics(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取用户趋势数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 用户趋势数据
     */
    DashboardStatisticsDTO.UserTrendDTO getUserTrend(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取预约统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预约统计数据
     */
    DashboardStatisticsDTO.AppointmentStatsDTO getAppointmentStats(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取科室排行数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 科室排行数据
     */
    List<DashboardStatisticsDTO.DepartmentRankDTO> getDepartmentRanks(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取实时预约动态
     * @param limit 限制条数
     * @return 实时预约动态数据
     */
    List<DashboardStatisticsDTO.AppointmentDynamicDTO> getAppointmentDynamics(int limit);
    
    /**
     * 获取今日概览数据
     * @return 今日概览数据
     */
    DashboardStatisticsDTO.TodayOverviewDTO getTodayOverview();
} 