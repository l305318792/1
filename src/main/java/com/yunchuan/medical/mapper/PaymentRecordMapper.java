package com.yunchuan.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.entity.PaymentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付记录Mapper接口
 */
@Mapper
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {

    /**
     * 根据用户ID查询支付记录列表
     */
    @Select("SELECT * FROM payment_record WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<PaymentRecord> selectByUserId(@Param("userId") String userId);

    /**
     * 查询所有支付记录
     */
    @Select("SELECT * FROM payment_record ORDER BY create_time DESC")
    List<PaymentRecord> selectAll();

    /**
     * 更新支付状态
     */
    @Select("UPDATE payment_record SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") String id, @Param("status") String status);

    /**
     * 根据时间范围查询支付记录
     */
    @Select("SELECT * FROM payment_record WHERE payment_time BETWEEN #{startTime} AND #{endTime} ORDER BY payment_time DESC")
    List<PaymentRecord> selectByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 导出支付记录
     */
    @Select("<script>" +
            "SELECT * FROM payment_record WHERE 1=1" +
            "<if test='userId != null'> AND user_id = #{userId}</if>" +
            "<if test='businessType != null'> AND business_type = #{businessType}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            "<if test='startTime != null'> AND payment_time &gt;= #{startTime}</if>" +
            "<if test='endTime != null'> AND payment_time &lt;= #{endTime}</if>" +
            " ORDER BY payment_time DESC" +
            "</script>")
    List<PaymentRecord> selectForExport(@Param("userId") String userId,
                                      @Param("businessType") String businessType,
                                      @Param("status") String status,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);
} 