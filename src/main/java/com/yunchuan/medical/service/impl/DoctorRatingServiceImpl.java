package com.yunchuan.medical.service.impl;

import com.yunchuan.medical.constant.Constants;
import com.yunchuan.medical.dto.DoctorRatingDTO;
import com.yunchuan.medical.dto.DoctorRatingFormDTO;
import com.yunchuan.medical.dto.AppointmentDetailDTO;
import com.yunchuan.medical.entity.Appointment;
import com.yunchuan.medical.entity.DoctorRating;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.mapper.AppointmentMapper;
import com.yunchuan.medical.mapper.DoctorMapper;
import com.yunchuan.medical.mapper.DoctorRatingMapper;
import com.yunchuan.medical.mapper.UserMapper;
import com.yunchuan.medical.service.DoctorRatingService;
import com.yunchuan.medical.util.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 医生评价服务实现
 */
@Service
public class DoctorRatingServiceImpl implements DoctorRatingService {

    private final DoctorRatingMapper doctorRatingMapper;
    private final AppointmentMapper appointmentMapper;
    private final DoctorMapper doctorMapper;
    private final UserMapper userMapper;

    public DoctorRatingServiceImpl(DoctorRatingMapper doctorRatingMapper,
                                 AppointmentMapper appointmentMapper,
                                 DoctorMapper doctorMapper,
                                 UserMapper userMapper) {
        this.doctorRatingMapper = doctorRatingMapper;
        this.appointmentMapper = appointmentMapper;
        this.doctorMapper = doctorMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DoctorRatingDTO createRating(DoctorRatingFormDTO form) {
        // 验证预约
        AppointmentDetailDTO appointment = appointmentMapper.selectDetailById(form.getAppointmentId());
        if (appointment == null) {
            throw new BusinessException("预约不存在");
        }
        
        String userId = SecurityUtil.getCurrentUserId();
        if (!userId.equals(appointment.getUserId())) {
            throw new BusinessException("无权评价此预约");
        }
        
        if (!Constants.APPOINTMENT_COMPLETED.equals(appointment.getStatus())) {
            throw new BusinessException("只能评价已完成的预约");
        }

        // 创建评价
        DoctorRating rating = new DoctorRating();
        rating.setId(UUID.randomUUID().toString());
        rating.setAppointmentId(form.getAppointmentId());
        rating.setDoctorId(appointment.getDoctorId());
        rating.setUserId(userId);
        rating.setServiceAttitude(form.getServiceAttitude());
        rating.setMedicalSkill(form.getMedicalSkill());
        rating.setMedicalEffect(form.getMedicalEffect());
        rating.setComment(form.getComment());
        rating.setCreateTime(LocalDateTime.now());
        rating.setUpdateTime(LocalDateTime.now());
        
        doctorRatingMapper.insert(rating);

        // 更新医生评分
        updateDoctorRating(appointment.getDoctorId());
        
        return convertToDTO(rating);
    }

    @Override
    public List<DoctorRatingDTO> getDoctorRatings(String doctorId) {
        return doctorRatingMapper.selectByDoctorId(doctorId);
    }

    @Override
    public List<DoctorRatingDTO> getMyRatings() {
        String userId = SecurityUtil.getCurrentUserId();
        return doctorRatingMapper.selectByUserId(userId);
    }

    private void updateDoctorRating(String doctorId) {
        List<DoctorRating> ratings = doctorRatingMapper.selectByDoctorId(doctorId)
                .stream()
                .map(dto -> {
                    DoctorRating rating = new DoctorRating();
                    rating.setServiceAttitude(dto.getServiceAttitude());
                    rating.setMedicalSkill(dto.getMedicalSkill());
                    rating.setMedicalEffect(dto.getMedicalEffect());
                    return rating;
                })
                .collect(Collectors.toList());

        if (!ratings.isEmpty()) {
            // 计算三个维度的平均分
            BigDecimal avgServiceAttitude = calculateAverage(ratings, DoctorRating::getServiceAttitude);
            BigDecimal avgMedicalSkill = calculateAverage(ratings, DoctorRating::getMedicalSkill);
            BigDecimal avgMedicalEffect = calculateAverage(ratings, DoctorRating::getMedicalEffect);
            
            // 计算总评分(三个维度的平均值)
            BigDecimal avgRating = avgServiceAttitude.add(avgMedicalSkill).add(avgMedicalEffect)
                    .divide(new BigDecimal("3"), 1, RoundingMode.HALF_UP);
            
            doctorMapper.updateRating(doctorId, avgRating);
        }
    }

    private BigDecimal calculateAverage(List<DoctorRating> ratings, java.util.function.Function<DoctorRating, BigDecimal> getter) {
        return ratings.stream()
                .map(getter)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(ratings.size()), 1, RoundingMode.HALF_UP);
    }

    private DoctorRatingDTO convertToDTO(DoctorRating rating) {
        if (rating == null) {
            return null;
        }
        DoctorRatingDTO dto = new DoctorRatingDTO();
        BeanUtils.copyProperties(rating, dto);
        
        // 查询医生信息
        Doctor doctor = doctorMapper.selectById(rating.getDoctorId());
        if (doctor != null) {
            dto.setDoctorName(doctor.getName());
        }
        
        // 查询用户信息
        User user = userMapper.selectById(rating.getUserId());
        if (user != null) {
            dto.setUserName(user.getName());
        }
        
        return dto;
    }
} 