package com.yunchuan.medical.dto;

import jakarta.validation.constraints.NotBlank;

public class AppointmentFormDTO {
    @NotBlank(message = "排班ID不能为空")
    private String scheduleId;

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
} 