package com.yunchuan.medical.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 预约查询条件DTO
 */
@Data
public class AppointmentQueryDTO {
    /**
     * 当前页码
     */
    private int current = 1;

    /**
     * 每页大小
     */
    private int size = 10;

    /**
     * 科室ID
     */
    private String departmentId;

    /**
     * 医生ID
     */
    private String doctorId;

    /**
     * 预约状态
     */
    private String status;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者手机号
     */
    private String patientPhone;

    /**
     * 预约号
     */
    private String appointmentNo;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向（asc/desc）
     */
    private String sortOrder;
} 