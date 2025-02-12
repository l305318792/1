package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunchuan.medical.entity.Holiday;
import com.yunchuan.medical.mapper.HolidayMapper;
import com.yunchuan.medical.service.HolidayService;
import com.yunchuan.medical.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 节假日服务实现类
 */
@Slf4j
@Service
public class HolidayServiceImpl extends ServiceImpl<HolidayMapper, Holiday> implements HolidayService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Holiday createHoliday(Holiday holiday) {
        // 验证日期是否已存在
        LambdaQueryWrapper<Holiday> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Holiday::getHolidayDate, holiday.getHolidayDate());
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该日期已存在节假日记录");
        }

        // 设置ID和时间
        holiday.setId(UUID.randomUUID().toString());
        holiday.setCreateTime(LocalDateTime.now());
        holiday.setUpdateTime(LocalDateTime.now());

        // 设置默认状态
        if (holiday.getStatus() == null) {
            holiday.setStatus("ENABLED");
        }

        baseMapper.insert(holiday);
        return holiday;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Holiday updateHoliday(String id, Holiday holiday) {
        // 验证是否存在
        Holiday existingHoliday = baseMapper.selectById(id);
        if (existingHoliday == null) {
            throw new BusinessException("节假日记录不存在");
        }

        // 验证日期是否与其他记录冲突
        if (!existingHoliday.getHolidayDate().equals(holiday.getHolidayDate())) {
            LambdaQueryWrapper<Holiday> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Holiday::getHolidayDate, holiday.getHolidayDate())
                   .ne(Holiday::getId, id);
            if (baseMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("该日期已存在节假日记录");
            }
        }

        // 更新记录
        holiday.setId(id);
        holiday.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(holiday);

        return holiday;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHoliday(String id) {
        Holiday holiday = baseMapper.selectById(id);
        if (holiday == null) {
            throw new BusinessException("节假日记录不存在");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public Holiday getHoliday(String id) {
        Holiday holiday = baseMapper.selectById(id);
        if (holiday == null) {
            throw new BusinessException("节假日记录不存在");
        }
        return holiday;
    }

    @Override
    public List<Holiday> getHolidaysByDateRange(LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<Holiday> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(Holiday::getHolidayDate, startDate)
               .le(Holiday::getHolidayDate, endDate)
               .eq(Holiday::getStatus, "ENABLED")
               .orderByAsc(Holiday::getHolidayDate);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        LambdaQueryWrapper<Holiday> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Holiday::getHolidayDate, date)
               .eq(Holiday::getStatus, "ENABLED")
               .eq(Holiday::getHolidayType, "HOLIDAY");
        return baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean isWorkday(LocalDate date) {
        // 首先检查是否是节假日
        LambdaQueryWrapper<Holiday> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Holiday::getHolidayDate, date)
               .eq(Holiday::getStatus, "ENABLED");
        Holiday holiday = baseMapper.selectOne(wrapper);

        if (holiday != null) {
            // 如果是调休工作日，返回true
            if ("WORKDAY".equals(holiday.getHolidayType())) {
                return true;
            }
            // 如果是法定节假日，返回false
            if ("HOLIDAY".equals(holiday.getHolidayType())) {
                return false;
            }
        }

        // 如果不是节假日，检查是否是周末
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHolidayStatus(String id, String status) {
        Holiday holiday = baseMapper.selectById(id);
        if (holiday == null) {
            throw new BusinessException("节假日记录不存在");
        }

        holiday.setStatus(status);
        holiday.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(holiday);
    }
} 