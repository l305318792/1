package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.MedicalRecordDTO;
import com.yunchuan.medical.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * 病历管理
 */
@Tag(name = "查看病例与处方")
@RestController
@RequestMapping("/medical-record")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * 获取病历列表
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public Result<List<MedicalRecordDTO>> list(@RequestParam(required = false) String patientId) {
        return Result.ok(medicalRecordService.getMedicalRecordList(patientId));
    }

    /**
     * 获取病历详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public Result<MedicalRecordDTO> get(@PathVariable String id) {
        return Result.ok(medicalRecordService.getMedicalRecordById(id));
    }

    /**
     * 创建病历
     */
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public Result<MedicalRecordDTO> add(@RequestBody @Valid MedicalRecordDTO medicalRecord) {
        return Result.ok(medicalRecordService.createMedicalRecord(medicalRecord));
    }

    /**
     * 更新病历
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public Result<MedicalRecordDTO> update(@PathVariable String id, @RequestBody @Valid MedicalRecordDTO medicalRecord) {
        medicalRecord.setId(id);
        return Result.ok(medicalRecordService.updateMedicalRecord(medicalRecord));
    }

    /**
     * 删除病历
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public Result<Boolean> delete(@PathVariable String id) {
        medicalRecordService.deleteMedicalRecord(id);
        return Result.ok(true);
    }
} 