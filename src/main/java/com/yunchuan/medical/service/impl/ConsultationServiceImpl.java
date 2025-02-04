package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.dto.PrescriptionDTO;
import com.yunchuan.medical.entity.Consultation;
import com.yunchuan.medical.entity.Prescription;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.mapper.ConsultationMapper;
import com.yunchuan.medical.mapper.PrescriptionMapper;
import com.yunchuan.medical.service.ConsultationService;
import com.yunchuan.medical.service.DoctorService;
import com.yunchuan.medical.service.DepartmentService;
import com.yunchuan.medical.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 问诊服务实现类
 */
@Service
@Transactional
public class ConsultationServiceImpl implements ConsultationService {
    
    private final ConsultationMapper consultationMapper;
    private final PrescriptionMapper prescriptionMapper;
    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(ConsultationServiceImpl.class);

    public ConsultationServiceImpl(ConsultationMapper consultationMapper, 
                                 PrescriptionMapper prescriptionMapper,
                                 DoctorService doctorService,
                                 DepartmentService departmentService,
                                 UserService userService) {
        this.consultationMapper = consultationMapper;
        this.prescriptionMapper = prescriptionMapper;
        this.doctorService = doctorService;
        this.departmentService = departmentService;
        this.userService = userService;
    }

    @Override
    public ConsultationDTO createConsultation(ConsultationDTO consultationDTO) {
        // 验证医生ID和科室ID是否存在
        Doctor doctor = doctorService.getById(consultationDTO.getDoctorId());
        if (doctor == null) {
            throw new BusinessException("医生不存在");
        }
        
        Department department = departmentService.getById(consultationDTO.getDepartmentId());
        if (department == null) {
            throw new BusinessException("科室不存在");
        }
        
        Consultation consultation = new Consultation();
        BeanUtils.copyProperties(consultationDTO, consultation);
        consultation.setId(UUID.randomUUID().toString().replace("-", ""));
        consultation.setStatus("PENDING");
        consultation.setCreateTime(LocalDateTime.now());
        consultation.setUpdateTime(LocalDateTime.now());
        consultationMapper.insert(consultation);
        
        // 重新查询以确保所有字段都正确设置
        return getConsultation(consultation.getId());
    }
    
