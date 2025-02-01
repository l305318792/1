package com.yunchuan.medical.mapper;

import com.yunchuan.medical.entity.DoctorRating;
import com.yunchuan.medical.dto.DoctorRatingDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 医生评分数据访问接口
 * 
 * @author yunchuan
 * @since 1.0.0
 */
@Mapper
public interface DoctorRatingMapper {
    
    /**
     * 新增评分
     */
    void insert(DoctorRating rating);
    
    /**
     * 根据医生ID查询评分列表
     */
    List<DoctorRatingDTO> selectByDoctorId(@Param("doctorId") String doctorId);
    
    /**
     * 根据用户ID查询评分列表
     */
    List<DoctorRatingDTO> selectByUserId(@Param("userId") String userId);
} 