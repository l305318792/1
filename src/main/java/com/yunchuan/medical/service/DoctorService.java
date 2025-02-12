package com.yunchuan.medical.service;

import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.dto.ScheduleDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.time.LocalDate;
import com.yunchuan.medical.dto.DoctorDTO;

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
     * 创建医生
     * @param doctorDTO 医生信息
     * @return 创建后的医生信息
     */
    DoctorDTO createDoctor(DoctorDTO doctorDTO);

    /**
     * 更新医生信息
     * @param doctorDTO 医生信息
     * @return 更新后的医生信息
     */
    DoctorDTO updateDoctor(DoctorDTO doctorDTO);

    /**
     * 根据ID获取医生信息
     * @param id 医生ID
     * @return 医生信息
     */
    DoctorDTO getDoctorById(String id);

    /**
     * 删除医生
     * @param id 医生ID
     */
    void deleteDoctor(String id);

    /**
     * 更新医生状态
     * @param id 医生ID
     * @param status 状态(1-正常，0-禁用)
     * @return 是否更新成功
     */
    boolean updateStatus(String id, Integer status);

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
    List<ScheduleDTO> getDoctorSchedule(String doctorId, LocalDate startDate, LocalDate endDate);

    /**
     * 根据用户ID获取医生信息
     * @param userId 用户ID
     * @return 医生信息
     */
    Doctor getByUserId(String userId);

    /**
     * 根据科室ID获取医生列表
     * @param departmentId 科室ID
     * @return 医生列表
     */
    List<DoctorDTO> getDoctorsByDepartment(String departmentId);
}
