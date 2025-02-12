package com.yunchuan.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.entity.Holiday;
import org.apache.ibatis.annotations.Mapper;

/**
 * 节假日Mapper接口
 */
@Mapper
public interface HolidayMapper extends BaseMapper<Holiday> {
} 