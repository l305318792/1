package com.yunchuan.medical.mapper;

import com.yunchuan.medical.entity.Doctor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * <p>
 * 医生表 Mapper 接口
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {

    /**
     * 更新医生评分
     */
    @Update("UPDATE doctor SET rating = #{rating}, rating_count = rating_count + 1 WHERE id = #{id}")
    boolean updateRating(String id, BigDecimal rating);
}
