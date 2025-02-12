package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.service.PaymentRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付记录控制器
 */
@Tag(name = "支付记录")
@RestController
@RequestMapping("/payment-record")
public class PaymentRecordController {

    private final PaymentRecordService paymentRecordService;

    public PaymentRecordController(PaymentRecordService paymentRecordService) {
        this.paymentRecordService = paymentRecordService;
    }

    /**
     * 创建支付记录
     */
    @Operation(summary = "创建支付记录")
    @PostMapping
    public Result<PaymentRecordDTO> createPayment(@RequestBody @Valid PaymentRecordDTO paymentRecord) {
        return Result.ok(paymentRecordService.createPayment(paymentRecord));
    }

    /**
     * 获取支付记录列表
     */
    @Operation(summary = "获取支付记录列表")
    @GetMapping("/list")
    public Result<List<PaymentRecordDTO>> getPaymentRecordList(@RequestParam(required = false) String userId) {
        return Result.ok(paymentRecordService.getPaymentRecordList(userId));
    }

    /**
     * 获取支付记录详情
     */
    @Operation(summary = "获取支付记录详情")
    @GetMapping("/{id}")
    public Result<PaymentRecordDTO> getPaymentRecord(@PathVariable String id) {
        return Result.ok(paymentRecordService.getPaymentRecordById(id));
    }

    /**
     * 更新支付记录
     */
    @Operation(summary = "更新支付记录")
    @PutMapping("/{id}")
    public Result<PaymentRecordDTO> updatePaymentRecord(@PathVariable String id, @RequestBody PaymentRecordDTO paymentRecord) {
        paymentRecord.setId(id);
        return Result.ok(paymentRecordService.updatePaymentRecord(paymentRecord));
    }

    /**
     * 删除支付记录
     */
    @Operation(summary = "删除支付记录")
    @DeleteMapping("/{id}")
    public Result<Void> deletePaymentRecord(@PathVariable String id) {
        paymentRecordService.deletePaymentRecord(id);
        return Result.ok();
    }
} 