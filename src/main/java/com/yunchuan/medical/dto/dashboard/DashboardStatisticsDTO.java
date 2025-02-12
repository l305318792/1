package com.yunchuan.medical.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

/**
 * 仪表盘统计数据DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "仪表盘统计数据DTO")
public class DashboardStatisticsDTO {
    
    /**
     * 用户趋势数据
     */
    @Schema(description = "用户趋势数据")
    private UserTrendDTO userTrend;
    
    /**
     * 预约统计数据
     */
    @Schema(description = "预约统计数据")
    private AppointmentStatsDTO appointmentStats;
    
    /**
     * 科室排行数据
     */
    @Schema(description = "科室排行数据")
    private List<DepartmentRankDTO> departmentRanks;
    
    /**
     * 实时预约动态
     */
    @Schema(description = "预约动态数据")
    private List<AppointmentDynamicDTO> appointmentDynamics;
    
    /**
     * 今日数据概览
     */
    @Schema(description = "今日概览数据")
    private TodayOverviewDTO todayOverview;

    /**
     * 用户趋势数据DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "用户趋势数据DTO")
    public static class UserTrendDTO {
        /**
         * 日期列表
         */
        @Schema(description = "日期列表")
        private List<String> dates;
        
        /**
         * 新增用户数
         */
        @Schema(description = "新增用户数")
        private List<Integer> newUsers;
        
        /**
         * 活跃用户数
         */
        @Schema(description = "活跃用户数")
        private List<Integer> activeUsers;
    }

    /**
     * 预约统计数据DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "预约统计数据DTO")
    public static class AppointmentStatsDTO {
        /**
         * 总预约数
         */
        @Schema(description = "总预约数")
        private Integer totalAppointments;
        
        /**
         * 已完成预约数
         */
        @Schema(description = "已完成预约数")
        private Integer completedAppointments;
        
        /**
         * 待就诊预约数
         */
        @Schema(description = "待处理预约数")
        private Integer pendingAppointments;
        
        /**
         * 已取消预约数
         */
        @Schema(description = "已取消预约数")
        private Integer cancelledAppointments;
        
        /**
         * 预约趋势数据
         */
        @Schema(description = "预约趋势")
        private List<DailyAppointmentDTO> trend;
    }

    /**
     * 每日预约数据DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "每日预约数据DTO")
    public static class DailyAppointmentDTO {
        /**
         * 日期
         */
        @Schema(description = "日期")
        private String date;
        
        /**
         * 预约数量
         */
        @Schema(description = "预约数量")
        private Integer count;
    }

    /**
     * 科室排行数据DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "科室排行数据DTO")
    public static class DepartmentRankDTO {
        /**
         * 科室ID
         */
        @Schema(description = "科室ID")
        private String departmentId;
        
        /**
         * 科室名称
         */
        @Schema(description = "科室名称")
        private String departmentName;
        
        /**
         * 预约数量
         */
        @Schema(description = "预约数量")
        private Integer appointmentCount;
        
        /**
         * 医生数量
         */
        @Schema(description = "医生数量")
        private Integer doctorCount;
        
        /**
         * 收入金额
         */
        @Schema(description = "收入金额")
        private BigDecimal income;
    }

    /**
     * 预约动态数据DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "预约动态数据DTO")
    public static class AppointmentDynamicDTO {
        /**
         * 预约ID
         */
        @Schema(description = "预约ID")
        private String appointmentId;
        
        /**
         * 患者姓名
         */
        @Schema(description = "患者姓名")
        private String patientName;
        
        /**
         * 医生姓名
         */
        @Schema(description = "医生姓名")
        private String doctorName;
        
        /**
         * 科室名称
         */
        @Schema(description = "科室名称")
        private String departmentName;
        
        /**
         * 预约时间
         */
        @Schema(description = "预约时间")
        private String appointmentTime;
        
        /**
         * 状态
         */
        @Schema(description = "状态")
        private String status;
    }

    /**
     * 今日概览数据DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "今日概览数据DTO")
    public static class TodayOverviewDTO {
        /**
         * 今日预约数
         */
        @Schema(description = "今日预约数")
        private Integer todayAppointments;
        
        /**
         * 今日就诊数
         */
        @Schema(description = "今日就诊数")
        private Integer todayVisits;
        
        /**
         * 今日收入
         */
        @Schema(description = "今日收入")
        private BigDecimal todayIncome;
        
        /**
         * 今日新增用户
         */
        @Schema(description = "今日新增用户")
        private Integer todayNewUsers;
    }
} 