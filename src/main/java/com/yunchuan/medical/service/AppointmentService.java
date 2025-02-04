package com.yunchuan.medical.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunchuan.medical.dto.AppointmentDTO;
import com.yunchuan.medical.dto.AppointmentQueryDTO;
import com.yunchuan.medical.entity.Appointment;
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
    
    /**
     * 获取所有预约列表（管理员使用）
     */
    List<AppointmentDTO> getAllAppointments();

    /**
     * 更新预约状态（管理员使用）
     * @param id 预约ID
     * @param status 新状态
     * @param reviewReason 审核原因（可选）
     * @return 更新后的预约信息
     */
    AppointmentDTO updateAppointmentStatus(String id, String status, String reviewReason);

    /**
     * 分页查询预约列表
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<AppointmentDTO> getAppointmentsByPage(Page<Appointment> page, AppointmentQueryDTO query);
}
