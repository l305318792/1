package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.service.DataCleanService;
import com.yunchuan.medical.mapper.UserMapper;
import com.yunchuan.medical.mapper.DoctorMapper;
import com.yunchuan.medical.mapper.DepartmentMapper;
import com.yunchuan.medical.mapper.ConsultationMapper;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.entity.Consultation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据清理服务实现类
 */
@Slf4j
@Service
public class DataCleanServiceImpl implements DataCleanService {

    private final UserMapper userMapper;
    private final DoctorMapper doctorMapper;
    private final DepartmentMapper departmentMapper;
    private final ConsultationMapper consultationMapper;

    public DataCleanServiceImpl(
            UserMapper userMapper,
            DoctorMapper doctorMapper,
            DepartmentMapper departmentMapper,
            ConsultationMapper consultationMapper) {
        this.userMapper = userMapper;
        this.doctorMapper = doctorMapper;
        this.departmentMapper = departmentMapper;
        this.consultationMapper = consultationMapper;
    }

    /**
     * 清理单行文本
     */
    private String cleanText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.trim().replaceAll("\\s+", " ");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanAllData() {
        log.info("开始清理所有数据");
        cleanUserData();
        cleanDoctorData();
        cleanDepartmentData();
        cleanConsultationData();
        log.info("所有数据清理完成");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanUserData() {
        log.info("开始清理用户数据");
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            String cleanedName = cleanText(user.getName());
            if (!cleanedName.equals(user.getName())) {
                user.setName(cleanedName);
                user.setUpdateTime(LocalDateTime.now());
                userMapper.updateById(user);
                log.info("清理用户数据: {} -> {}", user.getId(), cleanedName);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanDoctorData() {
        log.info("开始清理医生数据");
        List<Doctor> doctors = doctorMapper.selectList(null);
        for (Doctor doctor : doctors) {
            String cleanedName = cleanText(doctor.getName());
            if (!cleanedName.equals(doctor.getName())) {
                doctor.setName(cleanedName);
                doctor.setUpdateTime(LocalDateTime.now());
                doctorMapper.updateById(doctor);
                log.info("清理医生数据: {} -> {}", doctor.getId(), cleanedName);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanDepartmentData() {
        log.info("开始清理科室数据");
        List<Department> departments = departmentMapper.selectList(null);
        for (Department department : departments) {
            String cleanedName = cleanText(department.getName());
            if (!cleanedName.equals(department.getName())) {
                department.setName(cleanedName);
                department.setUpdateTime(LocalDateTime.now());
                departmentMapper.updateById(department);
                log.info("清理科室数据: {} -> {}", department.getId(), cleanedName);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanConsultationData() {
        log.info("开始清理问诊数据");
        List<Consultation> consultations = consultationMapper.selectList(null);
        for (Consultation consultation : consultations) {
            boolean needUpdate = false;

            // 清理症状描述
            String cleanedSymptoms = cleanText(consultation.getSymptoms());
            if (!cleanedSymptoms.equals(consultation.getSymptoms())) {
                consultation.setSymptoms(cleanedSymptoms);
                needUpdate = true;
            }

            // 清理诊断结果
            String cleanedDiagnosis = cleanText(consultation.getDiagnosis());
            if (!cleanedDiagnosis.equals(consultation.getDiagnosis())) {
                consultation.setDiagnosis(cleanedDiagnosis);
                needUpdate = true;
            }

            // 清理治疗方案
            String cleanedTreatment = cleanText(consultation.getTreatment());
            if (!cleanedTreatment.equals(consultation.getTreatment())) {
                consultation.setTreatment(cleanedTreatment);
                needUpdate = true;
            }

            if (needUpdate) {
                consultation.setUpdateTime(LocalDateTime.now());
                consultationMapper.updateById(consultation);
                log.info("清理问诊数据: {}", consultation.getId());
            }
        }
    }
} 