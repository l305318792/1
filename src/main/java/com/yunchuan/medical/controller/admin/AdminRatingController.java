package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.RatingDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员-评价管理
 */
@Tag(name = "管理员-评价管理")
@RestController
@RequestMapping("/admin/ratings")
public class AdminRatingController {

    @Operation(summary = "获取评价列表")
    @GetMapping
    public Result<Object> list() {
        // 模拟评价数据
        List<RatingDTO> ratings = new ArrayList<>();
        
        // 评价1
        RatingDTO rating1 = RatingDTO.builder()
                .id("1")
                .userId("2")
                .userName("张三")
                .doctorId("1")
                .doctorName("李医生")
                .departmentId("1")
                .departmentName("内科")
                .consultationId("1")
                .rating(5)
                .content("医生很专业，态度也很好，解答很详细")
                .status("normal")
                .createTime(LocalDateTime.now().minusHours(2))
                .build();
        ratings.add(rating1);
        
        // 评价2
        RatingDTO rating2 = RatingDTO.builder()
                .id("2")
                .userId("3")
                .userName("李四")
                .doctorId("2")
                .doctorName("陈医生")
                .departmentId("2")
                .departmentName("外科")
                .consultationId("2")
                .rating(4)
                .content("整体服务不错，建议很有帮助")
                .status("normal")
                .createTime(LocalDateTime.now().minusHours(1))
                .build();
        ratings.add(rating2);
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", ratings.size());
        result.put("list", ratings);
        
        return Result.ok(result);
    }

    @Operation(summary = "隐藏/显示评价")
    @PutMapping("/{id}/status")
    public Result<RatingDTO> updateStatus(@PathVariable String id, @RequestParam String status) {
        // 模拟更新评价状态
        RatingDTO rating = RatingDTO.builder()
                .id(id)
                .userId("2")
                .userName("张三")
                .doctorId("1")
                .doctorName("李医生")
                .departmentId("1")
                .departmentName("内科")
                .consultationId("1")
                .rating(5)
                .content("医生很专业，态度也很好，解答很详细")
                .status(status)
                .createTime(LocalDateTime.now().minusHours(2))
                .build();
                
        return Result.ok(rating);
    }
} 