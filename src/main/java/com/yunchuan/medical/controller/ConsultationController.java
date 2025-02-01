package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * 问诊管理
 */
@Tag(name = "问诊与处方管理")
@RestController
@RequestMapping("/consultation")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @Operation(summary = "创建问诊")
    @PostMapping
    public Result<ConsultationDTO> createConsultation(@RequestBody ConsultationDTO consultation) {
        return Result.ok(consultationService.createConsultation(consultation));
    }

    @Operation(summary = "开始问诊")
    @PutMapping("/{id}/start")
    public Result<ConsultationDTO> startConsultation(@PathVariable String id) {
        return Result.ok(consultationService.startConsultation(id));
    }

    @Operation(summary = "开具处方")
    @PostMapping("/{id}/prescriptions")
    public Result<ConsultationDTO> createPrescription(@PathVariable String id, @RequestBody ConsultationDTO consultation) {
        return Result.ok(consultationService.completeConsultation(id, consultation));
    }
} 