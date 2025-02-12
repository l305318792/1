package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付管理
 */
@Tag(name = "支付管理")
@RestController
@RequestMapping("/payment")
@CrossOrigin // 允许跨域访问
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * 创建支付
     */
    @Operation(summary = "创建支付")
    @PostMapping
    public Result<PaymentRecordDTO> createPayment(@RequestBody @Valid PaymentRecordDTO payment) {
        return Result.ok(paymentService.createPayment(payment));
    }

    /**
     * 获取支付详情
     */
    @Operation(summary = "获取支付详情")
    @GetMapping("/{id}")
    public Result<PaymentRecordDTO> getPayment(@PathVariable String id) {
        return Result.ok(paymentService.getPaymentById(id));
    }

    /**
     * 更新支付状态
     */
    @Operation(summary = "更新支付状态")
    @PutMapping("/{id}/status")
    public Result<PaymentRecordDTO> updatePaymentStatus(
            @PathVariable String id,
            @RequestParam String status) {
        return Result.ok(paymentService.updatePaymentStatus(id, status));
    }

    /**
     * 查询用户支付记录
     */
    @Operation(summary = "查询用户支付记录")
    @GetMapping("/user/{userId}")
    public Result<List<PaymentRecordDTO>> getUserPayments(@PathVariable String userId) {
        return Result.ok(paymentService.getPaymentsByUserId(userId));
    }

    /**
     * 查询所有支付记录
     */
    @Operation(summary = "查询所有支付记录")
    @GetMapping("/list")
    public Result<List<PaymentRecordDTO>> getAllPayments() {
        return Result.ok(paymentService.getAllPayments());
    }

    /**
     * 获取待支付记录列表
     */
    @Operation(summary = "获取待支付记录列表")
    @GetMapping("/pending")
    public Result<List<PaymentRecordDTO>> getPendingPayments(@RequestParam(required = false) String userId) {
        return Result.ok(paymentService.getPaymentsByStatus(userId, "PENDING"));
    }

    /**
     * 获取已支付记录列表
     */
    @Operation(summary = "获取已支付记录列表")
    @GetMapping("/paid")
    public Result<List<PaymentRecordDTO>> getPaidPayments(@RequestParam(required = false) String userId) {
        return Result.ok(paymentService.getPaymentsByStatus(userId, "SUCCESS"));
    }
} 