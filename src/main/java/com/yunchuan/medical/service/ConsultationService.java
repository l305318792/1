package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.dto.PrescriptionDTO;
import java.util.List;

/**
 * 问诊服务接口
 */
public interface ConsultationService {
    /**
     * 创建问诊
     */
    ConsultationDTO createConsultation(ConsultationDTO consultation);
    
    /**
     * 获取问诊列表
     */
    List<ConsultationDTO> getConsultationList();
    
    /**
     * 获取用户的问诊列表
     */
    List<ConsultationDTO> getConsultationListByUserId(String userId);
    
    /**
     * 获取问诊详情
     */
    ConsultationDTO getConsultation(String id);
    
    /**
     * 开始问诊
     */
    ConsultationDTO startConsultation(String id);
    
    /**
     * 结束问诊
     */
    ConsultationDTO completeConsultation(String id, ConsultationDTO consultation);

    /**
     * 开具处方
     */
    PrescriptionDTO createPrescription(PrescriptionDTO prescription);
} 