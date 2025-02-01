package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.AppointmentDTO;
import java.util.List;

/**
 * 预约服务接口
 */
public interface AppointmentService {

    /**
     * 创建预约
     */
    AppointmentDTO createAppointment(AppointmentDTO appointment);

    /**
     * 支付预约
     */
    AppointmentDTO payAppointment(String id);

    /**
     * 取消预约
     */
    AppointmentDTO cancelAppointment(String id);

    /**
     * 根据ID获取预约
     */
    AppointmentDTO getAppointmentById(String id);

    /**
     * 获取当前用户的预约列表
     */
    List<AppointmentDTO> getMyAppointments();
}
