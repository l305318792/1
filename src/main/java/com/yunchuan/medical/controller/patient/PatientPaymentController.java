package com.yunchuan.medical.controller.patient;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.service.PaymentService;
import com.yunchuan.medical.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 患者-支付管理
 */
@Tag(name = "患者-支付管理")
@RestController
@RequestMapping("/patient/payments")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class PatientPaymentController {

    private final PaymentService paymentService;

    public PatientPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * 创建支付订单
     */
    @Operation(summary = "创建支付订单")
    @PostMapping
    public Result<PaymentRecordDTO> createPayment(@RequestBody PaymentRecordDTO payment) {
        String userId = SecurityUtil.getCurrentUserId();
        payment.setUserId(userId);
        return Result.ok(paymentService.createPayment(payment));
    }

    /**
     * 获取我的支付记录
     */
    @Operation(summary = "获取我的支付记录")
    @GetMapping("/my")
    public Result<List<PaymentRecordDTO>> getMyPayments() {
        String userId = SecurityUtil.getCurrentUserId();
        return Result.ok(paymentService.getPaymentsByUserId(userId));
    }

    /**
     * 获取支付详情
     */
    @Operation(summary = "获取支付详情")
    @GetMapping("/{id}")
    public Result<PaymentRecordDTO> getPayment(@PathVariable String id) {
        PaymentRecordDTO payment = paymentService.getPaymentById(id);
        if (payment == null) {
            return Result.error("支付记录不存在");
        }
        // 验证是否是当前用户的支付记录
        String userId = SecurityUtil.getCurrentUserId();
        if (!userId.equals(payment.getUserId())) {
            return Result.error("无权查看此支付记录");
        }
        return Result.ok(payment);
    }

    /**
     * 取消支付
     */
    @Operation(summary = "取消支付")
    @PostMapping("/{id}/cancel")
    public Result<PaymentRecordDTO> cancelPayment(@PathVariable String id) {
        // 验证是否是当前用户的支付记录
        PaymentRecordDTO payment = paymentService.getPaymentById(id);
        if (payment == null) {
            return Result.error("支付记录不存在");
        }
        String userId = SecurityUtil.getCurrentUserId();
        if (!userId.equals(payment.getUserId())) {
            return Result.error("无权取消此支付");
        }
        return Result.ok(paymentService.updatePaymentStatus(id, "CANCELLED"));
    }

    /**
     * 更新支付状态
     */
    @Operation(summary = "更新支付状态")
    @PutMapping("/{id}/status")
    public Result<PaymentRecordDTO> updatePaymentStatus(
            @PathVariable String id,
            @RequestParam String status) {
        // 验证是否是当前用户的支付记录
        PaymentRecordDTO payment = paymentService.getPaymentById(id);
        if (payment == null) {
            return Result.error("支付记录不存在");
        }
        String userId = SecurityUtil.getCurrentUserId();
        if (!userId.equals(payment.getUserId())) {
            return Result.error("无权更新此支付状态");
        }
        return Result.ok(paymentService.updatePaymentStatus(id, status));
    }

    /**
     * 获取我的待支付记录
     */
    @Operation(summary = "获取我的待支付记录")
    @GetMapping("/pending")
    public Result<List<PaymentRecordDTO>> getMyPendingPayments() {
        String userId = SecurityUtil.getCurrentUserId();
        return Result.ok(paymentService.getPaymentsByStatus(userId, "PENDING"));
    }

    /**
     * 获取我的已支付记录
     */
    @Operation(summary = "获取我的已支付记录")
    @GetMapping("/paid")
    public Result<List<PaymentRecordDTO>> getMyPaidPayments() {
        String userId = SecurityUtil.getCurrentUserId();
        return Result.ok(paymentService.getPaymentsByStatus(userId, "SUCCESS"));
    }
} 