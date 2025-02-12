package com.yunchuan.medical.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunchuan.medical.dto.RefundDTO;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 退款记录服务接口
 */
public interface RefundRecordService {
    
    /**
     * 分页查询退款记录
     */
    IPage<RefundDTO> getRefundPage(Page<RefundDTO> page);
    
    /**
     * 创建退款申请
     */
    RefundDTO createRefund(RefundDTO refundDTO);
    
    /**
     * 处理退款申请
     */
    RefundDTO processRefund(String id, String action, String reason);
    
    /**
     * 根据ID获取退款记录
     */
    RefundDTO getRefundById(String id);
    
    /**
     * 根据用户ID获取退款记录
     */
    List<RefundDTO> getRefundsByUserId(String userId);
    
    /**
     * 根据状态获取退款记录
     */
    List<RefundDTO> getRefundsByStatus(String status);
    
    /**
     * 根据时间范围获取退款记录
     */
    List<RefundDTO> getRefundsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
} 