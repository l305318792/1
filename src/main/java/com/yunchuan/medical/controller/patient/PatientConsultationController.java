package com.yunchuan.medical.controller.patient;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.dto.ConsultationMessageDTO;
import com.yunchuan.medical.dto.RatingDTO;
import com.yunchuan.medical.dto.PrescriptionDTO;
import com.yunchuan.medical.service.ConsultationService;
import com.yunchuan.medical.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.ArrayList;

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

    @Operation(summary = "发送问诊消息")
    @PostMapping("/{consultationId}/messages")
    public Result<ConsultationMessageDTO> sendMessage(
            @PathVariable String consultationId,
            @RequestBody ConsultationMessageDTO messageDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        messageDTO.setConsultationId(consultationId);
        messageDTO.setSenderId(userId);
        messageDTO.setSenderType("PATIENT");
        return Result.ok(consultationService.sendMessage(messageDTO));
    }

    @Operation(summary = "获取问诊消息历史")
    @GetMapping("/{consultationId}/messages")
    public Result<List<ConsultationMessageDTO>> getMessages(@PathVariable String consultationId) {
        return Result.ok(consultationService.getMessageHistory(consultationId));
    }

    @Operation(summary = "评价问诊")
    @PostMapping("/{consultationId}/rate")
    public Result<RatingDTO> rateConsultation(
            @PathVariable String consultationId,
            @RequestBody RatingDTO ratingDTO) {
        String userId = SecurityUtil.getCurrentUserId();
        ratingDTO.setUserId(userId);
        ratingDTO.setConsultationId(consultationId);
        return Result.ok(consultationService.rateConsultation(ratingDTO));
    }

    @Operation(summary = "关联预约到问诊")
    @PostMapping("/{consultationId}/link-appointment/{appointmentId}")
    public Result<ConsultationDTO> linkAppointment(
            @PathVariable String consultationId,
            @PathVariable String appointmentId) {
        String userId = SecurityUtil.getCurrentUserId();
        return Result.ok(consultationService.linkAppointment(consultationId, appointmentId, userId));
    }

    @Operation(summary = "获取问诊处方列表")
    @GetMapping("/{consultationId}/prescriptions")
    public Result<List<PrescriptionDTO>> getPrescriptions(@PathVariable String consultationId) {
        // 1. 验证问诊记录是否存在
        ConsultationDTO consultation = consultationService.getConsultation(consultationId);
        if (consultation == null) {
            return Result.error("问诊记录不存在");
        }

        // 2. 验证是否是当前用户的问诊记录
        String userId = SecurityUtil.getCurrentUserId();
        if (!userId.equals(consultation.getUserId())) {
            return Result.error("无权查看此问诊记录的处方");
        }

        // 3. 验证问诊状态
        if (!"COMPLETED".equals(consultation.getStatus()) && !"IN_PROGRESS".equals(consultation.getStatus())) {
            return Result.error("只能查看进行中或已完成的问诊处方");
        }

        // 4. 获取处方列表
        List<PrescriptionDTO> prescriptions = consultationService.getPrescriptionsByConsultationId(consultationId);

        // 5. 对处方数据进行清理和规范化
        prescriptions = prescriptions.stream()
            .map(prescription -> {
                // 清理medications中的多余空格
                if (prescription.getMedications() != null) {
                    prescription.setMedications(
                        prescription.getMedications().stream()
                            .map(med -> med.trim().replaceAll("\\s+", " "))
                            .collect(Collectors.toList())
                    );
                }
                
                // 清理instructions中的多余空格和换行符
                if (prescription.getInstructions() != null) {
                    prescription.setInstructions(
                        Arrays.stream(prescription.getInstructions()
                            .replace("\\n", "\n")
                            .split("\n"))
                            .map(line -> line.trim().replaceAll("\\s+", " "))
                            .filter(line -> !line.isEmpty())
                            .collect(Collectors.joining("\n"))
                    );
                }
                
                return prescription;
            })
            // 去重：根据诊断和用药内容
            .collect(Collectors.collectingAndThen(
                Collectors.toMap(
                    dto -> dto.getDiagnosis() + "|" + String.join(",", dto.getMedications()),
                    dto -> dto,
                    (existing, replacement) -> existing.getCreateTime().isAfter(replacement.getCreateTime()) 
                        ? existing 
                        : replacement
                ),
                map -> new ArrayList<>(map.values())
            ))
            .stream()
            .sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()))
            .collect(Collectors.toList());

        return Result.ok(prescriptions);
    }

    @Operation(summary = "获取处方详情")
    @GetMapping("/{consultationId}/prescriptions/{prescriptionId}")
    public Result<PrescriptionDTO> getPrescriptionDetail(
            @PathVariable String consultationId,
            @PathVariable String prescriptionId) {
        // 1. 获取处方详情
        PrescriptionDTO prescription = consultationService.getPrescriptionDetail(prescriptionId);
        if (prescription == null) {
            return Result.error("处方不存在");
        }

        // 2. 验证是否是当前用户的处方
        String userId = SecurityUtil.getCurrentUserId();
        if (!userId.equals(prescription.getPatientId())) {
            return Result.error("无权查看此处方");
        }

        return Result.ok(prescription);
    }
} 