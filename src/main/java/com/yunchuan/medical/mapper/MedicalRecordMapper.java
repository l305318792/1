package com.yunchuan.medical.mapper;

import com.yunchuan.medical.entity.MedicalRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 病历数据访问接口
 */
@Mapper
public interface MedicalRecordMapper {

    /**
     * 根据ID查询病历
     */
    MedicalRecord selectById(@Param("id") String id);

    /**
     * 根据患者ID查询病历列表
     */
    List<MedicalRecord> selectByPatientId(@Param("patientId") String patientId);

    /**
     * 查询所有病历
     */
    List<MedicalRecord> selectAll();

    /**
     * 插入病历
     */
    int insert(MedicalRecord record);

    /**
     * 更新病历
     */
    int update(MedicalRecord record);

    /**
     * 删除病历
     */
    int deleteById(@Param("id") String id);
} 