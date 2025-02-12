package com.yunchuan.medical.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 视频通话控制器
 */
@Controller
public class VideoCallController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    // 用于存储房间信息
    private static final ConcurrentHashMap<String, Integer> rooms = new ConcurrentHashMap<>();

    /**
     * 处理offer信令
     */
    @MessageMapping("/video/offer/{roomId}")
    public void handleOffer(@DestinationVariable String roomId, String offerMessage) {
        rooms.compute(roomId, (key, count) -> count == null ? 1 : count + 1);
        messagingTemplate.convertAndSend("/topic/video/" + roomId, offerMessage);
    }

    /**
     * 处理answer信令
     */
    @MessageMapping("/video/answer/{roomId}")
    public void handleAnswer(@DestinationVariable String roomId, String answerMessage) {
        messagingTemplate.convertAndSend("/topic/video/" + roomId, answerMessage);
    }

    /**
     * 处理ICE候选者信息
     */
    @MessageMapping("/video/ice/{roomId}")
    public void handleIceCandidate(@DestinationVariable String roomId, String iceMessage) {
        messagingTemplate.convertAndSend("/topic/video/" + roomId, iceMessage);
    }
} 