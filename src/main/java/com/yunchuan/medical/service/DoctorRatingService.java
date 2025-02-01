package com.yunchuan.medical.service;

import com.yunchuan.medical.dto.DoctorRatingDTO;
import com.yunchuan.medical.dto.DoctorRatingFormDTO;
import java.util.List;

/**
 * 医生评价服务接口
 */
public interface DoctorRatingService {
    
    /**
     * 创建评价
     */
    DoctorRatingDTO createRating(DoctorRatingFormDTO form);
    
    /**
     * 获取医生评价列表
     */
    List<DoctorRatingDTO> getDoctorRatings(String doctorId);
    
    /**
     * 获取我的评价列表
     */
    List<DoctorRatingDTO> getMyRatings();
} 