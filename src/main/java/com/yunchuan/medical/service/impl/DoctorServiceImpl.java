package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.mapper.DoctorMapper;
import com.yunchuan.medical.service.DoctorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.yunchuan.medical.entity.Schedule;
import com.yunchuan.medical.mapper.ScheduleMapper;
import com.yunchuan.medical.dto.ScheduleDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.service.DepartmentService;
import com.yunchuan.medical.dto.DoctorDTO;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

/**
 * <p>
 * 医生表 服务实现类
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {

    private static final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Autowired
    private ScheduleMapper scheduleMapper;
    
    @Autowired
    private DepartmentService departmentService;

    @Override
    @Transactional
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(doctorDTO, doctor);
        
        // 设置ID和初始值
        doctor.setId(UUID.randomUUID().toString().replace("-", ""));
        doctor.setStatus(1);
        doctor.setRating(BigDecimal.ZERO);
        doctor.setRatingCount(0);
        doctor.setConsultCount(0);
        doctor.setAppointmentCount(0);
        doctor.setCreateTime(LocalDateTime.now());
        doctor.setUpdateTime(LocalDateTime.now());
        
        // 保存医生信息
        save(doctor);
        
        // 返回创建后的医生信息
        BeanUtils.copyProperties(doctor, doctorDTO);
        return doctorDTO;
    }

    @Override
    @Transactional
    public DoctorDTO updateDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = getById(doctorDTO.getId());
        if (doctor == null) {
            throw new RuntimeException("医生不存在");
        }
        
        BeanUtils.copyProperties(doctorDTO, doctor);
        doctor.setUpdateTime(LocalDateTime.now());
        
        // 更新医生信息
        updateById(doctor);
        
        // 返回更新后的医生信息
        BeanUtils.copyProperties(doctor, doctorDTO);
        return doctorDTO;
    }

    @Override
    public DoctorDTO getDoctorById(String id) {
        Doctor doctor = getById(id);
        if (doctor == null) {
            throw new RuntimeException("医生不存在");
        }
        
        DoctorDTO doctorDTO = new DoctorDTO();
        BeanUtils.copyProperties(doctor, doctorDTO);
        return doctorDTO;
    }

    @Override
    @Transactional
    public void deleteDoctor(String id) {
        Doctor doctor = getById(id);
        if (doctor == null) {
            throw new RuntimeException("医生不存在");
        }
        
        removeById(id);
    }

    @Override
    public boolean updateStatus(String id, Integer status) {
        Doctor doctor = this.getById(id);
        if (doctor != null) {
            doctor.setStatus(status);
            return this.updateById(doctor);
        }
        return false;
    }

    private void enrichScheduleDTO(ScheduleDTO dto) {
        // 补充医生信息
        Doctor doctor = this.getById(dto.getDoctorId());
        if (doctor != null) {
            dto.setDoctorName(doctor.getName());
        }
        
        // 补充科室信息
        Department department = departmentService.getById(dto.getDepartmentId());
        if (department != null) {
            dto.setDepartmentName(department.getName());
        }
    }

    @Override
    public List<ScheduleDTO> getDoctorSchedule(String doctorId) {
        log.info("正在查询医生排班信息，医生ID: {}", doctorId);
        
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 获取一周后的日期
        LocalDate oneWeekLater = today.plusDays(7);
        
        // 构建查询条件
        LambdaQueryWrapper<Schedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Schedule::getDoctorId, doctorId)
                   .ge(Schedule::getScheduleDate, today)
                   .le(Schedule::getScheduleDate, oneWeekLater)
                   .orderByAsc(Schedule::getScheduleDate, Schedule::getPeriod);
        
        // 查询医生一周内的排班信息
        List<Schedule> schedules = scheduleMapper.selectList(queryWrapper);
        
        // 转换为DTO并补充信息
        List<ScheduleDTO> scheduleDTOs = schedules.stream()
            .map(schedule -> {
                ScheduleDTO dto = new ScheduleDTO();
                BeanUtils.copyProperties(schedule, dto);
                enrichScheduleDTO(dto);
                return dto;
            })
            .collect(Collectors.toList());
            
        log.info("成功获取医生排班信息，数量: {}", scheduleDTOs.size());
        return scheduleDTOs;
    }

    @Override
    public List<ScheduleDTO> getDoctorSchedule(String doctorId, LocalDate startDate, LocalDate endDate) {
        log.info("正在查询医生排班信息，医生ID: {}, 开始日期: {}, 结束日期: {}", doctorId, startDate, endDate);
        
        // 构建查询条件
        LambdaQueryWrapper<Schedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Schedule::getDoctorId, doctorId)
                   .ge(Schedule::getScheduleDate, startDate)
                   .le(Schedule::getScheduleDate, endDate)
                   .orderByAsc(Schedule::getScheduleDate, Schedule::getPeriod);
        
        // 查询医生排班信息
        List<Schedule> schedules = scheduleMapper.selectList(queryWrapper);
        
        // 转换为DTO并补充信息
        List<ScheduleDTO> scheduleDTOs = schedules.stream()
            .map(schedule -> {
                ScheduleDTO dto = new ScheduleDTO();
                BeanUtils.copyProperties(schedule, dto);
                enrichScheduleDTO(dto);
                return dto;
            })
            .collect(Collectors.toList());
            
        log.info("成功获取医生排班信息，数量: {}", scheduleDTOs.size());
        return scheduleDTOs;
    }

    @Override
    public Doctor getByUserId(String userId) {
        log.info("根据用户ID查询医生信息: {}", userId);
        return lambdaQuery()
                .eq(Doctor::getUserId, userId)
                .one();
    }

    @Override
    public List<DoctorDTO> getDoctorsByDepartment(String departmentId) {
        log.info("获取科室{}的医生列表", departmentId);
        
        // 构建查询条件
        List<Doctor> doctors = lambdaQuery()
                .eq(Doctor::getDepartmentId, departmentId)
                .eq(Doctor::getStatus, 1)  // 只查询正常状态的医生
                .orderByDesc(Doctor::getRating)  // 按评分降序排序
                .list();
        
        // 转换为DTO
        return doctors.stream()
                .map(doctor -> {
                    DoctorDTO dto = new DoctorDTO();
                    BeanUtils.copyProperties(doctor, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
