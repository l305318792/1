package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.PaymentDTO;
import com.yunchuan.medical.dto.RefundDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员财务管理
 */
@Tag(name = "管理员-财务管理")
@RestController
@RequestMapping("/admin/finance")
public class AdminFinanceController {

    @Operation(summary = "获取支付记录列表")
    @GetMapping("/payments")
    public Result<Object> getPaymentList() {
        // 模拟支付记录数据
        PaymentDTO payment1 = PaymentDTO.builder()
                .id("1")
                .userId("u1")
                .userName("张三")
                .orderType("APPOINTMENT")  // 预约挂号费
                .orderId("1")
                .amount(new BigDecimal("50.00"))
                .status("SUCCESS")
                .paymentMethod("WECHAT")
                .createTime(LocalDateTime.now())
                .payTime(LocalDateTime.now())
                .build();
                
        PaymentDTO payment2 = PaymentDTO.builder()
                .id("2")
                .userId("u2")
                .userName("李四")
                .orderType("PRESCRIPTION")  // 处方药费
                .orderId("2")
                .amount(new BigDecimal("128.50"))
                .status("SUCCESS")
                .paymentMethod("ALIPAY")
                .createTime(LocalDateTime.now())
                .payTime(LocalDateTime.now())
                .build();
                
        return Result.ok(new Object() {
            public final List<PaymentDTO> list = Arrays.asList(payment1, payment2);
            public final int total = 2;
        });
    }

    @Operation(summary = "获取财务统计")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        // 模拟统计数据
        Map<String, Object> statistics = new HashMap<>();
        
        // 总收入统计
        statistics.put("totalIncome", new BigDecimal("10000.00"));
        statistics.put("todayIncome", new BigDecimal("1280.50"));
        
        // 支付方式统计
        Map<String, BigDecimal> paymentMethodStats = new HashMap<>();
        paymentMethodStats.put("WECHAT", new BigDecimal("6000.00"));
        paymentMethodStats.put("ALIPAY", new BigDecimal("4000.00"));
        statistics.put("paymentMethodStats", paymentMethodStats);
        
        // 收入类型统计
        Map<String, BigDecimal> incomeTypeStats = new HashMap<>();
        incomeTypeStats.put("APPOINTMENT", new BigDecimal("3000.00"));  // 挂号费
        incomeTypeStats.put("PRESCRIPTION", new BigDecimal("7000.00")); // 处方费
        statistics.put("incomeTypeStats", incomeTypeStats);
        
        return Result.ok(statistics);
    }

    @Operation(summary = "获取退款列表")
    @GetMapping("/refunds")
    public Result<Object> getRefundList() {
        // 模拟退款记录数据
        RefundDTO refund1 = RefundDTO.builder()
                .id("1")
                .paymentId("1")
                .userId("u1")
                .userName("张三")
                .orderType("APPOINTMENT")
                .orderId("1")
                .amount(new BigDecimal("50.00"))
                .reason("患者取消预约")
                .status("PENDING")
                .createTime(LocalDateTime.now())
                .build();
                
        RefundDTO refund2 = RefundDTO.builder()
                .id("2")
                .paymentId("2")
                .userId("u2")
                .userName("李四")
                .orderType("PRESCRIPTION")
                .orderId("2")
                .amount(new BigDecimal("128.50"))
                .reason("药品缺货")
                .status("COMPLETED")
                .createTime(LocalDateTime.now())
                .refundTime(LocalDateTime.now())
                .build();
                
        return Result.ok(new Object() {
            public final List<RefundDTO> list = Arrays.asList(refund1, refund2);
            public final int total = 2;
        });
    }

    @Operation(summary = "处理退款申请")
    @PostMapping("/refunds/{id}/process")
    public Result<RefundDTO> processRefund(
            @PathVariable String id,
            @RequestParam String action,  // APPROVE-同意, REJECT-拒绝
            @RequestParam(required = false) String reason) {
        
        RefundDTO refund = RefundDTO.builder()
                .id(id)
                .paymentId("1")
                .userId("u1")
                .userName("张三")
                .orderType("APPOINTMENT")
                .orderId("1")
                .amount(new BigDecimal("50.00"))
                .reason("患者取消预约")
                .status(action.equals("APPROVE") ? "COMPLETED" : "REJECTED")
                .rejectReason(action.equals("REJECT") ? reason : null)
                .createTime(LocalDateTime.now())
                .refundTime(action.equals("APPROVE") ? LocalDateTime.now() : null)
                .build();
                
        return Result.ok(refund);
    }
} 