package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.service.PaymentRecordService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * 支付记录管理
 */
@Tag(name = "支付记录管理")
@RestController
@RequestMapping("/payment-record")
public class PaymentRecordController {

    private final PaymentRecordService paymentRecordService;

    public PaymentRecordController(PaymentRecordService paymentRecordService) {
        this.paymentRecordService = paymentRecordService;
    }

    /**
     * 获取支付记录列表
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'DOCTOR', 'ADMIN')")
    public Result<List<PaymentRecordDTO>> list(@RequestParam(required = false) String userId) {
        return Result.ok(paymentRecordService.getPaymentRecordList(userId));
    }

    /**
     * 获取支付记录详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'DOCTOR', 'ADMIN')")
    public Result<PaymentRecordDTO> get(@PathVariable String id) {
        return Result.ok(paymentRecordService.getPaymentRecordById(id));
    }

    /**
     * 创建支付记录
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'DOCTOR', 'ADMIN')")
    public Result<PaymentRecordDTO> add(@RequestBody @Valid PaymentRecordDTO paymentRecord) {
        return Result.ok(paymentRecordService.createPaymentRecord(paymentRecord));
    }

    /**
     * 更新支付记录
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PaymentRecordDTO> update(@PathVariable String id, @RequestBody @Valid PaymentRecordDTO paymentRecord) {
        paymentRecord.setId(id);
        return Result.ok(paymentRecordService.updatePaymentRecord(paymentRecord));
    }

    /**
     * 删除支付记录
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> delete(@PathVariable String id) {
        paymentRecordService.deletePaymentRecord(id);
        return Result.ok(true);
    }
} 