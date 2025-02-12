package com.yunchuan.medical.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 问诊消息实体类
 */
@Data
@TableName("consultation_message")
public class ConsultationMessage {
    /**
     * 消息ID
     */
    private String id;

    /**
     * 问诊ID
     */
    private String consultationId;

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 发送者类型（PATIENT/DOCTOR/AI）
     */
    private String senderType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型（TEXT/IMAGE）
     */
    private String messageType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 