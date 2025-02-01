package com.yunchuan.medical.mapper;

import com.yunchuan.medical.entity.Appointment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.dto.AppointmentDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
