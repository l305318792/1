package com.yunchuan.medical.mapper;

import com.yunchuan.medical.entity.ConsultMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 咨询消息数据访问接口
 * 
 * @author yunchuan
 * @since 1.0.0
 */
@Mapper
public interface ConsultMessageMapper {
    
    /**
     * 保存咨询消息
     */
    int insert(ConsultMessage message);
    
    /**
     * 查询咨询记录
     */
    List<ConsultMessage> selectMessages(@Param("userId") String userId,
                                      @Param("doctorId") String doctorId);
    
    /**
     * 查询用户咨询记录
     */
    List<ConsultMessage> selectByUserId(@Param("userId") String userId);
} 