package com.project.fitness.service;

import com.project.fitness.DTO.ActivityRequest;
import com.project.fitness.DTO.ActivityResponse;
import com.project.fitness.model.*;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    @Autowired
    private final ActivityRepository activityRepository;

    @Autowired
    private final UserRepository userRepository;

    public ActivityResponse createActivity(ActivityRequest request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Activity activity = Activity.builder()
                .users(user)
                .type(request.getType())
                .additionalMetrics(request.getAdditionalMetrics())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        return mapToResponse(savedActivity);
    }

    public ActivityResponse getActivityById(String id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));
        return mapToResponse(activity);
    }

    public List<ActivityResponse> getAllActivities() {
        return activityRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ActivityResponse> getActivitiesByUserId(String userId) {
        return activityRepository.findByUsersId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponse updateActivity(String id, ActivityRequest request) {
        Activity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + id));

        Users user = existingActivity.getUsers();
        if (request.getUserId() != null && !request.getUserId().equals(existingActivity.getUsers().getId())) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        }

        Activity activity = Activity.builder()
                .id(existingActivity.getId())
                .users(user)
                .type(request.getType() != null ? request.getType() : existingActivity.getType())
                .additionalMetrics(request.getAdditionalMetrics() != null ? request.getAdditionalMetrics() : existingActivity.getAdditionalMetrics())
                .duration(request.getDuration() != null ? request.getDuration() : existingActivity.getDuration())
                .caloriesBurned(request.getCaloriesBurned() != null ? request.getCaloriesBurned() : existingActivity.getCaloriesBurned())
                .startTime(request.getStartTime() != null ? request.getStartTime() : existingActivity.getStartTime())
                .createdAt(existingActivity.getCreatedAt())
                .updateAt(existingActivity.getUpdateAt())
                .recommendations(existingActivity.getRecommendations())
                .build();

        Activity updatedActivity = activityRepository.save(activity);
        return mapToResponse(updatedActivity);
    }

    public void deleteActivity(String id) {
        if (!activityRepository.existsById(id)) {
            throw new RuntimeException("Activity not found with id: " + id);
        }
        activityRepository.deleteById(id);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUsers().getId());
        response.setType(activity.getType());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdateAt(activity.getUpdateAt());
        return response;
    }
}
