package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunchuan.medical.dto.AppointmentDTO;
import com.yunchuan.medical.dto.AppointmentQueryDTO;
import com.yunchuan.medical.dto.AppointmentStatisticsDTO;
import com.yunchuan.medical.dto.PaymentRecordDTO;
import com.yunchuan.medical.entity.Appointment;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.mapper.AppointmentMapper;
import com.yunchuan.medical.service.AppointmentService;
import com.yunchuan.medical.service.UserService;
import com.yunchuan.medical.service.DoctorService;
import com.yunchuan.medical.service.DepartmentService;
import com.yunchuan.medical.service.PaymentService;
import com.yunchuan.medical.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunchuan.medical.exception.BusinessException;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import java.util.Collections;
import java.util.HashMap;

/**
 * <p>
 * 预约表 服务实现类
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {

    private final UserService userService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PaymentService paymentService;

    private static final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    public AppointmentServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取指定时间范围内的最大序号
     */
    private Integer getMaxSequenceNumber(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("查询时间范围内的最大序号: {} - {}", startTime, endTime);
        
        QueryWrapper<Appointment> wrapper = new QueryWrapper<>();
        wrapper.select("IFNULL(MAX(sequence_number), 0) as max_seq")
               .ge("create_time", startTime)
               .lt("create_time", endTime);
               
        Map<String, Object> result = baseMapper.selectMaps(wrapper).stream()
                                         .findFirst()
                                         .orElse(new HashMap<>());
                                         
        Object maxSeq = result.getOrDefault("max_seq", 0);
        log.info("查询到的最大序号: {}", maxSeq);
        
        if (maxSeq instanceof Number) {
            return ((Number) maxSeq).intValue();
        }
        
        return 0;
    }

    @Override
    @Transactional
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        log.info("开始创建预约: {}", appointmentDTO);
        
        // 1. 创建预约记录
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(appointmentDTO, appointment);
        
        // 2. 设置基础信息
        appointment.setId(UUID.randomUUID().toString().replace("-", ""));
        appointment.setStatus("UNPAID");
        appointment.setCreateTime(LocalDateTime.now());
        appointment.setUpdateTime(LocalDateTime.now());
        
        // 3. 设置预约序号
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime tomorrow = today.plusDays(1);
        Integer maxSequence = getMaxSequenceNumber(today, tomorrow);
        appointment.setSequenceNumber(maxSequence + 1);
        
        // 4. 设置预约金额
        if (appointment.getAmount() == null) {
            appointment.setAmount(new BigDecimal("150.00")); // 默认预约金额
        }
        
        // 5. 保存预约记录
        save(appointment);
        
        // 6. 创建支付记录
        PaymentRecordDTO paymentRecord = new PaymentRecordDTO();
        paymentRecord.setUserId(appointment.getUserId());
        paymentRecord.setBusinessId(appointment.getId());
        paymentRecord.setBusinessType("APPOINTMENT");
        paymentRecord.setAmount(appointment.getAmount());
        paymentRecord.setPaymentMethod("WECHAT");
        paymentRecord.setRemark("预约挂号支付");
        PaymentRecordDTO savedPayment = paymentService.createPayment(paymentRecord);
        
        // 7. 更新预约记录的支付ID
        appointment.setPaymentId(savedPayment.getId());
        updateById(appointment);
        
        // 8. 转换为DTO并返回
        return convertToDTO(appointment);
    }

    @Override
    @Transactional
    public AppointmentDTO payAppointment(String id) {
        log.info("开始支付预约，ID: {}", id);
        
        // 获取预约信息
        Appointment appointment = getById(id);
        if (appointment == null) {
            throw new BusinessException("预约不存在");
        }
        
        // 验证金额
        if (appointment.getAmount() == null) {
            log.error("预约金额为空，ID: {}", id);
            throw new BusinessException("预约金额不能为空");
        }
        
        // 验证状态
        if (!"UNPAID".equals(appointment.getStatus())) {
            throw new BusinessException("预约状态不正确，当前状态: " + appointment.getStatus());
        }
        
        // 更新预约状态
        appointment.setStatus("PAID");
        appointment.setUpdateTime(LocalDateTime.now());
        updateById(appointment);
        
        log.info("预约支付成功，ID: {}, 金额: {}", id, appointment.getAmount());
        return convertToDTO(appointment);
    }

    @Override
    @Transactional
    public AppointmentDTO cancelAppointment(String id) {
        // 获取当前用户ID
        String userId = SecurityUtils.getCurrentUserId();
        
        // 查询预约
        Appointment appointment = getById(id);
        if (appointment == null) {
            throw new RuntimeException("预约不存在");
        }
        
        // 验证是否是当前用户的预约
        if (!appointment.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此预约");
        }
        
        // 验证预约状态
        if ("COMPLETED".equals(appointment.getStatus()) || "CANCELLED".equals(appointment.getStatus())) {
            throw new RuntimeException("预约状态不正确");
        }
        
        // 更新预约状态
        appointment.setStatus("CANCELLED");
        appointment.setUpdateTime(LocalDateTime.now());
        updateById(appointment);
        
        // 转换为DTO返回，补充医生和科室信息
        AppointmentDTO result = new AppointmentDTO();
        BeanUtils.copyProperties(appointment, result);
        
        // 补充医生信息
        Doctor doctor = doctorService.getById(appointment.getDoctorId());
        if (doctor != null) {
            result.setDoctorName(doctor.getName());
        }
        
        // 补充科室信息
        Department department = departmentService.getById(appointment.getDepartmentId());
        if (department != null) {
            result.setDepartmentName(department.getName());
        }
        
        // 设置预约时间
        result.setAppointmentTime(appointment.getVisitTime());
        
        return result;
    }

    @Override
    public AppointmentDTO getAppointmentById(String id) {
        // 查询预约
        Appointment appointment = getById(id);
        if (appointment == null) {
            throw new RuntimeException("预约不存在");
        }
        
        // 查询医生和科室信息
        Doctor doctor = doctorService.getById(appointment.getDoctorId());
        if (doctor == null) {
            throw new RuntimeException("医生不存在");
        }
        Department department = departmentService.getById(appointment.getDepartmentId());
        if (department == null) {
            throw new RuntimeException("科室不存在");
        }
        
        // 转换为DTO返回
        AppointmentDTO result = new AppointmentDTO();
        BeanUtils.copyProperties(appointment, result);
        result.setDoctorName(doctor.getName());
        result.setDepartmentName(department.getName());
        result.setAppointmentTime(appointment.getVisitTime());
        return result;
    }

    @Override
    public List<AppointmentDTO> getMyAppointments() {
        // 获取当前用户ID
        String userId = SecurityUtils.getCurrentUserId();
        
        // 查询当前用户的预约列表
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        
        List<Appointment> appointments = list(queryWrapper);
        
        // 转换为DTO列表返回，并补充医生和科室信息
        return appointments.stream()
                .map(appointment -> {
                    AppointmentDTO dto = new AppointmentDTO();
                    BeanUtils.copyProperties(appointment, dto);
                    
                    // 补充医生信息
                    Doctor doctor = doctorService.getById(appointment.getDoctorId());
                    if (doctor != null) {
                        dto.setDoctorName(doctor.getName());
                    }
                    
                    // 补充科室信息
                    Department department = departmentService.getById(appointment.getDepartmentId());
                    if (department != null) {
                        dto.setDepartmentName(department.getName());
                    }
                    
                    // 设置预约时间
                    dto.setAppointmentTime(appointment.getVisitTime());
                    
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        // 查询所有预约
        List<Appointment> appointments = list(new QueryWrapper<Appointment>().orderByDesc("create_time"));
        
        // 转换为DTO
        return appointments.stream().map(appointment -> {
            AppointmentDTO dto = new AppointmentDTO();
            BeanUtils.copyProperties(appointment, dto);
            
            // 获取医生信息
            Doctor doctor = doctorService.getById(appointment.getDoctorId());
            if (doctor != null) {
                dto.setDoctorName(doctor.getName());
            }
            
            // 获取科室信息
            Department department = departmentService.getById(appointment.getDepartmentId());
            if (department != null) {
                dto.setDepartmentName(department.getName());
            }
            
            // 获取患者信息
            User patient = userService.getById(appointment.getUserId());
            if (patient != null) {
                dto.setPatientName(patient.getName());
                dto.setPatientPhone(patient.getPhone());
            }
            
            // 设置预约时间
            dto.setAppointmentTime(appointment.getVisitTime());
            
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentDTO updateAppointmentStatus(String id, String status, String reviewReason) {
        // 查询预约
        Appointment appointment = getById(id);
        if (appointment == null) {
            throw new RuntimeException("预约不存在");
        }
        
        // 更新状态
        appointment.setStatus(status);
        appointment.setReviewReason(reviewReason);
        appointment.setUpdateTime(LocalDateTime.now());
        updateById(appointment);
        
        // 查询医生和科室信息
        Doctor doctor = doctorService.getById(appointment.getDoctorId());
        Department department = departmentService.getById(appointment.getDepartmentId());
        
        // 转换为DTO返回
        AppointmentDTO result = new AppointmentDTO();
        BeanUtils.copyProperties(appointment, result);
        if (doctor != null) {
            result.setDoctorName(doctor.getName());
        }
        if (department != null) {
            result.setDepartmentName(department.getName());
        }
        result.setAppointmentTime(appointment.getVisitTime());
        
        return result;
    }

    @Override
    public IPage<AppointmentDTO> getAppointmentsByPage(Page<Appointment> page, AppointmentQueryDTO query) {
        // 构建查询条件
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(query.getDoctorId())) {
            queryWrapper.eq("doctor_id", query.getDoctorId());
        }
        if (StringUtils.hasText(query.getDepartmentId())) {
            queryWrapper.eq("department_id", query.getDepartmentId());
        }
        if (StringUtils.hasText(query.getStatus())) {
            queryWrapper.eq("status", query.getStatus());
        }
        if (query.getStartDate() != null) {
            queryWrapper.ge("visit_time", query.getStartDate().atStartOfDay());
        }
        if (query.getEndDate() != null) {
            queryWrapper.le("visit_time", query.getEndDate().plusDays(1).atStartOfDay());
        }
        
        // 预约号查询
        if (StringUtils.hasText(query.getAppointmentNo())) {
            queryWrapper.like("id", query.getAppointmentNo());
        }
        
        // 添加排序
        if (StringUtils.hasText(query.getSortField())) {
            boolean isAsc = "asc".equalsIgnoreCase(query.getSortOrder());
            queryWrapper.orderBy(true, isAsc, query.getSortField());
        } else {
            // 默认按创建时间倒序排序
            queryWrapper.orderByDesc("create_time");
        }
        
        // 执行分页查询
        IPage<Appointment> appointmentIPage = page(page, queryWrapper);
        
        // 转换结果
        IPage<AppointmentDTO> result = appointmentIPage.convert(appointment -> {
            AppointmentDTO dto = new AppointmentDTO();
            BeanUtils.copyProperties(appointment, dto);
            
            // 获取医生信息
            Doctor doctor = doctorService.getById(appointment.getDoctorId());
            if (doctor != null) {
                dto.setDoctorName(doctor.getName());
            }
            
            // 获取科室信息
            Department department = departmentService.getById(appointment.getDepartmentId());
            if (department != null) {
                dto.setDepartmentName(department.getName());
            }
            
            // 获取患者信息
            User patient = userService.getById(appointment.getUserId());
            if (patient != null) {
                // 根据查询条件过滤患者信息
                boolean matchPatient = true;
                if (StringUtils.hasText(query.getPatientName())) {
                    matchPatient = patient.getName().contains(query.getPatientName());
                }
                if (matchPatient && StringUtils.hasText(query.getPatientPhone())) {
                    matchPatient = patient.getPhone().contains(query.getPatientPhone());
                }
                if (!matchPatient) {
                    return null;
                }
                
                dto.setPatientName(patient.getName());
                dto.setPatientPhone(patient.getPhone());
            }
            
            // 设置预约时间
            dto.setAppointmentTime(appointment.getVisitTime());
            
            return dto;
        });
        
        // 过滤掉不匹配的记录
        List<AppointmentDTO> filteredRecords = result.getRecords().stream()
            .filter(dto -> dto != null)
            .collect(Collectors.toList());
        result.setRecords(filteredRecords);
        result.setTotal(filteredRecords.size());
        
        return result;
    }

    @Override
    public AppointmentStatisticsDTO getAppointmentStatistics() {
        try {
            // 获取当前时间
            LocalDateTime now = LocalDateTime.now();
            LocalDate today = LocalDate.now();
            
            // 获取本周开始时间
            LocalDateTime weekStart = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).atStartOfDay();
            // 获取本月开始时间
            LocalDateTime monthStart = today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
            
            // 构建查询条件
            QueryWrapper<Appointment> totalQuery = new QueryWrapper<>();
            QueryWrapper<Appointment> todayQuery = new QueryWrapper<Appointment>()
                .ge("create_time", today.atStartOfDay());
            QueryWrapper<Appointment> weekQuery = new QueryWrapper<Appointment>()
                .ge("create_time", weekStart);
            QueryWrapper<Appointment> monthQuery = new QueryWrapper<Appointment>()
                .ge("create_time", monthStart);
            
            // 统计总预约数
            long totalAppointments = count(totalQuery);
            
            // 统计各状态预约数量
            List<Appointment> allAppointments = list();
            Map<String, Long> statusCount = allAppointments.stream()
                .collect(Collectors.groupingBy(
                    appointment -> appointment.getStatus() != null ? appointment.getStatus() : "UNKNOWN",
                    Collectors.counting()
                ));
                
            // 统计各科室预约数量
            Map<String, Long> departmentCount = allAppointments.stream()
                .filter(appointment -> appointment.getDepartmentId() != null)
                .collect(Collectors.groupingBy(
                    Appointment::getDepartmentId,
                    Collectors.counting()
                ));
                
            // 统计今日预约数
            long todayAppointments = count(todayQuery);
            
            // 统计本周预约数
            long weekAppointments = count(weekQuery);
            
            // 统计本月预约数
            long monthAppointments = count(monthQuery);
            
            log.info("统计结果 - 总预约数: {}, 今日预约数: {}, 本周预约数: {}, 本月预约数: {}", 
                    totalAppointments, todayAppointments, weekAppointments, monthAppointments);
            
            // 构建并返回统计结果
            return AppointmentStatisticsDTO.builder()
                .totalAppointments(totalAppointments)
                .statusCount(statusCount)
                .departmentCount(departmentCount)
                .todayAppointments(todayAppointments)
                .weekAppointments(weekAppointments)
                .monthAppointments(monthAppointments)
                .build();
                
        } catch (Exception e) {
            log.error("获取预约统计数据时发生错误", e);
            throw new RuntimeException("获取预约统计数据失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO) {
        log.info("开始更新预约信息: {}", appointmentDTO);
        
        // 1. 查询预约是否存在
        Appointment appointment = getById(appointmentDTO.getId());
        if (appointment == null) {
            throw new RuntimeException("预约不存在");
        }
        
        // 2. 验证医生和科室是否存在
        if (appointmentDTO.getDoctorId() != null) {
            Doctor doctor = doctorService.getById(appointmentDTO.getDoctorId());
            if (doctor == null) {
                throw new RuntimeException("医生不存在");
            }
        }
        
        if (appointmentDTO.getDepartmentId() != null) {
            Department department = departmentService.getById(appointmentDTO.getDepartmentId());
            if (department == null) {
                throw new RuntimeException("科室不存在");
            }
        }
        
        // 3. 更新预约信息
        BeanUtils.copyProperties(appointmentDTO, appointment, "id", "userId", "createTime", "status");
        
        // 4. 设置就诊时间和更新时间
        if (appointmentDTO.getAppointmentTime() != null) {
            appointment.setVisitTime(appointmentDTO.getAppointmentTime());
        }
        appointment.setUpdateTime(LocalDateTime.now());
        
        log.info("更新预约信息: {}", appointment);
        updateById(appointment);
        
        // 5. 查询更新后的完整信息
        return getAppointmentById(appointment.getId());
    }

    @Override
    @Transactional
    public int batchUpdateAmount(BigDecimal amount) {
        log.info("开始批量更新预约金额: {}", amount);
        
        try {
            // 创建更新条件
            LambdaUpdateWrapper<Appointment> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Appointment::getAmount, amount)
                        .set(Appointment::getUpdateTime, LocalDateTime.now())
                        .isNull(Appointment::getAmount);
            
            // 执行更新
            int count = baseMapper.update(null, updateWrapper);
            log.info("批量更新预约金额完成，更新数量: {}", count);
            
            return count;
        } catch (Exception e) {
            log.error("批量更新预约金额失败", e);
            throw new BusinessException("批量更新预约金额失败：" + e.getMessage());
        }
    }

    @Override
    public List<AppointmentDTO> getDoctorAppointments(String doctorId) {
        log.info("获取医生[{}]的预约列表", doctorId);
        
        // 查询该医生的所有预约
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Appointment::getDoctorId, doctorId)
               .orderByDesc(Appointment::getCreateTime);
               
        List<Appointment> appointments = list(wrapper);
        
        // 转换为DTO
        return appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        BeanUtils.copyProperties(appointment, dto);
        
        // 获取医生信息
        Doctor doctor = doctorService.getById(appointment.getDoctorId());
        if (doctor != null) {
            dto.setDoctorName(doctor.getName());
        }
        
        // 获取科室信息
        Department department = departmentService.getById(appointment.getDepartmentId());
        if (department != null) {
            dto.setDepartmentName(department.getName());
        }
        
        // 获取患者信息
        User patient = userService.getById(appointment.getUserId());
        if (patient != null) {
            dto.setPatientName(patient.getName());
            dto.setPatientPhone(patient.getPhone());
        }
        
        // 设置预约时间
        dto.setAppointmentTime(appointment.getVisitTime());
        
        return dto;
    }
}
