package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.ConsultMessageDTO;
import com.yunchuan.medical.dto.ConsultMessageFormDTO;
import java.util.List;

/**
 * 咨询服务接口
 */
public interface ConsultService {
    
    /**
     * 发送咨询消息
     */
    ConsultMessageDTO sendMessage(ConsultMessageFormDTO form);
    
    /**
     * 获取我的咨询记录
     */
    List<ConsultMessageDTO> getMyMessages();
} 