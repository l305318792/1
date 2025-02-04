package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunchuan.medical.dto.AppointmentDTO;
import com.yunchuan.medical.dto.AppointmentQueryDTO;
import com.yunchuan.medical.entity.Appointment;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.mapper.AppointmentMapper;
import com.yunchuan.medical.service.AppointmentService;
import com.yunchuan.medical.service.UserService;
import com.yunchuan.medical.service.DoctorService;
import com.yunchuan.medical.service.DepartmentService;
import com.yunchuan.medical.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private static final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    public AppointmentServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        try {
            // 获取当前用户ID
            String userId = SecurityUtils.getCurrentUserId();
            log.info("创建预约 - 当前用户ID: {}", userId);
            
            // 查询医生和科室信息
            log.info("查询医生信息 - 医生ID: {}", appointmentDTO.getDoctorId());
            Doctor doctor = doctorService.getById(appointmentDTO.getDoctorId());
            if (doctor == null) {
                log.error("医生不存在 - 医生ID: {}", appointmentDTO.getDoctorId());
                throw new RuntimeException("医生不存在");
            }
            log.info("获取到医生信息: {}", doctor);
            
            log.info("查询科室信息 - 科室ID: {}", appointmentDTO.getDepartmentId());
            Department department = departmentService.getById(appointmentDTO.getDepartmentId());
            if (department == null) {
                log.error("科室不存在 - 科室ID: {}", appointmentDTO.getDepartmentId());
                throw new RuntimeException("科室不存在");
            }
            log.info("获取到科室信息: {}", department);
            
            // 查询当前排班下的预约数量，用于生成序号
            log.info("查询当前排班预约数量 - 排班ID: {}", appointmentDTO.getScheduleId());
            QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("schedule_id", appointmentDTO.getScheduleId());
            long count = count(queryWrapper);
            log.info("当前排班已有预约数量: {}", count);
            
            // 创建预约实体
            Appointment appointment = new Appointment();
            
            // 设置基本信息
            appointment.setUserId(userId);
            appointment.setDoctorId(appointmentDTO.getDoctorId());
            appointment.setDepartmentId(appointmentDTO.getDepartmentId());
            appointment.setScheduleId(appointmentDTO.getScheduleId());
            appointment.setStatus("UNPAID"); // 待支付
            appointment.setSequenceNumber((int)(count + 1)); // 设置序号
            appointment.setVisitTime(appointmentDTO.getAppointmentTime()); // 设置就诊时间
            appointment.setCreateTime(LocalDateTime.now());
            appointment.setUpdateTime(LocalDateTime.now());
            
            log.info("准备保存预约信息: {}", appointment);
            
            // 保存预约
            save(appointment);
            log.info("预约保存成功 - 预约ID: {}", appointment.getId());
            
            // 转换为DTO返回
            AppointmentDTO result = new AppointmentDTO();
            BeanUtils.copyProperties(appointment, result);
            result.setDoctorName(doctor.getName());
            result.setDepartmentName(department.getName());
            result.setAppointmentTime(appointment.getVisitTime()); // 设置预约时间
            
            log.info("预约创建完成，返回结果: {}", result);
            return result;
        } catch (Exception e) {
            log.error("创建预约时发生异常", e);
            throw new RuntimeException("创建预约失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AppointmentDTO payAppointment(String id) {
        // 获取当前用户ID
        String userId = SecurityUtils.getCurrentUserId();
        log.info("当前用户ID: {}", userId);
        
        // 查询预约
        Appointment appointment = getById(id);
        log.info("查询到的预约信息: {}", appointment);
        if (appointment == null) {
            throw new RuntimeException("预约不存在");
        }
        
        // 验证是否是当前用户的预约
        log.info("预约用户ID: {}, 当前用户ID: {}", appointment.getUserId(), userId);
        if (!appointment.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此预约");
        }
        
        // 验证预约状态
        log.info("预约当前状态: {}", appointment.getStatus());
        if (!"UNPAID".equals(appointment.getStatus())) {
            throw new RuntimeException("预约状态不正确");
        }
        
        // 更新预约状态
        appointment.setStatus("PAID");
        appointment.setUpdateTime(LocalDateTime.now());
        log.info("更新预约状态为已支付: {}", appointment);
        updateById(appointment);
        
        // 转换为DTO返回
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
        List<Appointment> appointments = list();
        
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
        if (StringUtils.hasText(query.getUserId())) {
            queryWrapper.eq("user_id", query.getUserId());
        }
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
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc("create_time");
        
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
            
            // 设置预约时间
            dto.setAppointmentTime(appointment.getVisitTime());
            
            return dto;
        });
        
        return result;
    }
}
