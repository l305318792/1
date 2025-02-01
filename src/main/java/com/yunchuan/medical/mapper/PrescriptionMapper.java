package com.yunchuan.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.entity.Prescription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 处方数据访问接口
 */
@Mapper
public interface PrescriptionMapper extends BaseMapper<Prescription> {

    /**
     * 根据ID查询处方
     */
    Prescription selectById(@Param("id") String id);

    /**
     * 根据病历ID查询处方列表
     */
    List<Prescription> selectByRecordId(@Param("recordId") String recordId);

    /**
     * 查询所有处方
     */
    List<Prescription> selectAll();

    /**
     * 插入处方
     */
    int insert(Prescription prescription);

    /**
     * 更新处方
     */
    int update(Prescription prescription);

    /**
     * 删除处方
     */
    int deleteById(@Param("id") String id);

    /**
     * 根据问诊ID查询处方列表
     */
    List<Prescription> selectByConsultationId(@Param("consultationId") String consultationId);

    /**
     * 查询所有处方
     */
    List<Prescription> selectList();
} 