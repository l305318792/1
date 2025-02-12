package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.dto.PrescriptionDTO;
import com.yunchuan.medical.dto.ConsultationMessageDTO;
import com.yunchuan.medical.dto.RatingDTO;
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
     * 根据问诊ID获取处方列表
     * @param consultationId 问诊ID
     * @return 处方列表
     */
    List<PrescriptionDTO> getPrescriptionsByConsultationId(String consultationId);

    /**
     * 开具处方
     */
    PrescriptionDTO createPrescription(PrescriptionDTO prescription);

    /**
     * 根据医生ID和状态获取问诊列表
     * @param doctorId 医生ID
     * @param status 问诊状态
     * @return 问诊列表
     */
    List<ConsultationDTO> getConsultationListByDoctorIdAndStatus(String doctorId, String status);

    /**
     * 根据条件获取问诊列表
     * @param status 状态（可选）
     * @param doctorId 医生ID（可选）
     * @param departmentId 科室ID（可选）
     * @return 问诊列表
     */
    List<ConsultationDTO> getConsultationListByCondition(String status, String doctorId, String departmentId);

    /**
     * 清理历史数据中的格式问题
     */
    void cleanHistoricalData();

    /**
     * 发送问诊消息
     */
    ConsultationMessageDTO sendMessage(ConsultationMessageDTO messageDTO);

    /**
     * 获取问诊消息历史
     */
    List<ConsultationMessageDTO> getMessageHistory(String consultationId);

    /**
     * 评价问诊
     */
    RatingDTO rateConsultation(RatingDTO ratingDTO);

    /**
     * 关联预约到问诊记录
     * @param consultationId 问诊ID
     * @param appointmentId 预约ID
     * @param userId 用户ID
     * @return 更新后的问诊记录
     */
    ConsultationDTO linkAppointment(String consultationId, String appointmentId, String userId);

    /**
     * 获取处方详情
     * @param prescriptionId 处方ID
     * @return 处方详情DTO
     */
    PrescriptionDTO getPrescriptionDetail(String prescriptionId);

    /**
     * 支付问诊
     * @param consultationId 问诊ID
     * @return 更新后的问诊记录
     */
    ConsultationDTO payConsultation(String consultationId);
}