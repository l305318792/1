package com.yunchuan.medical.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunchuan.medical.dto.dashboard.DashboardStatisticsDTO;
import com.yunchuan.medical.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 管理员仪表盘控制器测试类
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AdminDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @Autowired
    private ObjectMapper objectMapper;

    private DashboardStatisticsDTO testStatistics;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testStatistics = DashboardStatisticsDTO.builder()
                .userTrend(DashboardStatisticsDTO.UserTrendDTO.builder()
                        .dates(Arrays.asList("2024-02-01", "2024-02-02"))
                        .newUsers(Arrays.asList(10, 15))
                        .activeUsers(Arrays.asList(50, 60))
                        .build())
                .appointmentStats(DashboardStatisticsDTO.AppointmentStatsDTO.builder()
                        .totalAppointments(100)
                        .completedAppointments(80)
                        .pendingAppointments(15)
                        .cancelledAppointments(5)
                        .build())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void getDashboardStatistics() throws Exception {
        when(dashboardService.getDashboardStatistics(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(testStatistics);

        mockMvc.perform(get("/admin/dashboard/statistics")
                .param("startDate", "2024-02-01")
                .param("endDate", "2024-02-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userTrend.dates").isArray())
                .andExpect(jsonPath("$.data.userTrend.newUsers").isArray())
                .andExpect(jsonPath("$.data.userTrend.activeUsers").isArray())
                .andExpect(jsonPath("$.data.appointmentStats.totalAppointments").value(100))
                .andExpect(jsonPath("$.data.appointmentStats.completedAppointments").value(80))
                .andExpect(jsonPath("$.data.appointmentStats.pendingAppointments").value(15))
                .andExpect(jsonPath("$.data.appointmentStats.cancelledAppointments").value(5));
    }

    @Test
    void unauthorizedAccess() throws Exception {
        mockMvc.perform(get("/admin/dashboard/statistics"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", authorities = "ROLE_USER")
    void forbiddenAccess() throws Exception {
        mockMvc.perform(get("/admin/dashboard/statistics"))
                .andExpect(status().isForbidden());
    }
} 