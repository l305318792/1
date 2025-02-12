package com.yunchuan.medical.service;

import com.yunchuan.medical.entity.Rating;
import com.yunchuan.medical.dto.RatingDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评价表 服务类
 * </p>
 *
 * @author yunchuan
 * @since 2025-01-25
 */
public interface RatingService extends IService<Rating> {
    
    /**
     * 创建评价
     */
    RatingDTO createRating(RatingDTO ratingDTO);
    
    /**
     * 获取医生评价列表
     */
    List<RatingDTO> getDoctorRatings(String doctorId);
    
    /**
     * 获取我的评价列表
     */
    List<RatingDTO> getMyRatings();
    
    /**
     * 获取医生评价统计
     */
    Map<String, Object> getDoctorRatingStats(String doctorId);
    
    /**
     * 删除评价
     */
    void deleteRating(String id);
    
    /**
     * 更新评价状态
     */
    void updateRatingStatus(String id, String status);
    
    /**
     * 检查是否已评价
     */
    boolean hasRated(String appointmentId);
}
