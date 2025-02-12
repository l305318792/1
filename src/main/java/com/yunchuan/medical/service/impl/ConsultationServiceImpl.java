package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunchuan.medical.dto.ConsultationDTO;
import com.yunchuan.medical.dto.PrescriptionDTO;
import com.yunchuan.medical.dto.ConsultationMessageDTO;
import com.yunchuan.medical.dto.RatingDTO;
import com.yunchuan.medical.dto.AppointmentDTO;
import com.yunchuan.medical.entity.Consultation;
import com.yunchuan.medical.entity.Prescription;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.entity.ConsultationMessage;
import com.yunchuan.medical.entity.Rating;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.mapper.ConsultationMapper;
import com.yunchuan.medical.mapper.PrescriptionMapper;
import com.yunchuan.medical.mapper.ConsultationMessageMapper;
import com.yunchuan.medical.service.ConsultationService;
import com.yunchuan.medical.service.DoctorService;
import com.yunchuan.medical.service.DepartmentService;
import com.yunchuan.medical.service.UserService;
import com.yunchuan.medical.service.RatingService;
import com.yunchuan.medical.service.AppointmentService;
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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * 问诊服务实现类
 */
@Service
@Transactional
public class ConsultationServiceImpl extends ServiceImpl<ConsultationMapper, Consultation> implements ConsultationService {
    
    private final ConsultationMapper consultationMapper;
    private final PrescriptionMapper prescriptionMapper;
    private final DoctorService doctorService;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final ConsultationMessageMapper consultationMessageMapper;
    private final RatingService ratingService;
    private final AppointmentService appointmentService;
    private static final Logger log = LoggerFactory.getLogger(ConsultationServiceImpl.class);

    public ConsultationServiceImpl(ConsultationMapper consultationMapper, 
                                 PrescriptionMapper prescriptionMapper,
                                 DoctorService doctorService,
                                 DepartmentService departmentService,
                                 UserService userService,
                                 ConsultationMessageMapper consultationMessageMapper,
                                 RatingService ratingService,
                                 AppointmentService appointmentService) {
        this.consultationMapper = consultationMapper;
        this.prescriptionMapper = prescriptionMapper;
        this.doctorService = doctorService;
        this.departmentService = departmentService;
        this.userService = userService;
        this.consultationMessageMapper = consultationMessageMapper;
        this.ratingService = ratingService;
        this.appointmentService = appointmentService;
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
        
        // 清理输入数据
        consultationDTO.setSymptoms(cleanMultilineText(consultationDTO.getSymptoms()));
        consultationDTO.setDiagnosis(cleanMultilineText(consultationDTO.getDiagnosis()));
        consultationDTO.setTreatment(cleanMultilineText(consultationDTO.getTreatment()));
        
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
        // 查询所有问诊记录，按创建时间倒序排序
        LambdaQueryWrapper<Consultation> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Consultation::getCreateTime);
        List<Consultation> consultations = consultationMapper.selectList(wrapper);
        
        // 使用统一的convertToDTO方法进行转换
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

