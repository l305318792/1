package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.constant.Constants;
import com.yunchuan.medical.dto.MedicalRecordDTO;
import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.dto.PrescriptionDTO;
import com.yunchuan.medical.entity.MedicalRecord;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.mapper.MedicalRecordMapper;
import com.yunchuan.medical.service.MedicalRecordService;
import com.yunchuan.medical.service.UserService;
import com.yunchuan.medical.service.DoctorService;
import com.yunchuan.medical.service.ConsultationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 病历服务实现类
 */
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger log = LoggerFactory.getLogger(MedicalRecordServiceImpl.class);

    private final MedicalRecordMapper medicalRecordMapper;
    private final UserService userService;
    private final DoctorService doctorService;
    private final ConsultationService consultationService;

    public MedicalRecordServiceImpl(MedicalRecordMapper medicalRecordMapper,
                                  UserService userService,
                                  DoctorService doctorService,
                                  ConsultationService consultationService) {
        this.medicalRecordMapper = medicalRecordMapper;
        this.userService = userService;
        this.doctorService = doctorService;
        this.consultationService = consultationService;
    }

    @Override
    public List<MedicalRecordDTO> getMedicalRecordList(String patientId) {
        List<MedicalRecord> records;
        if (patientId != null) {
            records = medicalRecordMapper.selectByPatientId(patientId);
        } else {
            records = medicalRecordMapper.selectAll();
        }
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MedicalRecordDTO getMedicalRecordById(String id) {
        try {
            MedicalRecord record = medicalRecordMapper.selectById(id);
            if (record == null) {
                throw new BusinessException("病历不存在");
            }
            return convertToDTO(record);
        } catch (Exception e) {
            throw new BusinessException("获取病历失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO recordDTO) {
        MedicalRecord record = new MedicalRecord();
        BeanUtils.copyProperties(recordDTO, record);
        
        // 设置基础字段
        record.setId(String.format("MR%d", System.currentTimeMillis() % 100000000)); // 生成类似 MR12345678 的ID
        record.setStatus(Constants.STATUS_ENABLED);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        
        // 设置必填字段的默认值
        if (record.getDiagnosis() == null) {
            record.setDiagnosis("待诊断");
        }
        if (record.getChiefComplaint() == null) {
            record.setChiefComplaint("无");
        }
        if (record.getPresentIllness() == null) {
            record.setPresentIllness("无");
        }
        
        if (medicalRecordMapper.insert(record) > 0) {
            return convertToDTO(record);
        }
        throw new BusinessException("创建病历失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MedicalRecordDTO updateMedicalRecord(MedicalRecordDTO recordDTO) {
        MedicalRecord record = medicalRecordMapper.selectById(recordDTO.getId());
        if (record == null) {
            throw new BusinessException("病历不存在");
        }
        
        // 验证状态值
        if (recordDTO.getStatus() != null && 
            !Constants.STATUS_ENABLED.equals(recordDTO.getStatus()) && 
            !Constants.STATUS_DISABLED.equals(recordDTO.getStatus())) {
            throw new BusinessException("无效的状态值");
        }
        
        BeanUtils.copyProperties(recordDTO, record);
        record.setUpdateTime(LocalDateTime.now());
        
        if (medicalRecordMapper.update(record) > 0) {
            return convertToDTO(record);
        }
        throw new BusinessException("更新病历失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMedicalRecord(String id) {
        MedicalRecord record = medicalRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("病历不存在");
        }
        
        if (medicalRecordMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除病历失败");
        }
    }

    /**
     * 根据问诊和处方自动生成病历
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MedicalRecordDTO generateFromConsultation(String consultationId) {
        log.info("开始根据问诊生成病历，问诊ID: {}", consultationId);
        
        // 1. 获取问诊记录
        ConsultationDTO consultation = consultationService.getConsultation(consultationId);
        if (consultation == null) {
            throw new BusinessException("问诊记录不存在");
        }
        
        // 2. 获取处方列表
        List<PrescriptionDTO> prescriptions = consultationService.getPrescriptionsByConsultationId(consultationId);
        if (prescriptions.isEmpty()) {
            throw new BusinessException("未找到相关处方");
        }
        
        // 3. 创建病历记录
        MedicalRecord record = new MedicalRecord();
        record.setId(String.format("MR%d", System.currentTimeMillis() % 100000000));
        record.setPatientId(consultation.getUserId());
        record.setDoctorId(consultation.getDoctorId());
        
        // 4. 设置就诊时间(使用问诊开始时间)
        record.setVisitTime(consultation.getStartTime());
        
        // 5. 设置主诉(来自问诊的症状描述)
        record.setChiefComplaint(consultation.getSymptoms());
        
        // 6. 设置现病史(使用问诊记录中的症状描述)
        record.setPresentIllness(consultation.getSymptoms());
        
        // 7. 设置诊断(优先使用问诊记录中的诊断，如果没有再从处方中获取)
        String diagnosis = null;
        if (consultation.getDiagnosis() != null && !consultation.getDiagnosis().trim().isEmpty()) {
            diagnosis = consultation.getDiagnosis().trim();
        } else {
            // 从处方中查找有效的诊断
            for (PrescriptionDTO prescription : prescriptions) {
                if (prescription.getDiagnosis() != null && 
                    !prescription.getDiagnosis().trim().isEmpty() && 
                    !"待医生诊断".equals(prescription.getDiagnosis().trim())) {
                    diagnosis = prescription.getDiagnosis().trim();
                    break;
                }
            }
        }
        record.setDiagnosis(diagnosis != null ? diagnosis : "待诊断");
        
        // 8. 设置治疗方案(包含所有处方信息)
        StringBuilder treatment = new StringBuilder();
        treatment.append("治疗记录：\n");
        
        // 按时间顺序处理所有处方
        for (int i = prescriptions.size() - 1; i >= 0; i--) {
            PrescriptionDTO prescription = prescriptions.get(i);
            treatment.append("\n就诊时间：").append(prescription.getCreateTime().toLocalDate()).append("\n");
            
            // 添加诊断
            if (prescription.getDiagnosis() != null && !prescription.getDiagnosis().trim().isEmpty() 
                && !"待医生诊断".equals(prescription.getDiagnosis().trim())) {
                treatment.append("诊断：").append(prescription.getDiagnosis().trim()).append("\n");
            }
            
            // 添加用药信息
            if (prescription.getMedications() != null && !prescription.getMedications().isEmpty()) {
                treatment.append("用药方案：\n");
                for (String medication : prescription.getMedications()) {
                    if (medication != null && !medication.trim().isEmpty()) {
                        treatment.append("- ").append(medication.trim()).append("\n");
                    }
                }
            }
            
            // 添加用药说明
            if (prescription.getDosage() != null && !prescription.getDosage().trim().isEmpty()) {
                treatment.append("\n用药说明：\n").append(prescription.getDosage().trim());
            }
            
            // 添加医嘱
            if (prescription.getInstructions() != null && !prescription.getInstructions().trim().isEmpty()) {
                treatment.append("\n\n医嘱：\n").append(prescription.getInstructions().trim());
            }
            
            // 添加分隔线（除了最后一条记录）
            if (i > 0) {
                treatment.append("\n\n----------------------------------------\n");
            }
        }
        
        // 如果问诊有治疗建议，也添加到治疗方案中
        if (consultation.getTreatment() != null && !consultation.getTreatment().trim().isEmpty()) {
            treatment.append("\n\n总体治疗建议：\n").append(consultation.getTreatment().trim());
        }
        
        record.setTreatment(treatment.toString());
        
        // 9. 设置既往史和体格检查为"无记录"
        record.setPastHistory("无记录");
        record.setPhysicalExam("无记录");
        
        // 10. 设置其他信息
        record.setStatus(Constants.STATUS_ENABLED);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        
        // 11. 保存病历
        if (medicalRecordMapper.insert(record) <= 0) {
            throw new BusinessException("保存病历失败");
        }
        
        log.info("病历生成成功，ID: {}", record.getId());
        
        // 12. 返回病历DTO
        return convertToDTO(record);
    }

    private MedicalRecordDTO convertToDTO(MedicalRecord record) {
        if (record == null) {
            return null;
        }
        MedicalRecordDTO dto = new MedicalRecordDTO();
        BeanUtils.copyProperties(record, dto);
        
        try {
            log.info("开始获取患者和医生信息，病历ID: {}", record.getId());
            
            // 获取患者信息
            String patientId = record.getPatientId();
            if (patientId != null) {
                log.info("获取患者信息，患者ID: {}", patientId);
                User patient = userService.getById(patientId);
                if (patient != null) {
                    dto.setPatientName(patient.getName() != null ? patient.getName() : patient.getUsername());
                    log.info("成功获取患者信息: {}", patient);
                } else {
                    log.warn("未找到患者信息，患者ID: {}", patientId);
                    dto.setPatientName("未知患者");
                }
            }
            
            // 获取医生信息
            String doctorId = record.getDoctorId();
            if (doctorId != null) {
                log.info("开始获取医生信息，医生ID: {}", doctorId);
                Doctor doctor = doctorService.getById(doctorId);
                if (doctor != null) {
                    dto.setDoctorName(doctor.getName());
                    log.info("成功获取医生信息: {}", doctor);
                } else {
                    log.warn("未找到医生信息，医生ID: {}", doctorId);
                    dto.setDoctorName("未知医生");
                }
            }
        } catch (Exception e) {
            log.error("获取用户或医生信息时发生错误: ", e);
            // 设置默认值，避免前端显示异常
            if (dto.getPatientName() == null) dto.setPatientName("未知患者");
            if (dto.getDoctorName() == null) dto.setDoctorName("未知医生");
        }
        
        return dto;
    }
} 