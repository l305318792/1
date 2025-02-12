package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.ScheduleDTO;
import com.yunchuan.medical.dto.ScheduleQueryDTO;
import com.yunchuan.medical.dto.BatchScheduleDTO;
import com.yunchuan.medical.dto.ScheduleFormDTO;
import com.yunchuan.medical.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班服务接口
 * @author yunchuan
 * @since 1.0.0
 */
public interface ScheduleService {
    /**
     * 根据ID获取排班信息
     * @param id 排班ID
     * @return 排班信息
     */
    ScheduleDTO getScheduleById(String id);
    
    /**
     * 创建新的排班
     * @param schedule 排班信息
     * @return 创建的排班信息
     */
    ScheduleDTO createSchedule(Schedule schedule);
    
    /**
     * 更新排班信息
     * @param schedule 排班信息
     * @return 更新后的排班信息
     */
    ScheduleDTO updateSchedule(Schedule schedule);
    
    /**
     * 删除排班
     * @param id 排班ID
     * @return 是否删除成功
     */
    boolean deleteSchedule(String id);
    
    /**
     * 获取排班列表
     * @return 排班列表
     */
    List<ScheduleDTO> listSchedules();

    /**
     * 按条件查询排班列表
     * @param queryDTO 查询条件
     * @return 排班列表
     */
    List<ScheduleDTO> getScheduleList(ScheduleQueryDTO queryDTO);

    /**
     * 获取医生排班列表
     * @param doctorId 医生ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班列表
     */
    List<ScheduleDTO> getDoctorSchedules(String doctorId, LocalDate startDate, LocalDate endDate);

    /**
     * 增加已预约人数
     * @param scheduleId 排班ID
     */
    void incrementAppointedCount(String scheduleId);

    /**
     * 减少已预约人数
     * @param scheduleId 排班ID
     */
    void decrementAppointedCount(String scheduleId);

    /**
     * 更新排班状态
     * @param scheduleId 排班ID
     * @param status 状态
     */
    void updateStatus(String scheduleId, String status);

    /**
     * 批量更新排班状态
     * @param scheduleIds 排班ID列表
     * @param status 状态
     */
    void batchUpdateStatus(List<String> scheduleIds, String status);

    /**
     * 批量删除排班
     * @param scheduleIds 排班ID列表
     */
    void batchDeleteSchedule(List<String> scheduleIds);

    /**
     * 批量创建排班
     * @param formDTO 排班表单
     */
    void batchCreateSchedule(ScheduleFormDTO formDTO);

    /**
     * 批量添加排班
     * @param batchDTO 批量排班信息
     */
    void batchAddSchedule(BatchScheduleDTO batchDTO);

    /**
     * 批量更新排班
     * @param batchDTO 批量排班信息
     */
    void batchUpdateSchedule(BatchScheduleDTO batchDTO);
} 