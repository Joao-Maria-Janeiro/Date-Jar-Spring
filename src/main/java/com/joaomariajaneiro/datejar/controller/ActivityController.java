package com.joaomariajaneiro.datejar.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaomariajaneiro.datejar.exceptions.AuthenticationException;
import com.joaomariajaneiro.datejar.model.Activity;
import com.joaomariajaneiro.datejar.model.Category;
import com.joaomariajaneiro.datejar.model.User;
import com.joaomariajaneiro.datejar.repository.ActivityRepository;
import com.joaomariajaneiro.datejar.repository.UserRepository;
import com.joaomariajaneiro.datejar.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@RequestMapping("/activities")
@RestController
public class ActivityController {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtTokenUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/category/{name}")
    public List<Activity> getActivitiesFromCategory(@PathVariable String name,
                                                    @RequestHeader Map<String, String> headers) {
        if (!headers.containsKey("authorization")) {
            throw new AuthenticationException();
        }

        String username =
                jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

        return activityRepository.getActivitiesOfCategory(name, username);

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

            Activity activity = new Activity(jsonNode.get("activity_name").asText());
            activityRepository.save(activity, jsonNode.get("category_name").asText(), username);
            return "Activity created successfuly";
        } catch (Exception e) {
            throw new RuntimeException("There was an error creating your activity");
        }
    }

}
