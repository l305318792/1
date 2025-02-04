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
    public Doctor getDoctorById(String id) {
        log.info("正在查询医生信息，医生ID: {}", id);
        Doctor doctor = this.getById(id);
        if (doctor != null) {
            log.info("成功获取医生信息: {}", doctor);
        } else {
            log.warn("未找到医生信息，医生ID: {}", id);
        }
        return doctor;
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
        queryWrapper.eq(Schedule::getDoctorId, Long.parseLong(doctorId))
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
    public List<ScheduleDTO> getDoctorSchedule(Long doctorId, LocalDate startDate, LocalDate endDate) {
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
}
