package com.yunchuan.medical.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Future;

/**
 * 排班规则DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRuleDTO {
    
    private String id;

    @NotBlank(message = "医生ID不能为空")
    private String doctorId;
    
    private String doctorName;

    @NotBlank(message = "规则类型不能为空")
    private String ruleType;

    @NotNull(message = "适用星期不能为空")
    private List<Integer> weekDays;

    @NotNull(message = "时间段不能为空")
    private List<String> periods;

    @NotNull(message = "开始日期不能为空")
    @Future(message = "开始日期必须是将来日期")
    private LocalDate startDate;

    @NotNull(message = "结束日期不能为空")
    @Future(message = "结束日期必须是将来日期")
    private LocalDate endDate;

    @NotNull(message = "最大预约数不能为空")
    @Min(value = 1, message = "最大预约数必须大于0")
    private Integer maxAppointments;

    @Builder.Default
    private Integer priority = 0;

    @Builder.Default
    private String status = "ENABLED";

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 将weekDays列表转换为字符串
     */
    public String getWeekDaysString() {
        if (weekDays == null || weekDays.isEmpty()) {
            return "";
        }
        return String.join(",", weekDays.stream().map(String::valueOf).toList());
    }

    /**
     * 将periods列表转换为字符串
     */
    public String getPeriodsString() {
        if (periods == null || periods.isEmpty()) {
            return "";
        }
        return String.join(",", periods);
    }

    /**
     * 将字符串转换为weekDays列表
     */
    public void setWeekDaysFromString(String weekDaysStr) {
        if (weekDaysStr == null || weekDaysStr.isEmpty()) {
            return;
        }
        this.weekDays = List.of(weekDaysStr.split(",")).stream()
                .map(Integer::valueOf)
                .toList();
    }

    /**
     * 将字符串转换为periods列表
     */
    public void setPeriodsFromString(String periodsStr) {
        if (periodsStr == null || periodsStr.isEmpty()) {
            return;
        }
        this.periods = List.of(periodsStr.split(","));
    }
} 