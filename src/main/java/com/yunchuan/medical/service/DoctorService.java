package com.yunchuan.medical.service;

import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.dto.ScheduleDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.time.LocalDate;

/**
 * <p>
 * 医生表 服务类
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
public interface DoctorService extends IService<Doctor> {

    /**
     * 根据ID获取医生信息
     */
    Doctor getDoctorById(String id);

    /**
     * 更新医生状态
     */
    boolean updateStatus(String id, String status);

    /**
     * 获取医生排班信息
     */
    List<ScheduleDTO> getDoctorSchedule(String doctorId);

    /**
     * 获取医生排班信息
     * @param doctorId 医生ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班列表
     */
    List<ScheduleDTO> getDoctorSchedule(Long doctorId, LocalDate startDate, LocalDate endDate);
}
