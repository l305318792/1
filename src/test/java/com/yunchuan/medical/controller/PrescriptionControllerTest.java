package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.PrescriptionDTO;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.service.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 处方管理测试类
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrescriptionService prescriptionService;

    @Test
    @WithMockUser(username = "doctor", authorities = "DOCTOR")
    void testDeletePrescription_Success() throws Exception {
        // 准备测试数据
        String prescriptionId = "test-prescription-id";
        doNothing().when(prescriptionService).deletePrescription(prescriptionId);

        // 执行删除请求
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/doctor/prescriptions/{id}", prescriptionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("操作成功"));

        // 验证服务方法是否被调用
        verify(prescriptionService, times(1)).deletePrescription(prescriptionId);
    }

    @Test
    @WithMockUser(username = "doctor", authorities = "DOCTOR")
    void testDeletePrescription_NotFound() throws Exception {
        // 准备测试数据
        String prescriptionId = "non-existent-id";
        doThrow(new BusinessException("处方不存在"))
                .when(prescriptionService).deletePrescription(prescriptionId);

        // 执行删除请求
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/doctor/prescriptions/{id}", prescriptionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.msg").value("处方不存在"));
    }

    @Test
    @WithMockUser(username = "patient", authorities = "PATIENT")
    void testDeletePrescription_NoPermission() throws Exception {
        // 准备测试数据
        String prescriptionId = "test-prescription-id";

        // 执行删除请求
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/doctor/prescriptions/{id}", prescriptionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeletePrescription_Unauthorized() throws Exception {
        // 准备测试数据
        String prescriptionId = "test-prescription-id";

        // 执行删除请求（未登录）
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/doctor/prescriptions/{id}", prescriptionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "doctor", authorities = "DOCTOR")
    void testDeletePrescription_AlreadyDeleted() throws Exception {
        // 准备测试数据
        String prescriptionId = "deleted-prescription-id";
        doThrow(new BusinessException("处方已被删除"))
                .when(prescriptionService).deletePrescription(prescriptionId);

        // 执行删除请求
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/doctor/prescriptions/{id}", prescriptionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.msg").value("处方已被删除"));
    }
} 