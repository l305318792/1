package com.yunchuan.medical.controller.patient;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.MedicalRecordDTO;
import com.yunchuan.medical.service.MedicalRecordService;
import com.yunchuan.medical.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 患者-病历管理
 */
@Tag(name = "患者-病历管理")
@RestController
@RequestMapping("/patient/medical-records")
@PreAuthorize("hasAuthority('PATIENT')")
public class PatientMedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public PatientMedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * 获取我的病历列表
     */
    @Operation(summary = "获取我的病历列表")
    @GetMapping
    public Result<List<MedicalRecordDTO>> getMyMedicalRecords() {
        String userId = SecurityUtil.getCurrentUserId();
        return Result.ok(medicalRecordService.getMedicalRecordList(userId));
    }

    /**
     * 获取我的病历列表（别名路由）
     */
    @Operation(summary = "获取我的病历列表")
    @GetMapping("/my")
    public Result<List<MedicalRecordDTO>> getMyMedicalRecordsAlias() {
        return getMyMedicalRecords();
    }

    /**
     * 获取病历详情
     */
    @Operation(summary = "获取病历详情")
    @GetMapping("/{id}")
    public Result<MedicalRecordDTO> getMedicalRecord(@PathVariable String id) {
        // 1. 获取病历详情
        MedicalRecordDTO record = medicalRecordService.getMedicalRecordById(id);
        if (record == null) {
            return Result.error("病历不存在");
        }

        // 2. 验证是否是当前用户的病历
        String userId = SecurityUtil.getCurrentUserId();
        if (!userId.equals(record.getPatientId())) {
            return Result.error("无权查看此病历");
        }

        return Result.ok(record);
    }

    /**
     * 根据问诊记录自动生成病历
     */
    @Operation(summary = "根据问诊记录自动生成病历")
    @PostMapping("/generate/{consultationId}")
    public Result<MedicalRecordDTO> generateFromConsultation(@PathVariable String consultationId) {
        try {
            MedicalRecordDTO record = medicalRecordService.generateFromConsultation(consultationId);
            return Result.ok(record);
        } catch (Exception e) {
            return Result.error("生成病历失败：" + e.getMessage());
        }
    }
} 