package com.yunchuan.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.entity.RefundRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 退款记录数据访问层
 */
@Mapper
public interface RefundRecordMapper extends BaseMapper<RefundRecord> {
    /**
     * 根据用户ID查询退款记录
     */
    List<RefundRecord> selectByUserId(@Param("userId") String userId);

    /**
     * 根据状态查询退款记录
     */
    List<RefundRecord> selectByStatus(@Param("status") String status);

    /**
     * 根据时间范围查询退款记录
     */
    List<RefundRecord> selectByTimeRange(
        @Param("startTime") LocalDateTime startTime, 
        @Param("endTime") LocalDateTime endTime
    );
} 