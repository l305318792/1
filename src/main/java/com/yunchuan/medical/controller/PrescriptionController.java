package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.PrescriptionDTO;
import com.yunchuan.medical.service.PrescriptionService;
import com.yunchuan.medical.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 处方管理
 */
@Tag(name = "处方管理")
@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private static final Logger log = LoggerFactory.getLogger(PrescriptionController.class);

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    /**
     * 获取处方列表
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public Result<List<PrescriptionDTO>> list(@RequestParam(required = false) String recordId) {
        return Result.ok(prescriptionService.getPrescriptionList(recordId));
    }

    /**
     * 获取处方详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public Result<PrescriptionDTO> get(@PathVariable String id) {
        return Result.ok(prescriptionService.getPrescriptionById(id));
    }

    /**
     * 开具处方
     */
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public Result<PrescriptionDTO> add(@RequestBody @Valid PrescriptionDTO prescription) {
        return Result.ok(prescriptionService.createPrescription(prescription));
    }

    /**
     * 更新处方
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public Result<PrescriptionDTO> update(@PathVariable String id, @RequestBody @Valid PrescriptionDTO prescription) {
        try {
            if (prescription == null) {
                String msg = "处方数据不能为空";
                log.error("更新处方失败：{}", msg);
                return Result.error(msg);
            }
            
            if (id == null || id.trim().isEmpty()) {
                String msg = "处方ID不能为空";
                log.error("更新处方失败：{}", msg);
                return Result.error(msg);
            }
            
            log.info("开始更新处方，ID: {}, 数据: {}", id, prescription);
            prescription.setId(id);
            
            try {
                log.info("调用Service层更新处方...");
                PrescriptionDTO updatedPrescription = prescriptionService.updatePrescription(prescription);
                
                if (updatedPrescription == null) {
                    String msg = String.format("更新处方失败：返回结果为空，ID: %s", id);
                    log.error(msg);
                    return Result.error(msg);
                }
                
                log.info("更新处方成功，ID: {}, 返回数据: {}", id, updatedPrescription);
                return Result.ok(updatedPrescription);
                
            } catch (BusinessException e) {
                String msg = String.format("更新处方业务异常，ID: %s, 错误: %s", id, e.getMessage());
                log.error(msg);
                return Result.error(e.getMessage());
            } catch (Exception e) {
                String msg = String.format("更新处方Service层异常，ID: %s, 错误类型: %s, 错误信息: %s", 
                    id, e.getClass().getName(), e.getMessage());
                log.error(msg, e);
                return Result.error("系统异常，请联系管理员");
            }
            
        } catch (Exception e) {
            String msg = String.format("更新处方Controller层异常，ID: %s, 错误类型: %s, 错误信息: %s", 
                id, e.getClass().getName(), e.getMessage());
            log.error(msg, e);
            return Result.error("系统异常，请联系管理员");
        }
    }

    /**
     * 删除处方
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public Result<Boolean> delete(@PathVariable String id) {
        prescriptionService.deletePrescription(id);
        return Result.ok(true);
    }
} 