package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.RatingDTO;
import com.yunchuan.medical.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 评价管理控制器
 */
@Slf4j
@Tag(name = "评价管理")
@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Operation(summary = "创建评价")
    @PostMapping
    public Result<RatingDTO> createRating(@RequestBody @Valid RatingDTO ratingDTO) {
        return Result.ok(ratingService.createRating(ratingDTO));
    }

    @Operation(summary = "获取医生评价列表")
    @GetMapping("/doctor/{doctorId}")
    public Result<List<RatingDTO>> getDoctorRatings(@PathVariable String doctorId) {
        return Result.ok(ratingService.getDoctorRatings(doctorId));
    }

    @Operation(summary = "获取我的评价列表")
    @GetMapping("/my")
    public Result<List<RatingDTO>> getMyRatings() {
        return Result.ok(ratingService.getMyRatings());
    }

    @Operation(summary = "获取医生评价统计")
    @GetMapping("/doctor/{doctorId}/stats")
    public Result<Map<String, Object>> getDoctorRatingStats(@PathVariable String doctorId) {
        return Result.ok(ratingService.getDoctorRatingStats(doctorId));
    }

    @Operation(summary = "删除评价")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRating(@PathVariable String id) {
        ratingService.deleteRating(id);
        return Result.ok();
    }

    @Operation(summary = "更新评价状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateRatingStatus(
            @PathVariable String id,
            @RequestParam String status) {
        ratingService.updateRatingStatus(id, status);
        return Result.ok();
    }

    @Operation(summary = "检查是否已评价")
    @GetMapping("/check")
    public Result<Boolean> checkRated(@RequestParam String appointmentId) {
        return Result.ok(ratingService.hasRated(appointmentId));
    }
}
