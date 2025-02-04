package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunchuan.medical.common.exception.BusinessException;
import com.yunchuan.medical.dto.ScheduleDTO;
import com.yunchuan.medical.dto.ScheduleQueryDTO;
import com.yunchuan.medical.dto.BatchScheduleDTO;
import com.yunchuan.medical.dto.ScheduleFormDTO;
import com.yunchuan.medical.entity.Schedule;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.mapper.ScheduleMapper;
import com.yunchuan.medical.service.ScheduleService;
import com.yunchuan.medical.service.DoctorService;
import com.yunchuan.medical.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.time.DayOfWeek;

/**
 * 排班服务实现类
 */
@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private DepartmentService departmentService;

    @Override
    public ScheduleDTO getScheduleById(String id) {
        log.info("获取排班信息，ID：{}", id);
        Schedule schedule = scheduleMapper.selectById(id);
        if (schedule == null) {
            throw new BusinessException("排班不存在");
        }
        return convertToDTO(schedule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScheduleDTO createSchedule(Schedule schedule) {
        log.info("创建排班信息：{}", schedule);
        try {
            // 生成ID
            schedule.setId(UUID.randomUUID().toString().replace("-", ""));
            schedule.setCreateTime(LocalDateTime.now());
            schedule.setUpdateTime(LocalDateTime.now());
            
            // 设置初始值
            schedule.setAppointedCount(0);
            if (schedule.getStatus() == null) {
                schedule.setStatus("1");
            }
            
            scheduleMapper.insert(schedule);
            return convertToDTO(schedule);
        } catch (Exception e) {
            log.error("创建排班异常", e);
            throw new BusinessException("创建排班失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScheduleDTO updateSchedule(Schedule schedule) {
        log.info("更新排班信息：{}", schedule);
        if (schedule.getId() == null) {
            throw new BusinessException("排班ID不能为空");
        }
        
        Schedule existingSchedule = scheduleMapper.selectById(schedule.getId());
        if (existingSchedule == null) {
            throw new BusinessException("排班不存在");
        }
        
        try {
            schedule.setUpdateTime(LocalDateTime.now());
            scheduleMapper.updateById(schedule);
            return convertToDTO(schedule);
        } catch (Exception e) {
            log.error("更新排班异常", e);
            throw new BusinessException("更新排班失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSchedule(String id) {
        log.info("删除排班，ID：{}", id);
        try {
            return scheduleMapper.deleteById(id) > 0;
        } catch (Exception e) {
            log.error("删除排班异常", e);
            throw new BusinessException("删除排班失败：" + e.getMessage());
        }
    }

    @Override
    public List<ScheduleDTO> listSchedules() {
        log.info("获取排班列表");
        try {
            LambdaQueryWrapper<Schedule> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(Schedule::getCreateTime);
            List<Schedule> schedules = scheduleMapper.selectList(wrapper);
            return schedules.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取排班列表异常", e);
            throw new BusinessException("获取排班列表失败：" + e.getMessage());
        }
    }

    @Override
    public List<ScheduleDTO> getScheduleList(ScheduleQueryDTO queryDTO) {
        log.info("按条件查询排班列表：{}", queryDTO);
        try {
            LambdaQueryWrapper<Schedule> wrapper = new LambdaQueryWrapper<>();
            
            // 添加查询条件
            if (queryDTO.getDoctorId() != null && !queryDTO.getDoctorId().isEmpty()) {
                wrapper.eq(Schedule::getDoctorId, queryDTO.getDoctorId());
            }
            
            if (queryDTO.getDepartmentId() != null && !queryDTO.getDepartmentId().isEmpty()) {
                wrapper.eq(Schedule::getDepartmentId, queryDTO.getDepartmentId());
            }
            
            if (queryDTO.getStartDate() != null) {
                wrapper.ge(Schedule::getScheduleDate, queryDTO.getStartDate());
            }
            
            if (queryDTO.getEndDate() != null) {
                wrapper.le(Schedule::getScheduleDate, queryDTO.getEndDate());
            }
            
            if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
                wrapper.eq(Schedule::getStatus, queryDTO.getStatus());
            }
            
            // 按创建时间降序排序
            wrapper.orderByDesc(Schedule::getCreateTime);
            
            List<Schedule> schedules = scheduleMapper.selectList(wrapper);
            return schedules.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("查询排班列表异常", e);
            throw new BusinessException("查询排班列表失败：" + e.getMessage());
        }
    }

    @Override
    public List<ScheduleDTO> getDoctorSchedules(String doctorId, LocalDate startDate, LocalDate endDate) {
        log.info("获取医生排班列表，医生ID：{}，开始日期：{}，结束日期：{}", doctorId, startDate, endDate);
        try {
            LambdaQueryWrapper<Schedule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Schedule::getDoctorId, doctorId)
                  .ge(startDate != null, Schedule::getScheduleDate, startDate)
                  .le(endDate != null, Schedule::getScheduleDate, endDate)
                  .orderByAsc(Schedule::getScheduleDate)
                  .orderByAsc(Schedule::getPeriod);
            
            List<Schedule> schedules = scheduleMapper.selectList(wrapper);
            return schedules.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取医生排班列表异常", e);
            throw new BusinessException("获取医生排班列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementAppointedCount(String scheduleId) {
        log.info("增加排班已预约人数，ID：{}", scheduleId);
        try {
            Schedule schedule = scheduleMapper.selectById(scheduleId);
            if (schedule == null) {
                throw new BusinessException("排班不存在");
            }
            
            // 检查是否还有可预约名额
            if (schedule.getAppointedCount() >= schedule.getMaxAppointments()) {
                throw new BusinessException("该排班已约满");
            }
            
            // 更新预约人数
            schedule.setAppointedCount(schedule.getAppointedCount() + 1);
            schedule.setUpdateTime(LocalDateTime.now());
            
            // 如果约满了，更新状态
            if (schedule.getAppointedCount().equals(schedule.getMaxAppointments())) {
                schedule.setStatus("FULL");
            }
            
            scheduleMapper.updateById(schedule);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("增加排班已预约人数异常", e);
            throw new BusinessException("增加排班已预约人数失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decrementAppointedCount(String scheduleId) {
        log.info("减少排班已预约人数，ID：{}", scheduleId);
        try {
            Schedule schedule = scheduleMapper.selectById(scheduleId);
            if (schedule == null) {
                throw new BusinessException("排班不存在");
            }
            
            // 检查是否还能减少
            if (schedule.getAppointedCount() <= 0) {
                throw new BusinessException("当前排班没有预约记录");
            }
            
            // 更新预约人数
            schedule.setAppointedCount(schedule.getAppointedCount() - 1);
            schedule.setUpdateTime(LocalDateTime.now());
            
            // 如果之前是约满状态，现在有空位了，更新状态
            if ("FULL".equals(schedule.getStatus())) {
                schedule.setStatus("AVAILABLE");
            }
            
            scheduleMapper.updateById(schedule);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("减少排班已预约人数异常", e);
            throw new BusinessException("减少排班已预约人数失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String scheduleId, String status) {
        log.info("更新排班状态，ID：{}，状态：{}", scheduleId, status);
        try {
            Schedule schedule = scheduleMapper.selectById(scheduleId);
            if (schedule == null) {
                throw new BusinessException("排班不存在");
            }
            
            // 验证状态值
            if (!isValidStatus(status)) {
                throw new BusinessException("无效的状态值");
            }
            
            // 更新状态
            schedule.setStatus(status);
            schedule.setUpdateTime(LocalDateTime.now());
            scheduleMapper.updateById(schedule);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新排班状态异常", e);
            throw new BusinessException("更新排班状态失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateStatus(List<String> scheduleIds, String status) {
        log.info("批量更新排班状态，ID列表：{}，状态：{}", scheduleIds, status);
        try {
            if (scheduleIds == null || scheduleIds.isEmpty()) {
                throw new BusinessException("排班ID列表不能为空");
            }
            
            // 验证状态值
            if (!isValidStatus(status)) {
                throw new BusinessException("无效的状态值");
            }
            
            // 批量更新状态
            for (String scheduleId : scheduleIds) {
                Schedule schedule = scheduleMapper.selectById(scheduleId);
                if (schedule != null) {
                    schedule.setStatus(status);
                    schedule.setUpdateTime(LocalDateTime.now());
                    scheduleMapper.updateById(schedule);
                }
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量更新排班状态异常", e);
            throw new BusinessException("批量更新排班状态失败：" + e.getMessage());
        }
    }

    /**
     * 验证状态值是否有效
     */
    private boolean isValidStatus(String status) {
        if (status == null || status.isEmpty()) {
            return false;
        }
        // 可用的状态值：AVAILABLE-可预约，FULL-已约满，CANCELLED-已取消
        return "AVAILABLE".equals(status) || 
               "FULL".equals(status) || 
               "CANCELLED".equals(status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteSchedule(List<String> scheduleIds) {
        log.info("批量删除排班，ID列表：{}", scheduleIds);
        try {
            if (scheduleIds == null || scheduleIds.isEmpty()) {
                throw new BusinessException("排班ID列表不能为空");
            }
            
            for (String scheduleId : scheduleIds) {
                scheduleMapper.deleteById(scheduleId);
            }
        } catch (Exception e) {
            log.error("批量删除排班异常", e);
            throw new BusinessException("批量删除排班失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateSchedule(ScheduleFormDTO formDTO) {
        log.info("批量创建排班：{}", formDTO);
        try {
            validateScheduleForm(formDTO);
            
            List<LocalDate> scheduleDates = generateScheduleDates(formDTO);
            for (LocalDate date : scheduleDates) {
                Schedule schedule = new Schedule();
                schedule.setId(UUID.randomUUID().toString().replace("-", ""));
                schedule.setDoctorId(formDTO.getDoctorId());
                schedule.setDepartmentId(formDTO.getDepartmentId());
                schedule.setScheduleDate(date);
                schedule.setPeriod(formDTO.getPeriod());
                schedule.setMaxAppointments(formDTO.getMaxAppointments());
                schedule.setAppointedCount(0);
                schedule.setStatus("1");
                schedule.setRemark(formDTO.getRemark());
                schedule.setCreateTime(LocalDateTime.now());
                schedule.setUpdateTime(LocalDateTime.now());
                
                scheduleMapper.insert(schedule);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量创建排班异常", e);
            throw new BusinessException("批量创建排班失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddSchedule(BatchScheduleDTO batchDTO) {
        log.info("批量添加排班：{}", batchDTO);
        try {
            validateBatchSchedule(batchDTO);
            
            for (LocalDate date : batchDTO.getScheduleDates()) {
                for (String period : batchDTO.getPeriods()) {
                    Schedule schedule = new Schedule();
                    schedule.setId(UUID.randomUUID().toString().replace("-", ""));
                    schedule.setDoctorId(batchDTO.getDoctorId());
                    schedule.setDepartmentId(batchDTO.getDepartmentId());
                    schedule.setScheduleDate(date);
                    schedule.setPeriod(period);
                    schedule.setMaxAppointments(batchDTO.getMaxAppointments());
                    schedule.setAppointedCount(0);
                    schedule.setStatus("1");
                    schedule.setRemark(batchDTO.getRemark());
                    schedule.setCreateTime(LocalDateTime.now());
                    schedule.setUpdateTime(LocalDateTime.now());
                    
                    scheduleMapper.insert(schedule);
                }
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量添加排班异常", e);
            throw new BusinessException("批量添加排班失败：" + e.getMessage());
        }
    }

    /**
     * 验证排班表单数据
     */
    private void validateScheduleForm(ScheduleFormDTO formDTO) {
        if (formDTO.getDoctorId() == null || formDTO.getDoctorId().isEmpty()) {
            throw new BusinessException("医生ID不能为空");
        }
        if (formDTO.getDepartmentId() == null || formDTO.getDepartmentId().isEmpty()) {
            throw new BusinessException("科室ID不能为空");
        }
        if (formDTO.getScheduleDate() == null) {
            throw new BusinessException("排班日期不能为空");
        }
        if (formDTO.getPeriod() == null || formDTO.getPeriod().isEmpty()) {
            throw new BusinessException("排班时段不能为空");
        }
        if (formDTO.getMaxAppointments() == null || formDTO.getMaxAppointments() <= 0) {
            throw new BusinessException("最大预约人数必须大于0");
        }
    }

    /**
     * 验证批量排班数据
     */
    private void validateBatchSchedule(BatchScheduleDTO batchDTO) {
        if (batchDTO.getDoctorId() == null || batchDTO.getDoctorId().isEmpty()) {
            throw new BusinessException("医生ID不能为空");
        }
        if (batchDTO.getDepartmentId() == null || batchDTO.getDepartmentId().isEmpty()) {
            throw new BusinessException("科室ID不能为空");
        }
        if (batchDTO.getScheduleDates() == null || batchDTO.getScheduleDates().isEmpty()) {
            throw new BusinessException("排班日期列表不能为空");
        }
        if (batchDTO.getPeriods() == null || batchDTO.getPeriods().isEmpty()) {
            throw new BusinessException("排班时段列表不能为空");
        }
        if (batchDTO.getMaxAppointments() == null || batchDTO.getMaxAppointments() <= 0) {
            throw new BusinessException("最大预约人数必须大于0");
        }
    }

    /**
     * 根据重复类型生成排班日期列表
     */
    private List<LocalDate> generateScheduleDates(ScheduleFormDTO formDTO) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate startDate = formDTO.getScheduleDate();
        
        if ("ONCE".equals(formDTO.getRepeatType()) || formDTO.getRepeatType() == null) {
            // 单次排班
            dates.add(startDate);
        } else if ("DAILY".equals(formDTO.getRepeatType())) {
            // 每天重复
            for (int i = 0; i < formDTO.getRepeatCount(); i++) {
                dates.add(startDate.plusDays(i));
            }
        } else if ("WEEKLY".equals(formDTO.getRepeatType())) {
            // 每周重复
            if (formDTO.getRepeatDays() == null || formDTO.getRepeatDays().isEmpty()) {
                throw new BusinessException("每周重复时必须指定重复的星期");
            }
            
            LocalDate currentDate = startDate;
            int weeksAdded = 0;
            while (weeksAdded < formDTO.getRepeatCount()) {
                for (Integer dayOfWeek : formDTO.getRepeatDays()) {
                    if (dayOfWeek < 1 || dayOfWeek > 7) {
                        throw new BusinessException("无效的星期值：" + dayOfWeek);
                    }
                    
                    LocalDate scheduleDate = currentDate.with(DayOfWeek.of(dayOfWeek));
                    if (!scheduleDate.isBefore(startDate)) {
                        dates.add(scheduleDate);
                    }
                }
                currentDate = currentDate.plusWeeks(1);
                weeksAdded++;
            }
        } else {
            throw new BusinessException("无效的重复类型：" + formDTO.getRepeatType());
        }
        
        return dates;
    }

    /**
     * 将Schedule实体转换为DTO
     */
    private ScheduleDTO convertToDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, dto);
        
        // 获取医生名称
        Doctor doctor = doctorService.getById(schedule.getDoctorId());
        if (doctor != null) {
            dto.setDoctorName(doctor.getName());
        }
        
        // 获取科室名称
        Department department = departmentService.getById(schedule.getDepartmentId());
        if (department != null) {
            dto.setDepartmentName(department.getName());
        }
        
        return dto;
    }
}