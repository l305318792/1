package com.yunchuan.medical.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunchuan.medical.entity.Holiday;
import com.yunchuan.medical.service.HolidayService;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 节假日管理控制器测试类
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AdminHolidayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HolidayService holidayService;

    @Autowired
    private ObjectMapper objectMapper;

    private Holiday testHoliday;
    private List<Holiday> testHolidays;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testHoliday = new Holiday();
        testHoliday.setId("1");
        testHoliday.setHolidayDate(LocalDate.of(2024, 2, 10));
        testHoliday.setHolidayName("春节");
        testHoliday.setHolidayType("HOLIDAY");
        testHoliday.setStatus("ENABLED");
        testHoliday.setCreateTime(LocalDateTime.now());
        testHoliday.setUpdateTime(LocalDateTime.now());

        Holiday holiday2 = new Holiday();
        holiday2.setId("2");
        holiday2.setHolidayDate(LocalDate.of(2024, 2, 11));
        holiday2.setHolidayName("春节");
        holiday2.setHolidayType("HOLIDAY");
        holiday2.setStatus("ENABLED");

        testHolidays = Arrays.asList(testHoliday, holiday2);
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void createHoliday() throws Exception {
        when(holidayService.createHoliday(any(Holiday.class))).thenReturn(testHoliday);

        mockMvc.perform(post("/admin/holidays")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testHoliday)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testHoliday.getId()))
                .andExpect(jsonPath("$.data.holidayName").value(testHoliday.getHolidayName()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void updateHoliday() throws Exception {
        when(holidayService.updateHoliday(any(String.class), any(Holiday.class))).thenReturn(testHoliday);

        mockMvc.perform(put("/admin/holidays/{id}", testHoliday.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testHoliday)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testHoliday.getId()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void getHoliday() throws Exception {
        when(holidayService.getHoliday(testHoliday.getId())).thenReturn(testHoliday);

        mockMvc.perform(get("/admin/holidays/{id}", testHoliday.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testHoliday.getId()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void getHolidaysByDateRange() throws Exception {
        LocalDate startDate = LocalDate.of(2024, 2, 10);
        LocalDate endDate = LocalDate.of(2024, 2, 11);
        
        when(holidayService.getHolidaysByDateRange(startDate, endDate)).thenReturn(testHolidays);

        mockMvc.perform(get("/admin/holidays/range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void isHoliday() throws Exception {
        LocalDate date = LocalDate.of(2024, 2, 10);
        when(holidayService.isHoliday(date)).thenReturn(true);

        mockMvc.perform(get("/admin/holidays/check/holiday")
                .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void isWorkday() throws Exception {
        LocalDate date = LocalDate.of(2024, 2, 10);
        when(holidayService.isWorkday(date)).thenReturn(false);

        mockMvc.perform(get("/admin/holidays/check/workday")
                .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(false));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    void updateHolidayStatus() throws Exception {
        mockMvc.perform(put("/admin/holidays/{id}/status", testHoliday.getId())
                .param("status", "DISABLED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void unauthorizedAccess() throws Exception {
        mockMvc.perform(get("/admin/holidays/{id}", testHoliday.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", authorities = "ROLE_USER")
    void forbiddenAccess() throws Exception {
        mockMvc.perform(get("/admin/holidays/{id}", testHoliday.getId()))
                .andExpect(status().isForbidden());
    }
} 