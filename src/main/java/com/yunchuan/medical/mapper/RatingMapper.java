package com.yunchuan.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.entity.Rating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * <p>
 * 评价表 Mapper 接口
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Mapper
public interface RatingMapper extends BaseMapper<Rating> {
    
    /**
     * 根据医生ID查询评价列表
     */
    @Select("SELECT * FROM doctor_rating WHERE doctor_id = #{doctorId} AND status = 'NORMAL' ORDER BY create_time DESC")
    List<Rating> selectByDoctorId(@Param("doctorId") String doctorId);
    
    /**
     * 根据用户ID查询评价列表
     */
    @Select("SELECT * FROM doctor_rating WHERE user_id = #{userId} AND status = 'NORMAL' ORDER BY create_time DESC")
    List<Rating> selectByUserId(@Param("userId") String userId);
    
    /**
     * 根据预约ID查询评价
     */
    @Select("SELECT * FROM doctor_rating WHERE appointment_id = #{appointmentId} AND status = 'NORMAL' LIMIT 1")
    Rating selectByAppointmentId(@Param("appointmentId") String appointmentId);

    /**
     * 根据问诊ID查询评价
     */
    @Select("SELECT * FROM doctor_rating WHERE consultation_id = #{consultationId} AND status = 'NORMAL' LIMIT 1")
    Rating selectByConsultationId(@Param("consultationId") String consultationId);

    /**
     * 检查问诊是否已评价
     */
    Boolean existsByConsultationId(@Param("consultationId") String consultationId);
}
