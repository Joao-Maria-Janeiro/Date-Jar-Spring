package com.joaomariajaneiro.datejar.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaomariajaneiro.datejar.exceptions.AuthenticationException;
import com.joaomariajaneiro.datejar.model.Activity;
import com.joaomariajaneiro.datejar.model.enums.Type;
import com.joaomariajaneiro.datejar.repository.ActivityRepository;
import com.joaomariajaneiro.datejar.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequestMapping("/activities")
@RestController
public class ActivityController {

    @Autowired
    private ActivityRepository activityRepository;


    @Autowired
    private JwtUtil jwtTokenUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/random/category/{categoryId}")
    public Activity getRandomActivityFromCategory(@PathVariable String categoryId,
                                                  @RequestHeader Map<String, String> headers) {
        if (!headers.containsKey("authorization")) {
            throw new AuthenticationException();
        }

        String username =
                jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

        List<Activity> activitiesOfCategory =
                activityRepository.getActivitiesOfCategory(Integer.valueOf(categoryId));
        int rnd = new Random().nextInt(activitiesOfCategory.size());

        return activitiesOfCategory.get(rnd);

    }

    @GetMapping(value = "/category/{categoryId}")
    public List<Activity> getActivitiesFromCategory(@PathVariable int categoryId,
                                                    @RequestHeader Map<String, String> headers) {
        if (!headers.containsKey("authorization")) {
            throw new AuthenticationException();
        }

        String username =
                jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

        return activityRepository.getActivitiesOfCategory(categoryId);
    }

    @PostMapping(value = "/update")
    public String updateActivity(
            @RequestBody String payload,
            @RequestHeader Map<String, String> headers) throws JsonProcessingException {
        if (!headers.containsKey("authorization")) {
            throw new AuthenticationException();
        }
        JsonNode jsonNode = objectMapper.readTree(payload);

        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

            for (Activity activity : activityRepository.getActivitiesOfCategory(jsonNode.get(
                    "category_id").asInt())) {
                if (activity.getName().equals(jsonNode.get("new_activity_name").asText())) {
                    return "The Activity with that name already exists for the chosen category";
                }
            }

            activityRepository.update(jsonNode.get("new_activity_name").asText(), jsonNode.get(
                    "old_activity_name").asText(), jsonNode.get("category_id").asInt());
            return "Success";
        } catch (Exception e) {
            return "There was an error updating your activity";
        }

    }

    @PostMapping(value = "/delete")
    public String deleteActivity(@RequestBody String payload,
                                 @RequestHeader Map<String, String> headers) throws IOException {
        if (!headers.containsKey("authorization")) {
            throw new AuthenticationException();
        }
        JsonNode jsonNode = objectMapper.readTree(payload);

        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil
                            .JWT_PREFIX, ""));

            activityRepository.delete(jsonNode.get("activity_name").asText(),
                    jsonNode.get("category_name").asText(), username, getType(jsonNode.get(
                            "category_type").asText()).ordinal());
            return "Activity deleted successfuly";
        } catch (Exception e) {
            throw new RuntimeException("There was an error creating your activity");
        }
    }

    @PostMapping(value = "/add")
    public String addActivity(@RequestBody String payload,
                              @RequestHeader Map<String, String> headers) throws IOException {
        if (!headers.containsKey("authorization")) {
            throw new AuthenticationException();
        }
        JsonNode jsonNode = objectMapper.readTree(payload);

        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil
                            .JWT_PREFIX, ""));

            for (Activity activity :
                    activityRepository.getActivitiesOfCategory(jsonNode.get("category_id").asInt())) {
                if (activity.getName().equals(jsonNode.get("activity_name").asText())) {
                    return "The Activity with that name already exists for the chosen category";
                }
            }

            Activity activity = new Activity(jsonNode.get("activity_name").asText());
            activityRepository.save(activity, jsonNode.get("category_id").asInt());
            return "Activity created successfuly";
        } catch (Exception e) {
            throw new RuntimeException("There was an error creating your activity");
        }
    }

    Type getType(String type) {
        try {
            return Type.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Category not found");
        }

    }

}
