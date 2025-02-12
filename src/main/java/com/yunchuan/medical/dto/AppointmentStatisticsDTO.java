package com.yunchuan.medical.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

/**
 * 预约统计DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatisticsDTO {
    /**
     * 总预约数
     */
    private Long totalAppointments;
    
    /**
     * 各状态预约数量
     */
    private Map<String, Long> statusCount;
    
    /**
     * 各科室预约数量
     */
    private Map<String, Long> departmentCount;
    
    /**
     * 今日预约数
     */
    private Long todayAppointments;
    
    /**
     * 本周预约数
     */
    private Long weekAppointments;
    
    /**
     * 本月预约数
     */
    private Long monthAppointments;
} 