    @Override
    public List<ConsultationDTO> getConsultationList() {
        List<Consultation> consultations = consultationMapper.selectList(null);
        return consultations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ConsultationDTO getConsultation(String id) {
        Consultation consultation = consultationMapper.selectById(id);
        if (consultation == null) {
            return null;
        }
        return convertToDTO(consultation);
    }

    private ConsultationDTO convertToDTO(Consultation consultation) {
        ConsultationDTO dto = new ConsultationDTO();
        BeanUtils.copyProperties(consultation, dto);
        
        // 获取医生信息
        log.info("开始获取医生信息 - 医生ID: {}", consultation.getDoctorId());
        Doctor doctor = doctorService.getById(consultation.getDoctorId());
        if (doctor != null) {
            log.info("找到医生信息: {}", doctor);
            User doctorUser = userService.getById(doctor.getUserId());
            if (doctorUser != null) {
                log.info("找到医生用户信息: {}", doctorUser);
                dto.setDoctorName(doctorUser.getName());
            } else {
                log.warn("未找到医生用户信息 - 用户ID: {}", doctor.getUserId());
            }
        } else {
            log.warn("未找到医生信息 - 医生ID: {}", consultation.getDoctorId());
        }
        
        // 获取科室信息
        log.info("开始获取科室信息 - 科室ID: {}", consultation.getDepartmentId());
        Department department = departmentService.getById(consultation.getDepartmentId());
        if (department != null) {
            log.info("找到科室信息: {}", department);
            dto.setDepartmentName(department.getName());
        } else {
            log.warn("未找到科室信息 - 科室ID: {}", consultation.getDepartmentId());
        }
        
        // 获取用户信息
        log.info("开始获取用户信息 - 用户ID: {}", consultation.getUserId());
        User user = userService.getById(consultation.getUserId());
        if (user != null) {
            log.info("找到用户信息: {}", user);
            dto.setUserName(user.getName());
        } else {
            log.warn("未找到用户信息 - 用户ID: {}", consultation.getUserId());
        }
        
        return dto;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConsultationDTO startConsultation(String id) {
        log.info("开始问诊，ID: {}", id);
        
        try {
            // 1. 查询问诊记录
            Consultation consultation = consultationMapper.selectById(id);
            if (consultation == null) {
                throw new BusinessException(400, "问诊记录不存在");
            }
            
            log.info("当前问诊状态: {}", consultation.getStatus());
            
            // 2. 检查状态
            if (!"PENDING".equals(consultation.getStatus())) {
                throw new BusinessException(400, "当前问诊状态不允许开始问诊");
            }
            
            // 3. 更新状态
            consultation.setStatus("IN_PROGRESS");
            consultation.setStartTime(LocalDateTime.now());
            consultation.setUpdateTime(LocalDateTime.now());
            
            log.info("更新问诊状态: {}", consultation);
            int rows = consultationMapper.updateById(consultation);
            log.info("更新影响行数: {}", rows);
            
            if (rows != 1) {
                throw new BusinessException(500, "更新问诊状态失败");
            }
            
            // 4. 返回更新后的数据
            return convertToDTO(consultation);
            
        } catch (BusinessException e) {
            log.error("开始问诊失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("开始问诊失败", e);
            throw new BusinessException(500, "开始问诊失败：" + e.getMessage());
        }
    }
    
    @Override
    public ConsultationDTO completeConsultation(String id, ConsultationDTO consultationDTO) {
        Consultation consultation = consultationMapper.selectById(id);
        if (consultation == null) {
            throw new BusinessException(400, "问诊记录不存在");
        }
        BeanUtils.copyProperties(consultationDTO, consultation);
        consultation.setStatus("COMPLETED");
        consultation.setUpdateTime(LocalDateTime.now());
        consultationMapper.updateById(consultation);
        return convertToDTO(consultation);
    }

    @Override
    @Transactional
    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
        log.info("开始创建处方: {}", prescriptionDTO);
        
        // 1. 检查问诊是否存在
        Consultation consultation = consultationMapper.selectById(prescriptionDTO.getConsultationId());
        if (consultation == null) {
            throw new BusinessException(400, "问诊记录不存在");
        }
        
        // 2. 创建处方
        Prescription prescription = new Prescription();
        prescription.setId(UUID.randomUUID().toString().replace("-", ""));
        prescription.setConsultationId(prescriptionDTO.getConsultationId());
        prescription.setDoctorId(prescriptionDTO.getDoctorId());
        prescription.setPatientId(prescriptionDTO.getPatientId());
        prescription.setDiagnosis(prescriptionDTO.getDiagnosis());
        
        // 3. 处理medications字段：将List转换为字符串
        if (prescriptionDTO.getMedications() != null && !prescriptionDTO.getMedications().isEmpty()) {
            prescription.setMedications(String.join("\n", prescriptionDTO.getMedications()));
        }
        
        prescription.setDosage(prescriptionDTO.getDosage());
        prescription.setInstructions(prescriptionDTO.getInstructions());
        prescription.setStatus("PENDING");
        prescription.setCreateTime(LocalDateTime.now());
        prescription.setUpdateTime(LocalDateTime.now());
        
        try {
            // 4. 保存处方
            log.info("保存处方信息: {}", prescription);
            prescriptionMapper.insert(prescription);
            
            // 5. 返回结果
            prescriptionDTO.setId(prescription.getId());
            prescriptionDTO.setStatus(prescription.getStatus());
            prescriptionDTO.setCreateTime(prescription.getCreateTime());
            prescriptionDTO.setUpdateTime(prescription.getUpdateTime());
            
            return prescriptionDTO;
        } catch (Exception e) {
            log.error("创建处方失败: ", e);
            throw new BusinessException(500, "创建处方失败: " + e.getMessage());
        }
    }

    @Override
    public List<ConsultationDTO> getConsultationListByUserId(String userId) {
        List<Consultation> consultations = consultationMapper.selectByUserId(userId);
        return consultations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
} 