package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunchuan.medical.constant.Constants;
import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.dto.FinanceStatisticsDTO;
import com.yunchuan.medical.dto.DailyIncomeDTO;
import com.yunchuan.medical.dto.BusinessTypeIncomeDTO;
import com.yunchuan.medical.entity.PaymentRecord;
import com.yunchuan.medical.entity.RefundRecord;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.mapper.PaymentRecordMapper;
import com.yunchuan.medical.mapper.RefundRecordMapper;
import com.yunchuan.medical.service.PaymentRecordService;
import com.yunchuan.medical.util.SecurityUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 支付记录服务实现类
 */
@Service
public class PaymentRecordServiceImpl implements PaymentRecordService {

    private final PaymentRecordMapper paymentRecordMapper;
    private final RefundRecordMapper refundRecordMapper;
    private static final Logger logger = LoggerFactory.getLogger(PaymentRecordServiceImpl.class);

    public PaymentRecordServiceImpl(PaymentRecordMapper paymentRecordMapper, RefundRecordMapper refundRecordMapper) {
        this.paymentRecordMapper = paymentRecordMapper;
        this.refundRecordMapper = refundRecordMapper;
    }

    @Override
    public List<PaymentRecordDTO> getPaymentRecordList(String userId) {
        List<PaymentRecord> records;
        if (userId != null) {
            records = paymentRecordMapper.selectByUserId(userId);
        } else {
            records = paymentRecordMapper.selectAll();
        }
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentRecordDTO getPaymentRecordById(String id) {
        PaymentRecord record = paymentRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }
        return convertToDTO(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentRecordDTO createPayment(PaymentRecordDTO paymentRecord) {
        // 1. 参数校验
        if (paymentRecord.getAmount() == null || paymentRecord.getBusinessType() == null) {
            throw new BusinessException("金额和业务类型不能为空");
        }
        
        // 2. 创建支付记录
        PaymentRecord record = new PaymentRecord();
        BeanUtils.copyProperties(paymentRecord, record);
        
        // 3. 设置基本信息
        record.setId(UUID.randomUUID().toString().replace("-", ""));
        record.setUserId(SecurityUtil.getCurrentUserId());
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        if (record.getStatus() == null) {
            record.setStatus("PENDING");
        }
        
        // 4. 保存记录
        paymentRecordMapper.insert(record);
        
        // 5. 返回结果
        PaymentRecordDTO result = new PaymentRecordDTO();
        BeanUtils.copyProperties(record, result);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentRecordDTO updatePaymentRecord(PaymentRecordDTO recordDTO) {
        PaymentRecord record = paymentRecordMapper.selectById(recordDTO.getId());
        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }
        
        // 验证状态值
        if (recordDTO.getStatus() != null && 
            !Constants.PAYMENT_PENDING.equals(recordDTO.getStatus()) && 
            !Constants.PAYMENT_SUCCESS.equals(recordDTO.getStatus()) && 
            !Constants.PAYMENT_FAILED.equals(recordDTO.getStatus()) &&
            !Constants.PAYMENT_REFUNDED.equals(recordDTO.getStatus())) {
            throw new BusinessException("无效的状态值");
        }
        
        BeanUtils.copyProperties(recordDTO, record);
        record.setUpdateTime(LocalDateTime.now());
        
        if (paymentRecordMapper.updateById(record) <= 0) {
            throw new BusinessException("更新支付记录失败");
        }
        
        return convertToDTO(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePaymentRecord(String id) {
        PaymentRecord record = paymentRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("支付记录不存在");
        }
        
        if (paymentRecordMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除支付记录失败");
        }
    }

    /**
     * 获取财务统计数据
     */
    @Override
    public FinanceStatisticsDTO getFinanceStatistics(LocalDate startDate, LocalDate endDate) {
        logger.info("开始获取财务统计数据, startDate: {}, endDate: {}", startDate, endDate);
        
        // 设置查询时间范围
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        
        // 查询成功支付且未退款的记录
        QueryWrapper<PaymentRecord> paymentWrapper = new QueryWrapper<>();
        paymentWrapper.eq("status", "SUCCESS");
        if (startDateTime != null && endDateTime != null) {
            paymentWrapper.between("payment_time", startDateTime, endDateTime);
        }
        List<PaymentRecord> successPayments = paymentRecordMapper.selectList(paymentWrapper);
        
        // 查询已退款的支付记录
        QueryWrapper<PaymentRecord> refundedWrapper = new QueryWrapper<>();
        refundedWrapper.eq("status", "REFUNDED");
        if (startDateTime != null && endDateTime != null) {
            refundedWrapper.between("payment_time", startDateTime, endDateTime);
        }
        List<PaymentRecord> refundedPayments = paymentRecordMapper.selectList(refundedWrapper);
        
        // 查询已批准的退款记录
        QueryWrapper<RefundRecord> refundWrapper = new QueryWrapper<>();
        refundWrapper.eq("status", "APPROVED");
        if (startDateTime != null && endDateTime != null) {
            refundWrapper.between("refund_time", startDateTime, endDateTime);
        }
        List<RefundRecord> refundRecords = refundRecordMapper.selectList(refundWrapper);
        
        // 计算总收入（包括已退款的记录）
        BigDecimal totalIncome = Stream.concat(successPayments.stream(), refundedPayments.stream())
                .map(PaymentRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算总退款
        BigDecimal totalRefund = refundRecords.stream()
                .map(RefundRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算实际收入（未退款的支付记录总额）
        BigDecimal actualIncome = successPayments.stream()
                .map(PaymentRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算今日收入
        LocalDate today = LocalDate.now();
        BigDecimal todayIncome = successPayments.stream()
                .filter(record -> record.getPaymentTime() != null && 
                        record.getPaymentTime().toLocalDate().equals(today))
                .map(PaymentRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算本周收入
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = today.with(DayOfWeek.SUNDAY);
        BigDecimal weekIncome = successPayments.stream()
                .filter(record -> record.getPaymentTime() != null && 
                        !record.getPaymentTime().toLocalDate().isBefore(weekStart) && 
                        !record.getPaymentTime().toLocalDate().isAfter(weekEnd))
                .map(PaymentRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算本月收入
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());
        BigDecimal monthIncome = successPayments.stream()
                .filter(record -> record.getPaymentTime() != null && 
                        !record.getPaymentTime().toLocalDate().isBefore(monthStart) && 
                        !record.getPaymentTime().toLocalDate().isAfter(monthEnd))
                .map(PaymentRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 按支付方式统计（只统计未退款的记录）
        Map<String, BigDecimal> paymentMethodStats = successPayments.stream()
                .collect(Collectors.groupingBy(
                    PaymentRecord::getPaymentMethod,
                    Collectors.mapping(
                        PaymentRecord::getAmount,
                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                    )
                ));
        
        // 按业务类型统计（只统计未退款的记录）
        Map<String, BigDecimal> businessTypeStats = successPayments.stream()
                .collect(Collectors.groupingBy(
                    PaymentRecord::getBusinessType,
                    Collectors.mapping(
                        PaymentRecord::getAmount,
                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                    )
                ));
        
        logger.info("财务统计完成 - 总收入: {}, 总退款: {}, 实际收入: {}", 
            totalIncome, totalRefund, actualIncome);
        
        return FinanceStatisticsDTO.builder()
                .totalIncome(totalIncome)
                .todayIncome(todayIncome)
                .weekIncome(weekIncome)
                .monthIncome(monthIncome)
                .paymentMethodStats(paymentMethodStats)
                .businessTypeStats(businessTypeStats)
                .totalRefund(totalRefund)
                .actualIncome(actualIncome)
                .build();
    }

    /**
     * 获取每日收入统计
     */
    @Override
    public List<DailyIncomeDTO> getDailyIncome(LocalDate startDate, LocalDate endDate) {
        logger.info("开始获取每日收入统计, 开始日期: {}, 结束日期: {}", startDate, endDate);
        
        // 转换为LocalDateTime，设置时间范围
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
        
        // 查询支付记录
        LambdaQueryWrapper<PaymentRecord> paymentWrapper = new LambdaQueryWrapper<>();
        paymentWrapper.ge(PaymentRecord::getPaymentTime, startDateTime)
                     .lt(PaymentRecord::getPaymentTime, endDateTime)
                     .eq(PaymentRecord::getStatus, "SUCCESS");
        List<PaymentRecord> paymentRecords = paymentRecordMapper.selectList(paymentWrapper);
        
        // 查询退款记录
        LambdaQueryWrapper<RefundRecord> refundWrapper = new LambdaQueryWrapper<>();
        refundWrapper.ge(RefundRecord::getRefundTime, startDateTime)
                    .lt(RefundRecord::getRefundTime, endDateTime)
                    .eq(RefundRecord::getStatus, "APPROVED");
        List<RefundRecord> refundRecords = refundRecordMapper.selectList(refundWrapper);
        
        // 按日期分组统计
        Map<LocalDate, List<PaymentRecord>> dailyPayments = paymentRecords.stream()
                .collect(Collectors.groupingBy(record -> 
                    record.getPaymentTime().toLocalDate()));
        
        Map<LocalDate, List<RefundRecord>> dailyRefunds = refundRecords.stream()
                .collect(Collectors.groupingBy(record -> 
                    record.getRefundTime().toLocalDate()));
        
        // 生成日期范围内的每一天的统计数据
        List<DailyIncomeDTO> result = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DailyIncomeDTO dto = new DailyIncomeDTO();
            dto.setDate(date);
            
            // 获取当天的支付记录
            List<PaymentRecord> dayPayments = dailyPayments.getOrDefault(date, Collections.emptyList());
            
            // 计算各类收入
            BigDecimal appointmentIncome = calculateIncomeByType(dayPayments, "APPOINTMENT");
            BigDecimal consultationIncome = calculateIncomeByType(dayPayments, "CONSULTATION");
            BigDecimal prescriptionIncome = calculateIncomeByType(dayPayments, "PRESCRIPTION");
            
            // 计算退款金额
            BigDecimal refundAmount = dailyRefunds.getOrDefault(date, Collections.emptyList())
                    .stream()
                    .map(RefundRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // 设置统计数据
            dto.setAppointmentIncome(appointmentIncome);
            dto.setConsultationIncome(consultationIncome);
            dto.setPrescriptionIncome(prescriptionIncome);
            dto.setTotalIncome(appointmentIncome.add(consultationIncome).add(prescriptionIncome));
            dto.setRefundAmount(refundAmount);
            dto.setActualIncome(dto.getTotalIncome().subtract(refundAmount));
            dto.setTransactionCount(dayPayments.size());
            
            result.add(dto);
        }
        
        logger.info("每日收入统计完成，统计天数: {}", result.size());
        return result;
    }

    private BigDecimal calculateIncomeByType(List<PaymentRecord> records, String businessType) {
        return records.stream()
                .filter(record -> businessType.equals(record.getBusinessType()))
                .map(PaymentRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 获取业务类型收入统计
     */
    @Override
    public List<BusinessTypeIncomeDTO> getBusinessTypeStatistics(LocalDate startDate, LocalDate endDate) {
        logger.info("开始获取业务类型收入统计, 开始日期: {}, 结束日期: {}", startDate, endDate);
        
        // 转换为LocalDateTime
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.plusDays(1).atStartOfDay() : null;
        
        // 查询所有支付记录（包括已退款的）
        LambdaQueryWrapper<PaymentRecord> paymentWrapper = new LambdaQueryWrapper<>();
        if (startDateTime != null) {
            paymentWrapper.ge(PaymentRecord::getPaymentTime, startDateTime);
        }
        if (endDateTime != null) {
            paymentWrapper.lt(PaymentRecord::getPaymentTime, endDateTime);
        }
        
        List<PaymentRecord> paymentRecords = paymentRecordMapper.selectList(paymentWrapper);
        
        // 查询退款记录
        LambdaQueryWrapper<RefundRecord> refundWrapper = new LambdaQueryWrapper<>();
        refundWrapper.eq(RefundRecord::getStatus, "APPROVED");
        if (startDateTime != null) {
            refundWrapper.ge(RefundRecord::getRefundTime, startDateTime);
        }
        if (endDateTime != null) {
            refundWrapper.lt(RefundRecord::getRefundTime, endDateTime);
        }
        
        List<RefundRecord> refundRecords = refundRecordMapper.selectList(refundWrapper);
        
        // 按业务类型分组统计总收入（包括已退款的）
        Map<String, BigDecimal> incomeByType = paymentRecords.stream()
                .collect(Collectors.groupingBy(
                    PaymentRecord::getBusinessType,
                    Collectors.reducing(
                        BigDecimal.ZERO,
                        PaymentRecord::getAmount,
                        BigDecimal::add
                    )
                ));
        
        // 按业务类型分组统计退款
        Map<String, BigDecimal> refundByType = refundRecords.stream()
                .collect(Collectors.groupingBy(
                    RefundRecord::getBusinessType,
                    Collectors.reducing(
                        BigDecimal.ZERO,
                        RefundRecord::getAmount,
                        BigDecimal::add
                    )
                ));
        
        // 按业务类型统计交易笔数
        Map<String, Long> transactionCountByType = paymentRecords.stream()
                .collect(Collectors.groupingBy(
                    PaymentRecord::getBusinessType,
                    Collectors.counting()
                ));
        
        // 计算总收入（用于计算占比）
        BigDecimal totalIncome = incomeByType.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 生成统计结果
        List<BusinessTypeIncomeDTO> result = new ArrayList<>();
        
        // 处理所有业务类型
        Set<String> allTypes = new HashSet<>();
        allTypes.addAll(incomeByType.keySet());
        allTypes.addAll(refundByType.keySet());
        
        for (String type : allTypes) {
            BusinessTypeIncomeDTO dto = new BusinessTypeIncomeDTO();
            dto.setBusinessType(type);
            dto.setBusinessTypeName(getBusinessTypeName(type));
            
            BigDecimal income = incomeByType.getOrDefault(type, BigDecimal.ZERO);
            BigDecimal refund = refundByType.getOrDefault(type, BigDecimal.ZERO);
            BigDecimal actualIncome = income.subtract(refund);
            
            dto.setAmount(actualIncome);
            
            // 计算占比（使用总收入的绝对值）
            if (totalIncome.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal absTotal = totalIncome.abs();
                BigDecimal percentage = actualIncome.abs()
                    .divide(absTotal, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
                dto.setPercentage(percentage);
            } else {
                dto.setPercentage(BigDecimal.ZERO);
            }
            
            // 设置交易笔数
            dto.setTransactionCount(transactionCountByType.getOrDefault(type, 0L).intValue());
            
            result.add(dto);
        }
        
        // 按金额绝对值降序排序
        result.sort((a, b) -> b.getAmount().abs().compareTo(a.getAmount().abs()));
        
        logger.info("业务类型收入统计完成，统计类型数: {}", result.size());
        return result;
    }

    /**
     * 获取业务类型名称
     */
    private String getBusinessTypeName(String businessType) {
        switch (businessType) {
            case "APPOINTMENT":
                return "预约挂号";
            case "CONSULTATION":
                return "在线问诊";
            case "PRESCRIPTION":
                return "处方开具";
            default:
                return businessType;
        }
    }

    @Override
    public void exportPaymentRecords(String userId, String businessType, String status,
                                   LocalDate startDate, LocalDate endDate,
                                   HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("支付记录");
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"支付ID", "用户ID", "业务类型", "金额", "支付方式", "支付时间", "状态"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // 查询数据
            List<PaymentRecord> records = paymentRecordMapper.selectForExport(
                    userId, businessType, status, 
                    startDate != null ? startDate.atStartOfDay() : null,
                    endDate != null ? endDate.atTime(LocalTime.MAX) : null
            );
            
            // 填充数据
            int rowNum = 1;
            for (PaymentRecord record : records) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getId());
                row.createCell(1).setCellValue(record.getUserId());
                row.createCell(2).setCellValue(record.getBusinessType());
                row.createCell(3).setCellValue(record.getAmount().doubleValue());
                row.createCell(4).setCellValue(record.getPaymentMethod());
                row.createCell(5).setCellValue(record.getPaymentTime().toString());
                row.createCell(6).setCellValue(record.getStatus());
            }
            
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=payment_records.xlsx");
            
            // 写入响应
            workbook.write(response.getOutputStream());
            
        } catch (IOException e) {
            throw new BusinessException("导出支付记录失败：" + e.getMessage());
        }
    }

    private PaymentRecordDTO convertToDTO(PaymentRecord record) {
        if (record == null) {
            return null;
        }
        PaymentRecordDTO dto = new PaymentRecordDTO();
        BeanUtils.copyProperties(record, dto);
        return dto;
    }
} 