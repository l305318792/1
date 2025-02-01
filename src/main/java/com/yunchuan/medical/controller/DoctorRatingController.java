package com.yunchuan.medical.controller;

import com.yunchuan.medical.common.Result;
import com.yunchuan.medical.dto.DoctorRatingDTO;
import com.yunchuan.medical.dto.DoctorRatingFormDTO;
import com.yunchuan.medical.service.DoctorRatingService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 医生评价接口
 */
@Tag(name = "医生评价管理")
@RestController
@RequestMapping("/doctor-rating")
public class DoctorRatingController {

    private final DoctorRatingService doctorRatingService;

    public DoctorRatingController(DoctorRatingService doctorRatingService) {
        this.doctorRatingService = doctorRatingService;
    }

    /**
     * 获取评价列表
     */
    @Operation(summary = "获取评价列表")
    @GetMapping("/doctor/{doctorId}")
    public Result<List<DoctorRatingDTO>> getDoctorRatings(@PathVariable String doctorId) {
        return Result.ok(doctorRatingService.getDoctorRatings(doctorId));
    }

    /**
     * 获取我的评价列表
     */
    @Operation(summary = "获取我的评价列表")
    @GetMapping("/my")
    public Result<List<DoctorRatingDTO>> getMyRatings() {
        return Result.ok(doctorRatingService.getMyRatings());
    }

    /**
     * 新增评价
     */
    @Operation(summary = "新增评价")
    @PostMapping
    public Result<DoctorRatingDTO> createRating(@RequestBody @Valid DoctorRatingFormDTO form) {
        return Result.ok(doctorRatingService.createRating(form));
    }

    /**
     * 获取医生评价统计
     */
    @GetMapping("/doctor/{id}/stats")
    public Result<Object> stats(@PathVariable String id) {
        return Result.ok(new Object());
    }

    /**
     * 删除评价
     */
    @DeleteMapping("/{id}")
    public Result<Object> delete(@PathVariable String id) {
        return Result.ok(new Object());
    }

    /**
     * 回复评价
     */
    @PostMapping("/{id}/reply")
    public Result<Object> reply(@PathVariable String id, @RequestBody Object content) {
        return Result.ok(new Object());
    }
} 