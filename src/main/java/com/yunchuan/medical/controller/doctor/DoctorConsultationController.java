package com.yunchuan.medical.controller.doctor;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.dto.PrescriptionDTO;
import com.yunchuan.medical.dto.ConsultationMessageDTO;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.service.ConsultationService;
import com.yunchuan.medical.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 医生问诊与处方管理
 */
@Tag(name = "医生-问诊管理")
@RestController
@RequestMapping("/doctor/consultations")
@Slf4j
public class DoctorConsultationController {

    private final ConsultationService consultationService;
    
    public DoctorConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @Operation(summary = "获取问诊列表")
    @GetMapping
    public Result<List<ConsultationDTO>> getConsultationList() {
        return Result.ok(consultationService.getConsultationList());
    }

    @Operation(summary = "获取待处理问诊列表")
    @GetMapping("/pending")
    public Result<List<ConsultationDTO>> getPendingConsultations() {
        String doctorId = SecurityUtil.getCurrentUserId();
        List<ConsultationDTO> consultations = consultationService.getConsultationListByDoctorIdAndStatus(doctorId, "PENDING");
        return Result.ok(consultations);
    }

    @Operation(summary = "获取问诊详情")
    @GetMapping("/{id}")
    public Result<ConsultationDTO> getConsultation(@PathVariable String id) {
        ConsultationDTO consultation = consultationService.getConsultation(id);
        if (consultation == null) {
            throw new BusinessException(400, "问诊记录不存在");
        }
        return Result.ok(consultation);
    }

    @Operation(summary = "开始问诊")
    @PutMapping("/{id}/start")
    public Result<ConsultationDTO> startConsultation(@PathVariable String id) {
        return Result.ok(consultationService.startConsultation(id));
    }

    @Operation(summary = "结束问诊")
    @PostMapping("/{id}/complete")
    public Result<ConsultationDTO> completeConsultation(
            @PathVariable String id,
            @RequestBody ConsultationDTO consultation) {
        ConsultationDTO existingConsultation = consultationService.getConsultation(id);
        if (existingConsultation == null) {
            throw new BusinessException(400, "问诊记录不存在");
        }
        return Result.ok(consultationService.completeConsultation(id, consultation));
    }

    @Operation(summary = "开具处方")
    @PostMapping("/{consultationId}/prescriptions")
    public Result<PrescriptionDTO> createPrescription(
            @PathVariable String consultationId,
            @RequestBody PrescriptionDTO prescription) {
        ConsultationDTO consultation = consultationService.getConsultation(consultationId);
        if (consultation == null) {
            throw new BusinessException(400, "问诊记录不存在");
        }
        prescription.setConsultationId(consultationId);
        return Result.ok(consultationService.createPrescription(prescription));
    }

    @Operation(summary = "发送问诊消息")
    @PostMapping("/{consultationId}/messages")
    public Result<ConsultationMessageDTO> sendMessage(
            @PathVariable String consultationId,
            @RequestBody ConsultationMessageDTO messageDTO) {
        String doctorId = SecurityUtil.getCurrentUserId();
        messageDTO.setConsultationId(consultationId);
        messageDTO.setSenderId(doctorId);
        messageDTO.setSenderType("DOCTOR");
        return Result.ok(consultationService.sendMessage(messageDTO));
    }

    @Operation(summary = "获取问诊消息历史")
    @GetMapping("/{consultationId}/messages")
    public Result<List<ConsultationMessageDTO>> getMessages(@PathVariable String consultationId) {
        return Result.ok(consultationService.getMessageHistory(consultationId));
    }

    @Operation(summary = "获取处方列表")
    @GetMapping("/{consultationId}/prescriptions")
    public Result<Object> getPrescriptionList(@PathVariable String consultationId) {
        List<PrescriptionDTO> prescriptions = consultationService.getPrescriptionsByConsultationId(consultationId);
        return Result.ok(new Object() {
            public final List<PrescriptionDTO> list = prescriptions;
            public final int total = prescriptions.size();
        });
    }
} 