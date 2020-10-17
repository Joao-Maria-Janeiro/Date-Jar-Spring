package com.joaomariajaneiro.datejar.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.joaomariajaneiro.datejar.exceptions.AuthenticationException;
import com.joaomariajaneiro.datejar.model.Category;
import com.joaomariajaneiro.datejar.model.User;
import com.joaomariajaneiro.datejar.model.enums.Type;
import com.joaomariajaneiro.datejar.repository.CategoryRepository;
import com.joaomariajaneiro.datejar.repository.UserRepository;
import com.joaomariajaneiro.datejar.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/categories")
@RestController
public class CategoryController {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private JwtUtil jwtTokenUtil;
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    @GetMapping(value = "/all")
//    public Map<String, List<String>> getPendindCategories(@RequestHeader Map<String, String> headers) {
//        if (!headers.containsKey("authorization")) {
//            throw new AuthenticationException();
//        }
//        String username =
//                jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));
//
//        List<Category> allUserPendingCategories =
//                userRepository.getAllUserPendingCategories(username);
//
//        Map<String, List<String>> typesToCategories = new HashMap<>();
//        allUserPendingCategories.forEach(
//                category -> {
//                    if (typesToCategories.containsKey(category.getType())) {
//                        List<String> categories = typesToCategories.get(category.getType());
//                        categories.add(category.getName());
//                        typesToCategories.put(category.getType().name(), categories);
//                    } else {
//                        typesToCategories.put(category.getType().name(), ImmutableList.of(category.getName()));
//                    }
//                }
//        );
//        return typesToCategories;
//    }
//
//
//    @PostMapping(value = "/add")
//    public List<Category> addToPendingCategories(
//            @RequestBody String payload,
//            @RequestHeader Map<String, String> headers) throws IOException {
//        if (!headers.containsKey("authorization")) {
//            throw new AuthenticationException();
//        }
//        String username =
//                jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));
//
//        JsonNode jsonNode = objectMapper.readTree(payload);
//        Category category = new Category(jsonNode.get("title").asText(), getType(jsonNode.get("type").asText()));
//        categoryRepository.save(category);
//        User user = userRepository.findByUsername(username);
//        user.getCategories().add(category);
//        userRepository.save(user);
//        return user.getCategories();
//    }
//
//    Type getType(String type) {
//        try {
//            return Type.valueOf(type.toUpperCase());
//        } catch (Exception e) {
//            throw new RuntimeException("Category not found");
//        }
//
//    }

}
