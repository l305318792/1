package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.constant.Constants;
import com.yunchuan.medical.dto.PrescriptionDTO;
import com.yunchuan.medical.entity.Prescription;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.mapper.PrescriptionMapper;
import com.yunchuan.medical.service.PrescriptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * 处方服务实现类
 */
@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    private static final Logger log = LoggerFactory.getLogger(PrescriptionServiceImpl.class);

    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionServiceImpl(PrescriptionMapper prescriptionMapper) {
        this.prescriptionMapper = prescriptionMapper;
    }

    @Override
    public List<PrescriptionDTO> getPrescriptionList(String recordId) {
        List<Prescription> prescriptions;
        if (recordId != null) {
            prescriptions = prescriptionMapper.selectByConsultationId(recordId);
        } else {
            prescriptions = prescriptionMapper.selectList();
        }
        return prescriptions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PrescriptionDTO getPrescriptionById(String id) {
        Prescription prescription = prescriptionMapper.selectById(id);
        if (prescription == null) {
            throw new BusinessException("处方不存在");
        }
        return convertToDTO(prescription);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
        log.info("开始创建处方: {}", prescriptionDTO);
        
        try {
            Prescription prescription = new Prescription();
            prescription.setId(UUID.randomUUID().toString().replace("-", ""));
            prescription.setConsultationId(prescriptionDTO.getConsultationId());
            prescription.setDoctorId(prescriptionDTO.getDoctorId());
            prescription.setPatientId(prescriptionDTO.getPatientId());
            prescription.setDiagnosis(prescriptionDTO.getDiagnosis());
            prescription.setDosage(prescriptionDTO.getDosage());
            prescription.setInstructions(prescriptionDTO.getInstructions());
            
            // 处理medications字段：将List转换为字符串
            if (prescriptionDTO.getMedications() != null && !prescriptionDTO.getMedications().isEmpty()) {
                prescription.setMedications(String.join(";", prescriptionDTO.getMedications()));
            }
            
            prescription.setStatus("PENDING");
            prescription.setCreateTime(LocalDateTime.now());
            prescription.setUpdateTime(LocalDateTime.now());
            
            log.info("保存处方信息: {}", prescription);
            if (prescriptionMapper.insert(prescription) <= 0) {
                throw new BusinessException("创建处方失败");
            }
            
            // 构建返回对象
            prescriptionDTO.setId(prescription.getId());
            prescriptionDTO.setStatus(prescription.getStatus());
            prescriptionDTO.setCreateTime(prescription.getCreateTime());
            prescriptionDTO.setUpdateTime(prescription.getUpdateTime());
            
            return prescriptionDTO;
        } catch (Exception e) {
            log.error("创建处方失败: ", e);
            throw new BusinessException("创建处方失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrescriptionDTO updatePrescription(PrescriptionDTO prescriptionDTO) {
        if (prescriptionDTO == null) {
            String msg = "处方数据不能为空";
            log.error(msg);
            throw new BusinessException(msg);
        }
        
        log.info("开始更新处方，接收到的处方DTO: {}", prescriptionDTO);
        
        // 验证ID
        if (prescriptionDTO.getId() == null || prescriptionDTO.getId().trim().isEmpty()) {
            String msg = "处方ID不能为空";
            log.error(msg);
            throw new BusinessException(msg);
        }
        
        // 查询原处方
        Prescription prescription = null;
        try {
            log.info("开始查询处方，ID: {}", prescriptionDTO.getId());
            prescription = prescriptionMapper.selectById(prescriptionDTO.getId());
            log.info("查询结果: {}", prescription);
            
            if (prescription == null) {
                String msg = String.format("处方不存在或已被删除，ID: %s", prescriptionDTO.getId());
                log.error(msg);
                throw new BusinessException(msg);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            String msg = String.format("查询处方时发生异常，ID: %s, 错误: %s", prescriptionDTO.getId(), e.getMessage());
            log.error(msg, e);
            throw new BusinessException(msg);
        }
        
        log.info("查询到原处方信息: {}", prescription);
        
        // 验证必填字段
        if (prescriptionDTO.getConsultationId() == null || prescriptionDTO.getConsultationId().trim().isEmpty()) {
            String msg = "问诊ID不能为空";
            log.error(msg);
            throw new BusinessException(msg);
        }
        if (prescriptionDTO.getDoctorId() == null || prescriptionDTO.getDoctorId().trim().isEmpty()) {
            String msg = "医生ID不能为空";
            log.error(msg);
            throw new BusinessException(msg);
        }
        if (prescriptionDTO.getPatientId() == null || prescriptionDTO.getPatientId().trim().isEmpty()) {
            String msg = "患者ID不能为空";
            log.error(msg);
            throw new BusinessException(msg);
        }
        
        // 验证状态值
        if (prescriptionDTO.getStatus() == null || prescriptionDTO.getStatus().trim().isEmpty()) {
            String msg = "状态不能为空";
            log.error(msg);
            throw new BusinessException(msg);
        }
        if (!"PENDING".equals(prescriptionDTO.getStatus()) && 
            !"COMPLETED".equals(prescriptionDTO.getStatus()) &&
            !"CANCELLED".equals(prescriptionDTO.getStatus())) {
            String msg = String.format("无效的状态值: %s，只能是PENDING/COMPLETED/CANCELLED", prescriptionDTO.getStatus());
            log.error(msg);
            throw new BusinessException(msg);
        }
        
        try {
            // 更新基本字段
            log.info("开始更新基本字段...");
            prescription.setConsultationId(prescriptionDTO.getConsultationId());
            prescription.setDoctorId(prescriptionDTO.getDoctorId());
            prescription.setPatientId(prescriptionDTO.getPatientId());
            prescription.setDiagnosis(prescriptionDTO.getDiagnosis());
            prescription.setDosage(prescriptionDTO.getDosage());
            prescription.setInstructions(prescriptionDTO.getInstructions());
            prescription.setStatus(prescriptionDTO.getStatus());
            
            // 处理medications字段：将List转换为字符串
            log.info("开始处理medications字段...");
            if (prescriptionDTO.getMedications() != null && !prescriptionDTO.getMedications().isEmpty()) {
                try {
                    String medicationsStr = String.join(";", prescriptionDTO.getMedications());
                    prescription.setMedications(medicationsStr);
                    log.info("处理medications后的结果: {}", medicationsStr);
                } catch (Exception e) {
                    String msg = String.format("处理medications字段时发生异常: %s", e.getMessage());
                    log.error(msg, e);
                    throw new BusinessException(msg);
                }
            } else {
                log.warn("medications为空，设置为空字符串");
                prescription.setMedications("");
            }
            
            // 设置更新时间
            prescription.setUpdateTime(LocalDateTime.now());
            
            // 执行更新
            log.info("即将更新的处方完整信息: {}", prescription);
            try {
                // 先尝试删除旧记录
                prescriptionMapper.deleteById(prescription.getId());
                log.info("删除旧记录成功");
                
                // 插入新记录
                if (prescriptionMapper.insert(prescription) <= 0) {
                    String msg = String.format("插入新记录失败，ID: %s", prescriptionDTO.getId());
                    log.error(msg);
                    throw new BusinessException(msg);
                }
                log.info("插入新记录成功");
            } catch (Exception e) {
                String msg = String.format("执行数据库操作时发生异常: %s", e.getMessage());
                log.error(msg, e);
                throw new BusinessException(msg);
            }
            
            // 重新查询最新数据
            log.info("开始查询更新后的数据...");
            Prescription updatedPrescription;
            try {
                updatedPrescription = prescriptionMapper.selectById(prescription.getId());
                if (updatedPrescription == null) {
                    String msg = String.format("更新后查询处方失败，ID: %s", prescriptionDTO.getId());
                    log.error(msg);
                    throw new BusinessException(msg);
                }
                log.info("查询到更新后的数据: {}", updatedPrescription);
            } catch (Exception e) {
                String msg = String.format("查询更新后数据时发生异常: %s", e.getMessage());
                log.error(msg, e);
                throw new BusinessException(msg);
            }
            
            // 转换为DTO
            log.info("开始转换为DTO...");
            try {
                PrescriptionDTO updatedDTO = convertToDTO(updatedPrescription);
                log.info("更新成功，返回DTO: {}", updatedDTO);
                return updatedDTO;
            } catch (Exception e) {
                String msg = String.format("转换DTO时发生异常: %s", e.getMessage());
                log.error(msg, e);
                throw new BusinessException(msg);
            }
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            String msg = String.format("更新处方时发生未知异常，ID: %s, 错误类型: %s, 错误信息: %s", 
                prescriptionDTO.getId(), e.getClass().getName(), e.getMessage());
            log.error(msg, e);
            throw new BusinessException(msg);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePrescription(String id) {
        if (prescriptionMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除处方失败");
        }
    }

    private PrescriptionDTO convertToDTO(Prescription prescription) {
        if (prescription == null) {
            return null;
        }
        
        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setId(prescription.getId());
        dto.setConsultationId(prescription.getConsultationId());
        dto.setDoctorId(prescription.getDoctorId());
        dto.setPatientId(prescription.getPatientId());
        dto.setDiagnosis(prescription.getDiagnosis());
        dto.setDosage(prescription.getDosage());
        dto.setInstructions(prescription.getInstructions());
        dto.setStatus(prescription.getStatus());
        dto.setCreateTime(prescription.getCreateTime());
        dto.setUpdateTime(prescription.getUpdateTime());
        
        // 处理medications字段：将字符串转换为List
        if (prescription.getMedications() != null && !prescription.getMedications().isEmpty()) {
            dto.setMedications(Arrays.asList(prescription.getMedications().split(";")));
        }
        
        return dto;
    }
} 