package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.dto.ConsultMessageDTO;
import com.yunchuan.medical.dto.ConsultMessageFormDTO;
import com.yunchuan.medical.entity.ConsultMessage;
import com.yunchuan.medical.mapper.ConsultMessageMapper;
import com.yunchuan.medical.service.AiService;
import com.yunchuan.medical.service.ConsultService;
import com.yunchuan.medical.util.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 咨询服务实现
 */
@Service
public class ConsultServiceImpl implements ConsultService {

    private final ConsultMessageMapper messageMapper;
    private final AiService aiService;

    public ConsultServiceImpl(ConsultMessageMapper messageMapper, AiService aiService) {
        this.messageMapper = messageMapper;
        this.aiService = aiService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConsultMessageDTO sendMessage(ConsultMessageFormDTO form) {
        String userId = SecurityUtil.getCurrentUserId();
        
        // 保存用户消息
        ConsultMessage userMessage = new ConsultMessage();
        userMessage.setId(UUID.randomUUID().toString());
        userMessage.setUserId(userId);
        userMessage.setContent(form.getContent());
        userMessage.setType("USER");
        userMessage.setStatus("REPLIED");
        userMessage.setCreateTime(LocalDateTime.now());
        userMessage.setUpdateTime(LocalDateTime.now());
        
        messageMapper.insert(userMessage);
        
        // 获取AI回复
        String aiReply = aiService.getAiReply(form.getContent());
        
        // 保存AI回复
        ConsultMessage aiMessage = new ConsultMessage();
        aiMessage.setId(UUID.randomUUID().toString());
        aiMessage.setUserId(userId);
        aiMessage.setContent(aiReply);
        aiMessage.setType("AI");
        aiMessage.setStatus("REPLIED");
        aiMessage.setCreateTime(LocalDateTime.now());
        aiMessage.setUpdateTime(LocalDateTime.now());
        
        messageMapper.insert(aiMessage);
        
        return convertToDTO(aiMessage);
    }

    @Override
    public List<ConsultMessageDTO> getMyMessages() {
        String userId = SecurityUtil.getCurrentUserId();
        return messageMapper.selectByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ConsultMessageDTO convertToDTO(ConsultMessage message) {
        if (message == null) {
            return null;
        }
        ConsultMessageDTO dto = new ConsultMessageDTO();
        BeanUtils.copyProperties(message, dto);
        return dto;
    }
} 