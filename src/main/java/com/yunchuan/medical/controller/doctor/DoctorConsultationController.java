package com.yunchuan.medical.controller.doctor;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.dto.PrescriptionDTO;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.service.ConsultationService;
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

    @Operation(summary = "获取处方列表")
    @GetMapping("/{consultationId}/prescriptions")
    public Result<Object> getPrescriptionList(@PathVariable String consultationId) {
        // 模拟处方数据
        PrescriptionDTO prescription = PrescriptionDTO.builder()
                .id("1")
                .consultationId(consultationId)
                .doctorId("1")
                .patientId("u1")
                .diagnosis("偏头痛")
                .medications(Arrays.asList("布洛芬缓释胶囊 0.3g", "维生素B2片 5mg"))
                .dosage("1. 每日2次，共3天\n2. 每日3次，共7天")
                .instructions("餐后服用，如有不适请立即停药")
                .status("PENDING")
                .createTime(LocalDateTime.now())
                .build();
                
        return Result.ok(new Object() {
            public final List<PrescriptionDTO> list = Arrays.asList(prescription);
            public final int total = 1;
        });
    }
} 