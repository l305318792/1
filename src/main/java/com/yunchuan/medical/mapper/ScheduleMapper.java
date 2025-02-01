package com.yunchuan.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 排班Mapper接口
 */
@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {
}