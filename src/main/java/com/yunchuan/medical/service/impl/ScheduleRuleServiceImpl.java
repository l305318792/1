package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunchuan.medical.entity.ScheduleRule;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.entity.Schedule;
import com.yunchuan.medical.dto.ScheduleRuleDTO;
import com.yunchuan.medical.mapper.ScheduleRuleMapper;
import com.yunchuan.medical.mapper.DoctorMapper;
import com.yunchuan.medical.mapper.ScheduleMapper;
import com.yunchuan.medical.service.ScheduleRuleService;
import com.yunchuan.medical.service.HolidayService;
import com.yunchuan.medical.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 排班规则服务实现类
 */
@Slf4j
@Service
public class ScheduleRuleServiceImpl extends ServiceImpl<ScheduleRuleMapper, ScheduleRule> implements ScheduleRuleService {

    private final DoctorMapper doctorMapper;
    private final ScheduleMapper scheduleMapper;
    private final HolidayService holidayService;

    public ScheduleRuleServiceImpl(DoctorMapper doctorMapper, 
                                 ScheduleMapper scheduleMapper,
                                 HolidayService holidayService) {
        this.doctorMapper = doctorMapper;
        this.scheduleMapper = scheduleMapper;
        this.holidayService = holidayService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScheduleRuleDTO createRule(ScheduleRuleDTO ruleDTO) {
        // 验证医生是否存在
        Doctor doctor = doctorMapper.selectById(ruleDTO.getDoctorId());
        if (doctor == null) {
            throw new BusinessException("医生不存在");
        }

        // 验证日期
        if (ruleDTO.getEndDate().isBefore(ruleDTO.getStartDate())) {
            throw new BusinessException("结束日期不能早于开始日期");
        }

        // 验证星期
        for (Integer weekDay : ruleDTO.getWeekDays()) {
            if (weekDay < 1 || weekDay > 7) {
                throw new BusinessException("无效的星期值：" + weekDay);
            }
        }

        // 创建规则
        ScheduleRule rule = new ScheduleRule();
        rule.setId(UUID.randomUUID().toString());
        rule.setDoctorId(ruleDTO.getDoctorId());
        rule.setRuleType(ruleDTO.getRuleType());
        rule.setWeekDays(ruleDTO.getWeekDaysString());
        rule.setPeriods(ruleDTO.getPeriodsString());
        rule.setStartDate(ruleDTO.getStartDate());
        rule.setEndDate(ruleDTO.getEndDate());
        rule.setMaxAppointments(ruleDTO.getMaxAppointments());
        rule.setPriority(ruleDTO.getPriority());
        rule.setStatus(ruleDTO.getStatus());
        rule.setCreateTime(LocalDateTime.now());
        rule.setUpdateTime(LocalDateTime.now());

        baseMapper.insert(rule);

        // 返回DTO
        ruleDTO.setId(rule.getId());
        ruleDTO.setDoctorName(doctor.getName());
        ruleDTO.setCreateTime(rule.getCreateTime());
        ruleDTO.setUpdateTime(rule.getUpdateTime());

        return ruleDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScheduleRuleDTO updateRule(String id, ScheduleRuleDTO ruleDTO) {
        // 验证规则是否存在
        ScheduleRule existingRule = baseMapper.selectById(id);
        if (existingRule == null) {
            throw new BusinessException("排班规则不存在");
        }

        // 验证医生是否存在
        Doctor doctor = doctorMapper.selectById(ruleDTO.getDoctorId());
        if (doctor == null) {
            throw new BusinessException("医生不存在");
        }

        // 验证日期
        if (ruleDTO.getEndDate().isBefore(ruleDTO.getStartDate())) {
            throw new BusinessException("结束日期不能早于开始日期");
        }

        // 验证星期
        for (Integer weekDay : ruleDTO.getWeekDays()) {
            if (weekDay < 1 || weekDay > 7) {
                throw new BusinessException("无效的星期值：" + weekDay);
            }
        }

        // 更新规则
        existingRule.setDoctorId(ruleDTO.getDoctorId());
        existingRule.setRuleType(ruleDTO.getRuleType());
        existingRule.setWeekDays(ruleDTO.getWeekDaysString());
        existingRule.setPeriods(ruleDTO.getPeriodsString());
        existingRule.setStartDate(ruleDTO.getStartDate());
        existingRule.setEndDate(ruleDTO.getEndDate());
        existingRule.setMaxAppointments(ruleDTO.getMaxAppointments());
        existingRule.setPriority(ruleDTO.getPriority());
        existingRule.setStatus(ruleDTO.getStatus());
        existingRule.setUpdateTime(LocalDateTime.now());

        baseMapper.updateById(existingRule);

        // 返回DTO
        ruleDTO.setId(existingRule.getId());
        ruleDTO.setDoctorName(doctor.getName());
        ruleDTO.setCreateTime(existingRule.getCreateTime());
        ruleDTO.setUpdateTime(existingRule.getUpdateTime());

        return ruleDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRule(String id) {
        ScheduleRule rule = baseMapper.selectById(id);
        if (rule == null) {
            throw new BusinessException("排班规则不存在");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public ScheduleRuleDTO getRule(String id) {
        ScheduleRule rule = baseMapper.selectById(id);
        if (rule == null) {
            throw new BusinessException("排班规则不存在");
        }

        Doctor doctor = doctorMapper.selectById(rule.getDoctorId());
        
        ScheduleRuleDTO dto = new ScheduleRuleDTO();
        BeanUtils.copyProperties(rule, dto);
        dto.setDoctorName(doctor != null ? doctor.getName() : null);
        dto.setWeekDaysFromString(rule.getWeekDays());
        dto.setPeriodsFromString(rule.getPeriods());

        return dto;
    }

    @Override
    public List<ScheduleRuleDTO> getDoctorRules(String doctorId) {
        LambdaQueryWrapper<ScheduleRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduleRule::getDoctorId, doctorId)
               .orderByDesc(ScheduleRule::getCreateTime);

        List<ScheduleRule> rules = baseMapper.selectList(wrapper);
        Doctor doctor = doctorMapper.selectById(doctorId);

        return rules.stream().map(rule -> {
            ScheduleRuleDTO dto = new ScheduleRuleDTO();
            BeanUtils.copyProperties(rule, dto);
            dto.setDoctorName(doctor != null ? doctor.getName() : null);
            dto.setWeekDaysFromString(rule.getWeekDays());
            dto.setPeriodsFromString(rule.getPeriods());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateSchedules(String doctorId, String ruleId) {
        // 获取排班规则
        ScheduleRule rule = baseMapper.selectById(ruleId);
        if (rule == null) {
            throw new BusinessException("排班规则不存在");
        }

        // 验证医生
        Doctor doctor = doctorMapper.selectById(doctorId);
        if (doctor == null) {
            throw new BusinessException("医生不存在");
        }

        // 获取规则的星期和时间段
        List<Integer> weekDays = List.of(rule.getWeekDays().split(",")).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        List<String> periods = List.of(rule.getPeriods().split(","));

        // 生成排班
        LocalDate currentDate = rule.getStartDate();
        while (!currentDate.isAfter(rule.getEndDate())) {
            // 检查是否是规则指定的工作日
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (weekDays.contains(dayOfWeek.getValue())) {
                // 检查是否是节假日
                if (holidayService.isWorkday(currentDate)) {
                    // 为每个时间段创建排班
                    for (String period : periods) {
                        Schedule schedule = new Schedule();
                        schedule.setId(UUID.randomUUID().toString());
                        schedule.setDoctorId(doctorId);
                        schedule.setDepartmentId(doctor.getDepartmentId());
                        schedule.setScheduleDate(currentDate);
                        schedule.setPeriod(period);
                        schedule.setMaxAppointments(rule.getMaxAppointments());
                        schedule.setAppointedCount(0);
                        schedule.setStatus("1");
                        schedule.setRemark("自动生成");
                        schedule.setCreateTime(LocalDateTime.now());
                        schedule.setUpdateTime(LocalDateTime.now());

                        scheduleMapper.insert(schedule);
                    }
                }
            }
            currentDate = currentDate.plusDays(1);
        }
    }

    @Override
    public List<ScheduleRuleDTO> previewSchedules(String doctorId, String ruleId) {
        // 获取排班规则
        ScheduleRule rule = baseMapper.selectById(ruleId);
        if (rule == null) {
            throw new BusinessException("排班规则不存在");
        }

        // 验证医生
        Doctor doctor = doctorMapper.selectById(doctorId);
        if (doctor == null) {
            throw new BusinessException("医生不存在");
        }

        // 获取规则的星期和时间段
        List<Integer> weekDays = List.of(rule.getWeekDays().split(",")).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        List<String> periods = List.of(rule.getPeriods().split(","));

        // 生成预览数据
        List<ScheduleRuleDTO> previewList = new ArrayList<>();
        LocalDate currentDate = rule.getStartDate();
        while (!currentDate.isAfter(rule.getEndDate())) {
            // 检查是否是规则指定的工作日
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (weekDays.contains(dayOfWeek.getValue()) && holidayService.isWorkday(currentDate)) {
                for (String period : periods) {
                    ScheduleRuleDTO preview = new ScheduleRuleDTO();
                    preview.setDoctorId(doctorId);
                    preview.setDoctorName(doctor.getName());
                    preview.setStartDate(currentDate);
                    preview.setPeriods(List.of(period));
                    preview.setMaxAppointments(rule.getMaxAppointments());
                    previewList.add(preview);
                }
            }
            currentDate = currentDate.plusDays(1);
        }

        return previewList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRuleStatus(String id, String status) {
        ScheduleRule rule = baseMapper.selectById(id);
        if (rule == null) {
            throw new BusinessException("排班规则不存在");
        }

        rule.setStatus(status);
        rule.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(rule);
    }
} 