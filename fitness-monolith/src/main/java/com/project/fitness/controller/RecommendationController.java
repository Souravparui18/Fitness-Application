package com.project.fitness.controller;

import com.project.fitness.DTO.RecommendationRequest;
import com.project.fitness.DTO.RecommendationResponse;
import com.project.fitness.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<RecommendationResponse> generateRecommendation(@RequestBody RecommendationRequest request) {
        RecommendationResponse recommendation = recommendationService.generateRecommendation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(recommendation);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendationResponse>> getRecommendationsByUserId(@PathVariable String userId) {
        List<RecommendationResponse> recommendations = recommendationService.getRecommendationByUserId(userId);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<RecommendationResponse>> getRecommendationsByActivityId(@PathVariable String activityId) {
        List<RecommendationResponse> recommendations = recommendationService.getRecommendationByActivityId(activityId);
        return ResponseEntity.ok(recommendations);
    }
}
