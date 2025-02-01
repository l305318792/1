package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.PrescriptionDTO;
import java.util.List;

/**
 * 处方服务接口
 */
public interface PrescriptionService {
    
    /**
     * 获取处方列表
     * @param recordId 病历ID
     * @return 处方列表
     */
    List<PrescriptionDTO> getPrescriptionList(String recordId);
    
    /**
     * 根据ID获取处方
     * @param id 处方ID
     * @return 处方信息
     */
    PrescriptionDTO getPrescriptionById(String id);
    
    /**
     * 创建处方
     * @param prescription 处方信息
     * @return 创建后的处方
     */
    PrescriptionDTO createPrescription(PrescriptionDTO prescription);
    
    /**
     * 更新处方
     * @param prescription 处方信息
     * @return 更新后的处方
     */
    PrescriptionDTO updatePrescription(PrescriptionDTO prescription);
    
    /**
     * 删除处方
     * @param id 处方ID
     */
    void deletePrescription(String id);
} 