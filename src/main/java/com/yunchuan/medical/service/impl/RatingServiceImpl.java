package com.yunchuan.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunchuan.medical.dto.RatingDTO;
import com.yunchuan.medical.entity.Rating;
import com.yunchuan.medical.entity.Doctor;
import com.yunchuan.medical.entity.User;
import com.yunchuan.medical.mapper.RatingMapper;
import com.yunchuan.medical.service.RatingService;
import com.yunchuan.medical.service.DoctorService;
import com.yunchuan.medical.service.UserService;
import com.yunchuan.medical.exception.BusinessException;
import com.yunchuan.medical.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 * 评价表 服务实现类
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
@Slf4j
@Service
public class RatingServiceImpl extends ServiceImpl<RatingMapper, Rating> implements RatingService {

    private final DoctorService doctorService;
    private final UserService userService;

    public RatingServiceImpl(DoctorService doctorService, UserService userService) {
        this.doctorService = doctorService;
        this.userService = userService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RatingDTO createRating(RatingDTO ratingDTO) {
        // 1. 验证是否已评价
        if (ratingDTO.getAppointmentId() != null && hasRated(ratingDTO.getAppointmentId())) {
            throw new BusinessException("该预约已评价");
        }
        if (ratingDTO.getConsultationId() != null && hasRatedConsultation(ratingDTO.getConsultationId())) {
            throw new BusinessException("该问诊已评价");
        }

        // 2. 设置用户ID
        String userId = SecurityUtil.getCurrentUserId();
        ratingDTO.setUserId(userId);

        // 3. 创建评价记录
        Rating rating = new Rating();
        rating.setId(UUID.randomUUID().toString().replace("-", ""));
        rating.setAppointmentId(ratingDTO.getAppointmentId());
        rating.setConsultationId(ratingDTO.getConsultationId());
        rating.setDoctorId(ratingDTO.getDoctorId());
        rating.setUserId(userId);
        rating.setServiceAttitude(ratingDTO.getServiceAttitude());
        rating.setMedicalSkill(ratingDTO.getMedicalSkill());
        rating.setMedicalEffect(ratingDTO.getMedicalEffect());
        rating.setComment(ratingDTO.getComment());
        rating.setStatus("NORMAL");
        rating.setCreateTime(LocalDateTime.now());
        rating.setUpdateTime(LocalDateTime.now());

        // 4. 保存评价
        save(rating);

        // 5. 更新医生评分
        updateDoctorRating(rating.getDoctorId());

        return convertToDTO(rating);
    }

    @Override
    public List<RatingDTO> getDoctorRatings(String doctorId) {
        List<Rating> ratings = baseMapper.selectByDoctorId(doctorId);
        return ratings.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<RatingDTO> getMyRatings() {
        String currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            log.error("获取当前用户ID失败");
            throw new BusinessException("用户未登录");
        }

        User user = userService.getById(currentUserId);
        if (user == null) {
            log.error("未找到用户信息，用户ID: {}", currentUserId);
            throw new BusinessException("用户信息不存在");
        }

        if (user.getRole() == null) {
            log.error("用户角色为空，用户ID: {}", currentUserId);
            throw new BusinessException("用户角色未设置");
        }

        List<Rating> ratings;
        
        if ("DOCTOR".equals(user.getRole())) {
            // 如果是医生，查询对自己的评价
            Doctor doctor = doctorService.getByUserId(currentUserId);
            if (doctor != null) {
                ratings = baseMapper.selectByDoctorId(doctor.getId());
            } else {
                log.warn("未找到医生信息，用户ID: {}", currentUserId);
                ratings = new ArrayList<>();
            }
        } else {
            // 如果是患者，查询自己发出的评价
            ratings = baseMapper.selectByUserId(currentUserId);
        }
        
        return ratings.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public Map<String, Object> getDoctorRatingStats(String doctorId) {
        List<Rating> ratings = baseMapper.selectByDoctorId(doctorId);
        Map<String, Object> stats = new HashMap<>();

        if (ratings.isEmpty()) {
            stats.put("totalRatings", 0);
            stats.put("avgServiceAttitude", "0.0");
            stats.put("avgMedicalSkill", "0.0");
            stats.put("avgMedicalEffect", "0.0");
            stats.put("overallRating", "0.0");
            stats.put("ratingDistribution", new HashMap<>());
            stats.put("recentRatings", new ArrayList<>());
            return stats;
        }

        // 计算平均分
        BigDecimal avgServiceAttitude = calculateAverage(ratings, Rating::getServiceAttitude);
        BigDecimal avgMedicalSkill = calculateAverage(ratings, Rating::getMedicalSkill);
        BigDecimal avgMedicalEffect = calculateAverage(ratings, Rating::getMedicalEffect);

        // 计算总体评分
        BigDecimal overallRating = avgServiceAttitude.add(avgMedicalSkill).add(avgMedicalEffect)
                .divide(new BigDecimal("3"), 1, RoundingMode.HALF_UP);

        // 评分分布
        Map<Integer, Long> distribution = new HashMap<>();
        ratings.forEach(r -> {
            BigDecimal avgScore = r.getServiceAttitude().add(r.getMedicalSkill()).add(r.getMedicalEffect())
                    .divide(new BigDecimal("3"), 1, RoundingMode.HALF_UP);
            int score = avgScore.intValue();
            distribution.merge(score, 1L, Long::sum);
        });

        // 最近评价
        List<RatingDTO> recentRatings = ratings.stream()
                .sorted(Comparator.comparing(Rating::getCreateTime).reversed())
                .limit(5)
                .map(this::convertToDTO)
                .toList();

        stats.put("totalRatings", ratings.size());
        stats.put("avgServiceAttitude", avgServiceAttitude.toString());
        stats.put("avgMedicalSkill", avgMedicalSkill.toString());
        stats.put("avgMedicalEffect", avgMedicalEffect.toString());
        stats.put("overallRating", overallRating.toString());
        stats.put("ratingDistribution", distribution);
        stats.put("recentRatings", recentRatings);

        return stats;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRating(String id) {
        Rating rating = getById(id);
        if (rating == null) {
            throw new BusinessException("评价不存在");
        }

        removeById(id);
        updateDoctorRating(rating.getDoctorId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRatingStatus(String id, String status) {
        Rating rating = getById(id);
        if (rating == null) {
            throw new BusinessException("评价不存在");
        }

        rating.setStatus(status);
        rating.setUpdateTime(LocalDateTime.now());
        updateById(rating);
    }

    @Override
    public boolean hasRated(String appointmentId) {
        if (appointmentId == null) {
            return false;
        }
        Rating rating = baseMapper.selectByAppointmentId(appointmentId);
        return rating != null;
    }

    /**
     * 检查问诊是否已评价
     */
    public boolean hasRatedConsultation(String consultationId) {
        if (consultationId == null) {
            return false;
        }
        Boolean exists = baseMapper.existsByConsultationId(consultationId);
        return exists != null && exists;
    }

    private BigDecimal calculateAverage(List<Rating> ratings, java.util.function.Function<Rating, BigDecimal> getter) {
        return ratings.stream()
                .map(getter)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(ratings.size()), 1, RoundingMode.HALF_UP);
    }

    /**
     * 更新医生评分
     */
    private void updateDoctorRating(String doctorId) {
        List<Rating> ratings = baseMapper.selectByDoctorId(doctorId);
        if (ratings.isEmpty()) {
            return;
        }

        // 计算平均分
        BigDecimal avgServiceAttitude = calculateAverage(ratings, Rating::getServiceAttitude);
        BigDecimal avgMedicalSkill = calculateAverage(ratings, Rating::getMedicalSkill);
        BigDecimal avgMedicalEffect = calculateAverage(ratings, Rating::getMedicalEffect);

        // 计算总评分
        BigDecimal avgRating = avgServiceAttitude.add(avgMedicalSkill).add(avgMedicalEffect)
                .divide(new BigDecimal("3"), 1, RoundingMode.HALF_UP);

        // 更新医生评分
        Doctor doctor = doctorService.getById(doctorId);
        if (doctor != null) {
            doctor.setRating(avgRating);
            doctor.setRatingCount(ratings.size());
            doctor.setUpdateTime(LocalDateTime.now());
            doctorService.updateById(doctor);
        }
    }

    /**
     * 转换为DTO
     */
    private RatingDTO convertToDTO(Rating rating) {
        if (rating == null) {
            return null;
        }

        RatingDTO dto = new RatingDTO();
        dto.setId(rating.getId());
        dto.setAppointmentId(rating.getAppointmentId());
        dto.setConsultationId(rating.getConsultationId());
        dto.setDoctorId(rating.getDoctorId());
        dto.setUserId(rating.getUserId());
        dto.setServiceAttitude(rating.getServiceAttitude());
        dto.setMedicalSkill(rating.getMedicalSkill());
        dto.setMedicalEffect(rating.getMedicalEffect());
        dto.setComment(rating.getComment());
        dto.setStatus(rating.getStatus());
        dto.setCreateTime(rating.getCreateTime());
        dto.setUpdateTime(rating.getUpdateTime());

        // 设置医生和用户信息
        Doctor doctor = doctorService.getById(rating.getDoctorId());
        if (doctor != null) {
            dto.setDoctorName(doctor.getName());
        }

        User user = userService.getById(rating.getUserId());
        if (user != null) {
            dto.setUserName(user.getName());
        }

        return dto;
    }
}
