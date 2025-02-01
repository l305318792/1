package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.MedicalRecordDTO;
import java.util.List;

/**
 * 病历服务接口
 */
public interface MedicalRecordService {
    
    /**
     * 获取病历列表
     * @param patientId 患者ID
     * @return 病历列表
     */
    List<MedicalRecordDTO> getMedicalRecordList(String patientId);
    
    /**
     * 根据ID获取病历
     * @param id 病历ID
     * @return 病历信息
     */
    MedicalRecordDTO getMedicalRecordById(String id);
    
    /**
     * 创建病历
     * @param medicalRecord 病历信息
     * @return 创建后的病历
     */
    MedicalRecordDTO createMedicalRecord(MedicalRecordDTO medicalRecord);
    
    /**
     * 更新病历
     * @param medicalRecord 病历信息
     * @return 更新后的病历
     */
    MedicalRecordDTO updateMedicalRecord(MedicalRecordDTO medicalRecord);
    
    /**
     * 删除病历
     * @param id 病历ID
     */
    void deleteMedicalRecord(String id);
} 