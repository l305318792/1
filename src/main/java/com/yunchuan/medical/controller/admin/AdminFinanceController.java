package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.PaymentDTO;
import com.yunchuan.medical.dto.RefundDTO;
import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.dto.FinanceStatisticsDTO;
import com.yunchuan.medical.service.PaymentRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;
import com.yunchuan.medical.dto.DailyIncomeDTO;
import com.yunchuan.medical.dto.BusinessTypeIncomeDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunchuan.medical.service.RefundRecordService;

/**
 * 管理员财务管理
 */
@Tag(name = "管理员-财务管理")
@RestController
@RequestMapping("/admin/finance")
@PreAuthorize("hasRole('ADMIN')")
public class AdminFinanceController {

    private final PaymentRecordService paymentRecordService;
    private final RefundRecordService refundRecordService;

    public AdminFinanceController(PaymentRecordService paymentRecordService, RefundRecordService refundRecordService) {
        this.paymentRecordService = paymentRecordService;
        this.refundRecordService = refundRecordService;
    }

    @Operation(summary = "获取支付记录列表")
    @GetMapping("/payments")
    public Result<List<PaymentRecordDTO>> getPaymentList(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.ok(paymentRecordService.getPaymentRecordList(userId));
    }

    @Operation(summary = "获取财务统计数据")
    @GetMapping("/statistics")
    public Result<FinanceStatisticsDTO> getFinanceStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.ok(paymentRecordService.getFinanceStatistics(startDate, endDate));
    }

    @Operation(summary = "获取每日收入统计")
    @GetMapping("/daily-income")
    public Result<List<DailyIncomeDTO>> getDailyIncome(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.ok(paymentRecordService.getDailyIncome(startDate, endDate));
    }

    @Operation(summary = "获取业务类型收入占比")
    @GetMapping("/business-type-statistics")
    public Result<List<BusinessTypeIncomeDTO>> getBusinessTypeStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.ok(paymentRecordService.getBusinessTypeStatistics(startDate, endDate));
    }

    @Operation(summary = "导出支付记录")
    @GetMapping("/export")
    public void exportPaymentRecords(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            HttpServletResponse response) {
        paymentRecordService.exportPaymentRecords(userId, businessType, status, startDate, endDate, response);
    }

    @Operation(summary = "获取退款列表")
    @GetMapping("/refunds")
    public Result<IPage<RefundDTO>> getRefundList(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        Page<RefundDTO> page = new Page<>(current, size);
        return Result.ok(refundRecordService.getRefundPage(page));
    }

    @Operation(summary = "处理退款申请")
    @PostMapping("/refunds/{id}/process")
    public Result<RefundDTO> processRefund(
            @PathVariable String id,
            @RequestParam String action,
            @RequestParam(required = false) String reason) {
        return Result.ok(refundRecordService.processRefund(id, action, reason));
    }

    @Operation(summary = "创建退款申请")
    @PostMapping("/refunds")
    public Result<RefundDTO> createRefund(@RequestBody RefundDTO refundDTO) {
        return Result.ok(refundRecordService.createRefund(refundDTO));
    }

    @Operation(summary = "创建支付记录")
    @PostMapping("/payments")
    public Result<PaymentRecordDTO> createPayment(@RequestBody PaymentRecordDTO paymentRecord) {
        return Result.ok(paymentRecordService.createPayment(paymentRecord));
    }
} 