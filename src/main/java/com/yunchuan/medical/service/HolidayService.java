package com.yunchuan.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunchuan.medical.entity.Holiday;
import java.time.LocalDate;
import java.util.List;

/**
 * 节假日服务接口
 */
public interface HolidayService extends IService<Holiday> {
    
    /**
     * 创建节假日
     */
    Holiday createHoliday(Holiday holiday);
    
    /**
     * 更新节假日
     */
    Holiday updateHoliday(String id, Holiday holiday);
    
    /**
     * 删除节假日
     */
    void deleteHoliday(String id);
    
    /**
     * 获取节假日
     */
    Holiday getHoliday(String id);
    
    /**
     * 获取指定日期范围内的节假日
     */
    List<Holiday> getHolidaysByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * 判断指定日期是否为节假日
     */
    boolean isHoliday(LocalDate date);
    
    /**
     * 判断指定日期是否为工作日
     */
    boolean isWorkday(LocalDate date);
    
    /**
     * 启用/禁用节假日
     */
    void updateHolidayStatus(String id, String status);
} 