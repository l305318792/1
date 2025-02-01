package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.constant.Constants;
import com.yunchuan.medical.dto.MedicalRecordDTO;
import com.yunchuan.medical.entity.MedicalRecord;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.mapper.MedicalRecordMapper;
import com.yunchuan.medical.service.MedicalRecordService;
import com.yunchuan.medical.service.UserService;
import com.yunchuan.medical.service.DoctorService;
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

    public MedicalRecordServiceImpl(MedicalRecordMapper medicalRecordMapper,
                                  UserService userService,
                                  DoctorService doctorService) {
        this.medicalRecordMapper = medicalRecordMapper;
        this.userService = userService;
        this.doctorService = doctorService;
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
                User patient = userService.getUserByUsername("admin"); // 临时使用admin用户
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
                dto.setDoctorName("主任医师"); // 临时使用固定值
                log.info("使用默认医生信息");
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