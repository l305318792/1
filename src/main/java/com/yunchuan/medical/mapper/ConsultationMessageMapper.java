package com.yunchuan.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunchuan.medical.entity.ConsultationMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 问诊消息 Mapper 接口
 */
@Mapper
public interface ConsultationMessageMapper extends BaseMapper<ConsultationMessage> {
    
    /**
     * 根据问诊ID查询消息列表
     */
    @Select("SELECT * FROM consultation_message WHERE consultation_id = #{consultationId} ORDER BY create_time ASC")
    List<ConsultationMessage> selectByConsultationId(String consultationId);
} 