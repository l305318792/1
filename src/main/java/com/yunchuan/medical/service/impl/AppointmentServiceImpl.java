package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunchuan.medical.dto.AppointmentDTO;
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

    public AppointmentServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        // 获取当前用户ID
        String userId = "1885661643981516802"; // 使用登录时返回的userId
        
        // 查询医生和科室信息
        Doctor doctor = doctorService.getById(appointmentDTO.getDoctorId());
        if (doctor == null) {
            throw new RuntimeException("医生不存在");
        }
        Department department = departmentService.getById(appointmentDTO.getDepartmentId());
        if (department == null) {
            throw new RuntimeException("科室不存在");
        }
        
        // 查询当前排班下的预约数量，用于生成序号
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("schedule_id", appointmentDTO.getScheduleId());
        long count = count(queryWrapper);
        
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
        
        // 保存预约
        save(appointment);
        
        // 转换为DTO返回
        AppointmentDTO result = new AppointmentDTO();
        BeanUtils.copyProperties(appointment, result);
        result.setDoctorName(doctor.getName());
        result.setDepartmentName(department.getName());
        result.setAppointmentTime(appointment.getVisitTime()); // 设置预约时间
        return result;
    }

    @Override
    @Transactional
    public AppointmentDTO payAppointment(String id) {
        // 获取当前用户ID
        String userId = "1885661643981516802"; // 使用固定的用户ID
        
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
        if (!"UNPAID".equals(appointment.getStatus())) {
            throw new RuntimeException("预约状态不正确");
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
        
        // 更新预约状态
        appointment.setStatus("PAID");
        appointment.setUpdateTime(LocalDateTime.now());
        updateById(appointment);
        
        // 转换为DTO返回
        AppointmentDTO result = new AppointmentDTO();
        BeanUtils.copyProperties(appointment, result);
        result.setDoctorName(doctor.getName());
        result.setDepartmentName(department.getName());
        result.setAppointmentTime(appointment.getVisitTime());
        return result;
    }

    @Override
    @Transactional
    public AppointmentDTO cancelAppointment(String id) {
        // 获取当前用户ID
        String userId = "1885661643981516802"; // 使用固定的用户ID
        
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
        String userId = "1885661643981516802"; // 使用固定的用户ID
        
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
}
