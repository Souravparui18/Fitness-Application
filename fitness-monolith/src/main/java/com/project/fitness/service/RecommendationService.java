package com.project.fitness.service;

import com.project.fitness.DTO.RecommendationRequest;
import com.project.fitness.DTO.RecommendationResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.model.Recommendation;
import com.project.fitness.model.Users;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.RecommendationRepository;
import com.project.fitness.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    public RecommendationResponse generateRecommendation(RecommendationRequest request) {
        Users user = findUserById(request.getUserId());
        Activity activity = findActivityById(request.getActivityId());

        String recommendationText = buildRecommendationText(request, activity);
        
        Recommendation recommendation = buildRecommendation(user, activity, request, recommendationText);
        Recommendation saved = recommendationRepository.save(recommendation);
        
        return mapToResponse(saved);
    }

    public List<RecommendationResponse> getRecommendationByUserId(String userId) {
        List<Recommendation> recommendations = recommendationRepository.findByUsersId(userId);
        return recommendations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RecommendationResponse> getRecommendationByActivityId(String activityId) {
        List<Recommendation> recommendations = recommendationRepository.findByActivityId(activityId);
        return recommendations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private Users findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    private Activity findActivityById(String activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found with id: " + activityId));
    }

    private String buildRecommendationText(RecommendationRequest request, Activity activity) {
        if (request.getRecommendation() != null && !request.getRecommendation().isBlank()) {
            return request.getRecommendation();
        }
        return "Auto-generated recommendation for activity: " + activity.getType();
    }

    private Recommendation buildRecommendation(Users user, Activity activity, 
                                               RecommendationRequest request, String recommendationText) {
        return Recommendation.builder()
                .users(user)
                .activity(activity)
                .recommendation(recommendationText)
                .improvements(request.getImprovements())
                .suggestions(request.getSuggestions())
                .safety(request.getSafety())
                .build();
    }

    private RecommendationResponse mapToResponse(Recommendation recommendation) {
        RecommendationResponse response = new RecommendationResponse();
        response.setId(recommendation.getId());
        response.setUserId(recommendation.getUsers().getId());
        response.setActivityId(recommendation.getActivity().getId());
        response.setType(recommendation.getType());
        response.setRecommendation(recommendation.getRecommendation());
        response.setImprovements(recommendation.getImprovements());
        response.setSuggestions(recommendation.getSuggestions());
        response.setSafety(recommendation.getSafety());
        response.setCreatedAt(recommendation.getCreatedAt());
        response.setUpdateAt(recommendation.getUpdateAt());
        return response;
    }
}