    /**
     * 清理单行文本,去除多余空格
     */
    private String cleanText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.trim().replaceAll("\\s+", " ");
    }

    /**
     * 清理多行文本,保留换行符但去除每行多余空格
     */
    private String cleanMultilineText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text.lines()
                  .map(line -> line.trim().replaceAll("\\s+", " "))
                  .filter(line -> !line.isEmpty())
                  .collect(Collectors.joining("\n"));
    }

    /**
     * 将实体对象转换为DTO
     */
    private ConsultationDTO convertToDTO(Consultation consultation) {
        if (consultation == null) {
            return null;
        }

        ConsultationDTO dto = new ConsultationDTO();
        dto.setId(consultation.getId());
        dto.setUserId(consultation.getUserId());
        dto.setDoctorId(consultation.getDoctorId());
        dto.setDepartmentId(consultation.getDepartmentId());
        dto.setAppointmentId(consultation.getAppointmentId());
        
        // 处理症状描述
        String symptoms = cleanMultilineText(consultation.getSymptoms());
        dto.setSymptoms(symptoms.isEmpty() ? "等待患者补充症状描述" : symptoms);
        
        // 处理诊断结果
        String diagnosis = cleanMultilineText(consultation.getDiagnosis());
        dto.setDiagnosis(diagnosis.isEmpty() ? "" : diagnosis);
        
        // 处理治疗方案
        String treatment = cleanMultilineText(consultation.getTreatment());
        if (!treatment.isEmpty() && !treatment.contains("\n")) {
            // 如果治疗方案是单行文本，尝试格式化为多行
            treatment = Arrays.stream(treatment.split("[,，;；]"))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .map(s -> s.startsWith("1.") || s.startsWith("2.") || s.startsWith("3.") || s.startsWith("4.") ? s : s.startsWith("建议") ? s : "建议" + s)
                            .map(s -> s.endsWith("。") ? s : s + "。")
                            .collect(Collectors.joining("\n"));
        }
        dto.setTreatment(treatment);

        dto.setStatus(consultation.getStatus());
        dto.setStartTime(consultation.getStartTime());
        dto.setEndTime(consultation.getEndTime());
        dto.setCreateTime(consultation.getCreateTime());
        dto.setUpdateTime(consultation.getUpdateTime());

        // 获取用户信息
        try {
            User user = userService.getById(consultation.getUserId());
            if (user != null && user.getName() != null && !user.getName().trim().isEmpty()) {
                dto.setUserName(cleanText(user.getName()));
            } else {
                log.warn("用户名缺失或为空，用户ID: {}", consultation.getUserId());
                dto.setUserName("未知患者");
            }
        } catch (Exception e) {
            log.warn("获取用户信息失败: {}", e.getMessage());
            dto.setUserName("未知患者");
        }

        // 获取医生信息
        try {
            Doctor doctor = doctorService.getById(consultation.getDoctorId());
            if (doctor != null && doctor.getName() != null && !doctor.getName().trim().isEmpty()) {
                dto.setDoctorName(cleanText(doctor.getName()));
            } else {
                log.warn("医生名缺失或为空，医生ID: {}", consultation.getDoctorId());
                dto.setDoctorName("未知医生");
            }
        } catch (Exception e) {
            log.warn("获取医生信息失败: {}", e.getMessage());
            dto.setDoctorName("未知医生");
        }

        // 获取科室信息
        try {
            Department department = departmentService.getById(consultation.getDepartmentId());
            if (department != null && department.getName() != null && !department.getName().trim().isEmpty()) {
                dto.setDepartmentName(cleanText(department.getName()));
            } else {
                log.warn("科室名缺失或为空，科室ID: {}", consultation.getDepartmentId());
                dto.setDepartmentName("未知科室");
            }
        } catch (Exception e) {
            log.warn("获取科室信息失败: {}", e.getMessage());
            dto.setDepartmentName("未知科室");
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
        log.info("完成问诊，ID: {}, 诊断: {}", id, consultationDTO.getDiagnosis());
        
        // 1. 查询问诊记录
        Consultation consultation = consultationMapper.selectById(id);
        if (consultation == null) {
            throw new BusinessException(400, "问诊记录不存在");
        }
        
        // 2. 清理并更新诊断和治疗方案
        consultation.setDiagnosis(cleanMultilineText(consultationDTO.getDiagnosis()));
        consultation.setTreatment(cleanMultilineText(consultationDTO.getTreatment()));
        consultation.setStatus("COMPLETED");
        consultation.setEndTime(LocalDateTime.now());
        consultation.setUpdateTime(LocalDateTime.now());
        
        // 3. 保存更新
        consultationMapper.updateById(consultation);
        log.info("问诊完成，更新后的记录: {}", consultation);
        
        // 4. 返回完整的DTO
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
        
        // 4. 设置统一用药说明
        prescription.setDosage("用药注意事项：\n" +
                             "1. 请遵医嘱按时服药，不建议擅自增减剂量\n" +
                             "2. 布洛芬建议饭后服用，避免空腹\n" +
                             "3. 板蓝根颗粒可用温开水冲服\n" +
                             "4. 如出现不良反应请立即停药并联系医生");
        
        // 5. 设置用药说明和注意事项
        prescription.setInstructions(prescriptionDTO.getNotes()); // 将notes映射到instructions
        prescription.setStatus("PENDING");
        prescription.setCreateTime(LocalDateTime.now());
        prescription.setUpdateTime(LocalDateTime.now());
        
        try {
            // 6. 保存处方
            log.info("保存处方信息: {}", prescription);
            prescriptionMapper.insert(prescription);
            
            // 6.1 更新问诊状态为已完成
            consultation.setStatus("COMPLETED");
            consultation.setEndTime(LocalDateTime.now());
            consultation.setUpdateTime(LocalDateTime.now());
            consultationMapper.updateById(consultation);
            log.info("更新问诊状态为已完成");
            
            // 7. 返回结果
            prescriptionDTO.setId(prescription.getId());
            prescriptionDTO.setStatus(prescription.getStatus());
            prescriptionDTO.setCreateTime(prescription.getCreateTime());
            prescriptionDTO.setUpdateTime(prescription.getUpdateTime());
            prescriptionDTO.setInstructions(prescription.getInstructions()); // 设置返回的instructions
            prescriptionDTO.setDosage(prescription.getDosage()); // 设置返回的dosage
            
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

    @Override
    public List<ConsultationDTO> getConsultationListByDoctorIdAndStatus(String doctorId, String status) {
        List<Consultation> consultations = consultationMapper.selectByDoctorIdAndStatus(doctorId, status);
        return consultations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDTO> getPrescriptionsByConsultationId(String consultationId) {
        log.info("开始获取问诊处方列表 - 问诊ID: {}", consultationId);
        
        // 1. 验证问诊记录是否存在
        Consultation consultation = getById(consultationId);
        if (consultation == null) {
            throw new BusinessException("问诊记录不存在");
        }
        
        // 2. 查询处方记录，严格限制查询条件
        List<Prescription> prescriptions = prescriptionMapper.selectList(
            new LambdaQueryWrapper<Prescription>()
                .eq(Prescription::getConsultationId, consultationId)  // 只查询指定问诊的处方
                .eq(Prescription::getPatientId, consultation.getUserId())  // 确保只返回当前患者的处方
                .orderByDesc(Prescription::getCreateTime)
        );
        
        log.info("查询到{}条处方记录", prescriptions.size());
        
        // 3. 转换为DTO，同时进行数据清理和格式化
        return prescriptions.stream()
                .map(prescription -> {
                    PrescriptionDTO dto = new PrescriptionDTO();
                    BeanUtils.copyProperties(prescription, dto);
                    
                    // 处理诊断字段：去除多余空格
                    if (dto.getDiagnosis() != null) {
                        dto.setDiagnosis(cleanText(dto.getDiagnosis()));
                    }
                    
                    // 处理medications字段：将字符串转换为List，并规范化格式
                    if (prescription.getMedications() != null && !prescription.getMedications().isEmpty()) {
                        dto.setMedications(
                            Arrays.stream(prescription.getMedications().split("\n"))
                                  .map(this::cleanText)  // 使用统一的文本清理方法
                                  .filter(s -> !s.isEmpty())
                                  .collect(Collectors.toList())
                        );
                    } else {
                        dto.setMedications(new ArrayList<>());
                    }
                    
                    // 设置默认的用药说明
                    if (dto.getDosage() == null || dto.getDosage().isEmpty()) {
                        dto.setDosage("用药注意事项：\n" +
                                    "1. 请遵医嘱按时服药，不建议擅自增减剂量\n" +
                                    "2. 如出现不适请及时联系医生\n" +
                                    "3. 保存药品时请避免高温和潮湿\n" +
                                    "4. 请放在儿童接触不到的地方");
                    }
                    
                    // 处理instructions字段：规范化换行符和空格
                    if (dto.getInstructions() != null) {
                        dto.setInstructions(
                            Arrays.stream(dto.getInstructions()
                                .replace("\\n", "\n")
                                .split("\n"))
                                .map(this::cleanText)  // 使用统一的文本清理方法
                                .filter(line -> !line.isEmpty())
                                .collect(Collectors.joining("\n"))
                        );
                    }
                    
                    // 确保consultationId和patientId正确
                    dto.setConsultationId(consultationId);
                    dto.setPatientId(consultation.getUserId());
                    
                    return dto;
                })
                // 去重：根据诊断和用药内容
                .collect(Collectors.collectingAndThen(
                    Collectors.toMap(
                        dto -> dto.getDiagnosis() + "|" + String.join(",", dto.getMedications()),
                        dto -> dto,
                        (existing, replacement) -> existing.getCreateTime().isAfter(replacement.getCreateTime()) 
                            ? existing 
                            : replacement
                    ),
                    map -> new ArrayList<>(map.values())
                ))
                .stream()
                .sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultationDTO> getConsultationListByCondition(String status, String doctorId, String departmentId) {
        // 构建查询条件
        LambdaQueryWrapper<Consultation> wrapper = new LambdaQueryWrapper<>();
        
        // 添加状态过滤
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Consultation::getStatus, status);
        }
        
        // 添加医生过滤
        if (doctorId != null && !doctorId.isEmpty()) {
            wrapper.eq(Consultation::getDoctorId, doctorId);
        }
        
        // 添加科室过滤
        if (departmentId != null && !departmentId.isEmpty()) {
            wrapper.eq(Consultation::getDepartmentId, departmentId);
        }
        
        // 按创建时间倒序排序
        wrapper.orderByDesc(Consultation::getCreateTime);
        
        // 查询数据并使用统一的convertToDTO方法进行转换
        List<Consultation> consultations = consultationMapper.selectList(wrapper);
        return consultations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 清理数据库中的历史数据
     */
    public void cleanHistoricalData() {
        log.info("开始清理历史数据");
        
        // 1. 查询所有问诊记录
        List<Consultation> consultations = consultationMapper.selectList(null);
        
        // 2. 遍历并清理数据
        for (Consultation consultation : consultations) {
            boolean needUpdate = false;
            
            // 清理症状描述
            String cleanedSymptoms = cleanMultilineText(consultation.getSymptoms());
            if (!cleanedSymptoms.equals(consultation.getSymptoms())) {
                consultation.setSymptoms(cleanedSymptoms);
                needUpdate = true;
            }
            
            // 清理诊断结果
            String cleanedDiagnosis = cleanMultilineText(consultation.getDiagnosis());
            if (!cleanedDiagnosis.equals(consultation.getDiagnosis())) {
                consultation.setDiagnosis(cleanedDiagnosis);
                needUpdate = true;
            }
            
            // 清理治疗方案
            String cleanedTreatment = cleanMultilineText(consultation.getTreatment());
            if (!cleanedTreatment.equals(consultation.getTreatment())) {
                consultation.setTreatment(cleanedTreatment);
                needUpdate = true;
            }
            
            // 如果有数据被清理，则更新数据库
            if (needUpdate) {
                consultation.setUpdateTime(LocalDateTime.now());
                consultationMapper.updateById(consultation);
                log.info("清理并更新问诊记录: {}", consultation.getId());
            }
        }
        
        log.info("历史数据清理完成");
    }

    @Override
    public ConsultationMessageDTO sendMessage(ConsultationMessageDTO messageDTO) {
        // 1. 验证问诊是否存在
        Consultation consultation = getById(messageDTO.getConsultationId());
        if (consultation == null) {
            throw new BusinessException("问诊记录不存在");
        }

        // 2. 验证问诊状态
        if (!"IN_PROGRESS".equals(consultation.getStatus())) {
            throw new BusinessException("问诊已结束，无法发送消息");
        }

        // 3. 创建消息记录
        ConsultationMessage message = new ConsultationMessage();
        message.setId(UUID.randomUUID().toString().replace("-", ""));
        message.setConsultationId(messageDTO.getConsultationId());
        message.setSenderId(messageDTO.getSenderId());
        message.setSenderType(messageDTO.getSenderType());
        message.setContent(messageDTO.getContent());
        message.setMessageType(messageDTO.getMessageType());
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());

        // 4. 保存消息
        consultationMessageMapper.insert(message);

        // 5. 更新问诊最后活动时间
        consultation.setUpdateTime(LocalDateTime.now());
        updateById(consultation);

        // 6. 转换为DTO并返回
        BeanUtils.copyProperties(message, messageDTO);
        return messageDTO;
    }

    @Override
    public List<ConsultationMessageDTO> getMessageHistory(String consultationId) {
        // 1. 验证问诊是否存在
        Consultation consultation = getById(consultationId);
        if (consultation == null) {
            throw new BusinessException("问诊记录不存在");
        }

        // 2. 获取消息历史
        List<ConsultationMessage> messages = consultationMessageMapper.selectByConsultationId(consultationId);

        // 3. 转换为DTO并返回
        return messages.stream()
                .map(message -> {
                    ConsultationMessageDTO dto = new ConsultationMessageDTO();
                    BeanUtils.copyProperties(message, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public RatingDTO rateConsultation(RatingDTO ratingDTO) {
        // 1. 验证问诊是否存在
        Consultation consultation = getById(ratingDTO.getConsultationId());
        if (consultation == null) {
            throw new BusinessException("问诊记录不存在");
        }

        // 2. 验证问诊状态
        if (!"COMPLETED".equals(consultation.getStatus())) {
            throw new BusinessException("问诊未完成，无法评价");
        }

        // 3. 验证是否已评价
        if (ratingService.hasRated(ratingDTO.getConsultationId())) {
            throw new BusinessException("该问诊已评价");
        }

        // 4. 设置医生ID
        ratingDTO.setDoctorId(consultation.getDoctorId());

        // 5. 创建评价
        return ratingService.createRating(ratingDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConsultationDTO linkAppointment(String consultationId, String appointmentId, String userId) {
        log.info("开始关联预约到问诊记录，问诊ID: {}, 预约ID: {}, 用户ID: {}", consultationId, appointmentId, userId);
        
        // 1. 验证问诊记录是否存在
        Consultation consultation = getById(consultationId);
        if (consultation == null) {
            throw new BusinessException("问诊记录不存在");
        }
        
        // 2. 验证是否是当前用户的问诊记录
        if (!userId.equals(consultation.getUserId())) {
            throw new BusinessException("无权操作此问诊记录");
        }
        
        // 3. 验证预约是否已完成
        AppointmentDTO appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new BusinessException("预约记录不存在");
        }
        if (!"COMPLETED".equals(appointment.getStatus())) {
            throw new BusinessException("只能关联已完成的预约");
        }
        if (!userId.equals(appointment.getUserId())) {
            throw new BusinessException("无权操作此预约记录");
        }
        
        // 4. 更新问诊记录
        consultation.setAppointmentId(appointmentId);
        consultation.setUpdateTime(LocalDateTime.now());
        updateById(consultation);
        
        log.info("关联预约成功，问诊ID: {}, 预约ID: {}", consultationId, appointmentId);
        
        // 5. 返回更新后的问诊记录
        return convertToDTO(consultation);
    }

    @Override
    public PrescriptionDTO getPrescriptionDetail(String prescriptionId) {
        log.info("获取处方详情 - 处方ID: {}", prescriptionId);
        
        // 1. 查询处方记录
        Prescription prescription = prescriptionMapper.selectById(prescriptionId);
        if (prescription == null) {
            throw new BusinessException(400, "处方不存在");
        }
        
        log.info("查询到处方记录: consultationId={}, doctorId={}, patientId={}", 
            prescription.getConsultationId(), prescription.getDoctorId(), prescription.getPatientId());
            
        // 2. 转换为DTO
        PrescriptionDTO dto = new PrescriptionDTO();
        BeanUtils.copyProperties(prescription, dto);
        
        // 3. 处理medications字段：将字符串转换为List
        if (prescription.getMedications() != null && !prescription.getMedications().isEmpty()) {
            dto.setMedications(
                Arrays.stream(prescription.getMedications().split("\n"))
                    .map(this::cleanText)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList())
            );
        } else {
            dto.setMedications(new ArrayList<>());
        }
        
        // 4. 获取医生信息 - 修改为先通过user_id查询
        try {
            Doctor doctor = doctorService.lambdaQuery()
                .eq(Doctor::getUserId, prescription.getDoctorId())
                .one();
                
            if (doctor != null && doctor.getName() != null) {
                dto.setDoctorName(doctor.getName());
                // 更新为正确的医生ID
                dto.setDoctorId(doctor.getId());
                log.info("设置医生信息: id={}, name={}", doctor.getId(), doctor.getName());
            } else {
                log.warn("未找到医生信息或医生姓名为空, userId: {}", prescription.getDoctorId());
                dto.setDoctorName("未知医生");
            }
        } catch (Exception e) {
            log.error("获取医生信息失败: {}", e.getMessage());
            dto.setDoctorName("未知医生");
        }
        
        // 5. 处理instructions字段的换行符
        if (dto.getInstructions() != null) {
            String instructions = dto.getInstructions()
                .replace("\\n", "\n")
                .replace("\r", "");
            dto.setInstructions(instructions);
            log.info("处理后的instructions: {}", instructions);
        }
        
        // 6. 查询问诊记录(用于关联)
        try {
            Consultation consultation = getById(prescription.getConsultationId());
            if (consultation == null) {
                consultation = consultationMapper.selectOne(
                    new LambdaQueryWrapper<Consultation>()
                        .eq(Consultation::getDoctorId, prescription.getDoctorId())
                        .eq(Consultation::getUserId, prescription.getPatientId())
                        .orderByDesc(Consultation::getCreateTime)
                        .last("LIMIT 1")
                );
                
                if (consultation != null) {
                    log.info("通过医生ID和患者ID找到问诊记录: {}", consultation.getId());
                    dto.setConsultationId(consultation.getId());
                }
            }
        } catch (Exception e) {
            log.warn("查询问诊记录失败: {}", e.getMessage());
        }
        
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConsultationDTO payConsultation(String consultationId) {
        log.info("开始处理问诊支付，问诊ID: {}", consultationId);
        
        // 1. 获取问诊记录
        Consultation consultation = getById(consultationId);
        if (consultation == null) {
            throw new BusinessException("问诊记录不存在");
        }
        
        // 2. 验证状态
        if (!"PENDING".equals(consultation.getStatus())) {
            throw new BusinessException("问诊状态不正确，当前状态: " + consultation.getStatus());
        }
        
        // 3. 更新问诊状态为待接诊
        consultation.setStatus("WAITING");
        consultation.setUpdateTime(LocalDateTime.now());
        updateById(consultation);
        
        log.info("问诊支付处理完成，ID: {}", consultationId);
        return convertToDTO(consultation);
    }
} 