package com.yunchuan.medical.mapper;

import com.yunchuan.medical.entity.Appointment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.dto.AppointmentDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;

/**
 * <p>
 * 预约表 Mapper 接口
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {

    /**
     * 根据ID查询预约详情
     *
     * @param id 预约ID
     * @return 预约详情DTO
     */
    AppointmentDetailDTO selectDetailById(@Param("id") String id);

    /**
     * 检查指定用户在指定时间是否已有预约
     * 通过检查时间段重叠来判断冲突（前后30分钟内）
     */
    @Select("SELECT COUNT(*) FROM appointment WHERE user_id = #{userId} " +
            "AND visit_time >= #{visitTime} - INTERVAL '30' MINUTE " +
            "AND visit_time <= #{visitTime} + INTERVAL '30' MINUTE " +
            "AND status NOT IN ('CANCELLED', 'COMPLETED')")
    int checkTimeConflict(@Param("userId") String userId, @Param("visitTime") LocalDateTime visitTime);

    /**
     * 获取指定时间范围内的最大序号
     */
    @Select("SELECT COALESCE(MAX(sequence_number), 0) FROM appointment WHERE create_time >= #{startTime} AND create_time < #{endTime}")
    Integer getMaxSequenceNumber(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
