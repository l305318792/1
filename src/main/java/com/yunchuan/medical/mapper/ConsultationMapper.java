package com.yunchuan.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.entity.Consultation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 问诊记录数据访问接口
 */
@Mapper
public interface ConsultationMapper extends BaseMapper<Consultation> {
    /**
     * 根据医生ID查询问诊记录
     */
    List<Consultation> selectByDoctorId(@Param("doctorId") String doctorId);

    /**
     * 根据用户ID查询问诊记录
     */
    @Select("SELECT * FROM consultation WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Consultation> selectByUserId(String userId);

    /**
     * 根据状态查询问诊记录
     */
    List<Consultation> selectByStatus(@Param("status") String status);

    /**
     * 根据医生ID和状态查询问诊记录
     */
    @Select("SELECT * FROM consultation WHERE doctor_id = #{doctorId} AND status = #{status} ORDER BY create_time DESC")
    List<Consultation> selectByDoctorIdAndStatus(@Param("doctorId") String doctorId, @Param("status") String status);
} 