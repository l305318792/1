package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import java.util.stream.Collectors;
import com.yunchuan.medical.entity.Consultation;
import com.yunchuan.medical.mapper.ConsultationMapper;
import com.yunchuan.medical.service.DataCleanService;

/**
 * 管理员问诊管理
 */
@Tag(name = "管理员-问诊管理")
@RestController
@RequestMapping("/admin/consultations")
@PreAuthorize("hasRole('ADMIN')")
public class AdminConsultationController {

    private final ConsultationService consultationService;
    private final ConsultationMapper consultationMapper;
    private final DataCleanService dataCleanService;

    public AdminConsultationController(
            ConsultationService consultationService,
            ConsultationMapper consultationMapper,
            DataCleanService dataCleanService) {
        this.consultationService = consultationService;
        this.consultationMapper = consultationMapper;
        this.dataCleanService = dataCleanService;
    }

    @Operation(summary = "获取问诊列表")
    @GetMapping
    public Result<List<ConsultationDTO>> getConsultationList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) String departmentId) {
        // 构建查询条件
        LambdaQueryWrapper<Consultation> wrapper = new LambdaQueryWrapper<>();
        
        // 添加状态过滤
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Consultation::getStatus, status);
        }
        
        // 添加医生过滤
        if (doctorId != null && !doctorId.isEmpty()) {
            wrapper.eq(Consultation::getDoctorId, doctorId);
        }
        
        // 添加科室过滤
        if (departmentId != null && !departmentId.isEmpty()) {
            wrapper.eq(Consultation::getDepartmentId, departmentId);
        }
        
        // 按创建时间倒序排序
        wrapper.orderByDesc(Consultation::getCreateTime);
        
        // 使用consultationService的方法
        return Result.ok(consultationService.getConsultationList());
    }

    @Operation(summary = "获取问诊详情")
    @GetMapping("/{id}")
    public Result<ConsultationDTO> getConsultation(@PathVariable String id) {
        ConsultationDTO consultation = consultationService.getConsultation(id);
        if (consultation == null) {
            return Result.error("问诊记录不存在");
        }
        return Result.ok(consultation);
    }

    @Operation(summary = "清理所有数据")
    @PostMapping("/clean-data")
    public Result<Void> cleanHistoricalData() {
        dataCleanService.cleanAllData();
        return Result.ok();
    }
} 