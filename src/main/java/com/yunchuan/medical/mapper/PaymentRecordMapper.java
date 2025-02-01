package com.yunchuan.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.entity.PaymentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 支付记录Mapper接口
 */
@Mapper
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {

    /**
     * 根据用户ID查询支付记录列表
     */
    List<PaymentRecord> selectByUserId(@Param("userId") String userId);

    /**
     * 查询所有支付记录
     */
    List<PaymentRecord> selectAll();

    /**
     * 更新支付状态
     */
    int updateStatus(@Param("id") String id, @Param("status") String status);
} 