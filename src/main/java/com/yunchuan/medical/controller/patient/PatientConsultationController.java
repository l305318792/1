package com.yunchuan.medical.controller.patient;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.service.ConsultationService;
import com.yunchuan.medical.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 患者问诊管理
 */
@Tag(name = "患者-问诊管理")
@RestController
@RequestMapping("/patient/consultations")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class PatientConsultationController {

    private final ConsultationService consultationService;

    public PatientConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @Operation(summary = "获取我的问诊列表")
    @GetMapping("/my")
    public Result<List<ConsultationDTO>> getMyConsultations() {
        String userId = SecurityUtil.getCurrentUserId();
        return Result.ok(consultationService.getConsultationListByUserId(userId));
    }

    @Operation(summary = "获取问诊详情")
    @GetMapping("/{id}")
    public Result<ConsultationDTO> getConsultation(@PathVariable String id) {
        ConsultationDTO consultation = consultationService.getConsultation(id);
        if (consultation == null) {
            return Result.error("问诊记录不存在");
        }
        // 验证是否是当前用户的问诊记录
        String userId = SecurityUtil.getCurrentUserId();
        if (!userId.equals(consultation.getUserId())) {
            return Result.error("无权查看此问诊记录");
        }
        return Result.ok(consultation);
    }

    @Operation(summary = "创建问诊")
    @PostMapping
    public Result<ConsultationDTO> createConsultation(@RequestBody ConsultationDTO consultationDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        consultationDTO.setUserId(userId);
        return Result.ok(consultationService.createConsultation(consultationDTO));
    }
} 