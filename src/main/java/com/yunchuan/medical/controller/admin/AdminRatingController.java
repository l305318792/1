package com.yunchuan.medical.controller.admin;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.RatingDTO;
import com.yunchuan.medical.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 管理员-评价管理
 */
@Tag(name = "管理员-评价管理")
@RestController
@RequestMapping("/admin/ratings")
public class AdminRatingController {

    private final RatingService ratingService;

    public AdminRatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Operation(summary = "获取医生评价列表")
    @GetMapping("/doctor/{doctorId}")
    public Result<List<RatingDTO>> getDoctorRatings(@PathVariable String doctorId) {
        return Result.ok(ratingService.getDoctorRatings(doctorId));
    }

    @Operation(summary = "获取医生评价统计")
    @GetMapping("/doctor/{doctorId}/stats")
    public Result<Map<String, Object>> getDoctorRatingStats(@PathVariable String doctorId) {
        return Result.ok(ratingService.getDoctorRatingStats(doctorId));
    }

    @Operation(summary = "更新评价状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateRatingStatus(
            @PathVariable String id,
            @RequestParam String status) {
        ratingService.updateRatingStatus(id, status);
        return Result.ok();
    }

    @Operation(summary = "删除评价")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRating(@PathVariable String id) {
        ratingService.deleteRating(id);
        return Result.ok();
    }
} 