package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunchuan.medical.dto.dashboard.DashboardStatisticsDTO;
import com.yunchuan.medical.entity.Appointment;
import com.yunchuan.medical.entity.Department;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.entity.PaymentRecord;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.mapper.AppointmentMapper;
import com.yunchuan.medical.mapper.DepartmentMapper;
import com.yunchuan.medical.mapper.DoctorMapper;
import com.yunchuan.medical.mapper.PaymentRecordMapper;
import com.yunchuan.medical.mapper.UserMapper;
import com.yunchuan.medical.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仪表盘服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserMapper userMapper;
    private final AppointmentMapper appointmentMapper;
    private final DepartmentMapper departmentMapper;
    private final DoctorMapper doctorMapper;
    private final PaymentRecordMapper paymentRecordMapper;

    @Override
    public DashboardStatisticsDTO getDashboardStatistics(LocalDate startDate, LocalDate endDate) {
        return DashboardStatisticsDTO.builder()
                .userTrend(getUserTrend(startDate, endDate))
                .appointmentStats(getAppointmentStats(startDate, endDate))
                .departmentRanks(getDepartmentRanks(startDate, endDate))
                .appointmentDynamics(getAppointmentDynamics(10))
                .todayOverview(getTodayOverview())
                .build();
    }

    @Override
    public DashboardStatisticsDTO.UserTrendDTO getUserTrend(LocalDate startDate, LocalDate endDate) {
        List<String> dates = new ArrayList<>();
        List<Integer> newUsers = new ArrayList<>();
        List<Integer> activeUsers = new ArrayList<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate.toString());
            
            LocalDateTime dayStart = currentDate.atStartOfDay();
            LocalDateTime dayEnd = currentDate.atTime(LocalTime.MAX);
            
            Long newUserCount = userMapper.selectCount(new LambdaQueryWrapper<User>()
                    .between(User::getCreateTime, dayStart, dayEnd));
            newUsers.add(newUserCount.intValue());
            
            Long activeUserCount = appointmentMapper.selectCount(new LambdaQueryWrapper<Appointment>()
                    .between(Appointment::getVisitTime, dayStart, dayEnd));
            activeUsers.add(activeUserCount.intValue());
            
            currentDate = currentDate.plusDays(1);
        }

        return DashboardStatisticsDTO.UserTrendDTO.builder()
                .dates(dates)
                .newUsers(newUsers)
                .activeUsers(activeUsers)
                .build();
    }

    @Override
    public DashboardStatisticsDTO.AppointmentStatsDTO getAppointmentStats(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Appointment> appointments = appointmentMapper.selectList(new LambdaQueryWrapper<Appointment>()
                .between(Appointment::getVisitTime, start, end));

        Map<String, Long> statusCount = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getStatus, Collectors.counting()));

        Map<LocalDate, Long> dailyCount = appointments.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getVisitTime().toLocalDate(),
                        Collectors.counting()
                ));

        List<DashboardStatisticsDTO.DailyAppointmentDTO> trend = new ArrayList<>();
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            trend.add(DashboardStatisticsDTO.DailyAppointmentDTO.builder()
                    .date(current.toString())
                    .count(dailyCount.getOrDefault(current, 0L).intValue())
                    .build());
            current = current.plusDays(1);
        }

        return DashboardStatisticsDTO.AppointmentStatsDTO.builder()
                .totalAppointments(appointments.size())
                .completedAppointments(statusCount.getOrDefault("COMPLETED", 0L).intValue())
                .pendingAppointments(statusCount.getOrDefault("PENDING", 0L).intValue())
                .cancelledAppointments(statusCount.getOrDefault("CANCELLED", 0L).intValue())
                .trend(trend)
                .build();
    }

    @Override
    public List<DashboardStatisticsDTO.DepartmentRankDTO> getDepartmentRanks(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<Department> departments = departmentMapper.selectList(null);
        Map<String, String> departmentNames = departments.stream()
                .collect(Collectors.toMap(Department::getId, Department::getName));

        List<Appointment> appointments = appointmentMapper.selectList(new LambdaQueryWrapper<Appointment>()
                .between(Appointment::getVisitTime, start, end));

        List<PaymentRecord> payments = paymentRecordMapper.selectList(new LambdaQueryWrapper<PaymentRecord>()
                .between(PaymentRecord::getPaymentTime, start, end)
                .eq(PaymentRecord::getStatus, "SUCCESS"));

        Map<String, DashboardStatisticsDTO.DepartmentRankDTO> departmentStats = new HashMap<>();

        appointments.forEach(appointment -> {
            String deptId = appointment.getDepartmentId();
            departmentStats.computeIfAbsent(deptId, id -> DashboardStatisticsDTO.DepartmentRankDTO.builder()
                    .departmentId(id)
                    .departmentName(departmentNames.get(id))
                    .appointmentCount(0)
                    .doctorCount(0)
                    .income(BigDecimal.ZERO)
                    .build())
                    .setAppointmentCount(departmentStats.get(deptId).getAppointmentCount() + 1);
        });

        Map<String, Long> doctorCounts = doctorMapper.selectList(null).stream()
                .collect(Collectors.groupingBy(Doctor::getDepartmentId, Collectors.counting()));

        departmentStats.forEach((deptId, stats) -> 
            stats.setDoctorCount(doctorCounts.getOrDefault(deptId, 0L).intValue()));

        return departmentStats.values().stream()
                .sorted(Comparator.comparing(DashboardStatisticsDTO.DepartmentRankDTO::getAppointmentCount).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<DashboardStatisticsDTO.AppointmentDynamicDTO> getAppointmentDynamics(int limit) {
        List<Appointment> appointments = appointmentMapper.selectList(new LambdaQueryWrapper<Appointment>()
                .orderByDesc(Appointment::getVisitTime)
                .last("LIMIT " + limit));

        Set<String> doctorIds = appointments.stream()
                .map(Appointment::getDoctorId)
                .collect(Collectors.toSet());
        Map<String, Doctor> doctors = doctorMapper.selectList(new LambdaQueryWrapper<Doctor>()
                .in(Doctor::getId, doctorIds))
                .stream()
                .collect(Collectors.toMap(Doctor::getId, d -> d));

        Set<String> departmentIds = appointments.stream()
                .map(Appointment::getDepartmentId)
                .collect(Collectors.toSet());
        Map<String, Department> departments = departmentMapper.selectList(new LambdaQueryWrapper<Department>()
                .in(Department::getId, departmentIds))
                .stream()
                .collect(Collectors.toMap(Department::getId, d -> d));

        return appointments.stream()
                .map(appointment -> {
                    Doctor doctor = doctors.get(appointment.getDoctorId());
                    Department department = departments.get(appointment.getDepartmentId());
                    return DashboardStatisticsDTO.AppointmentDynamicDTO.builder()
                            .appointmentId(appointment.getId())
                            .patientName("患者" + (appointment.getUserId() != null ? appointment.getUserId().substring(0, Math.min(3, appointment.getUserId().length())) : "未知")) // 脱敏处理
                            .doctorName(doctor != null ? doctor.getName() : "未知医生")
                            .departmentName(department != null ? department.getName() : "未知科室")
                            .appointmentTime(appointment.getVisitTime().toString())
                            .status(appointment.getStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public DashboardStatisticsDTO.TodayOverviewDTO getTodayOverview() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);

        Long todayAppointments = appointmentMapper.selectCount(new LambdaQueryWrapper<Appointment>()
                .between(Appointment::getVisitTime, todayStart, todayEnd));

        Long todayVisits = appointmentMapper.selectCount(new LambdaQueryWrapper<Appointment>()
                .between(Appointment::getVisitTime, todayStart, todayEnd)
                .eq(Appointment::getStatus, "COMPLETED"));

        BigDecimal todayIncome = paymentRecordMapper.selectList(new LambdaQueryWrapper<PaymentRecord>()
                .between(PaymentRecord::getPaymentTime, todayStart, todayEnd)
                .eq(PaymentRecord::getStatus, "SUCCESS"))
                .stream()
                .map(PaymentRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long todayNewUsers = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .between(User::getCreateTime, todayStart, todayEnd));

        return DashboardStatisticsDTO.TodayOverviewDTO.builder()
                .todayAppointments(todayAppointments.intValue())
                .todayVisits(todayVisits.intValue())
                .todayIncome(todayIncome)
                .todayNewUsers(todayNewUsers.intValue())
                .build();
    }
} 