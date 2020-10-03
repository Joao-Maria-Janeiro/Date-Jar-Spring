package com.joaomariajaneiro.datejar.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/categories")
@RestController
public class CategoryController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtUtil jwtTokenUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/categories/all")
    public List<String> getPendindCategories(@RequestHeader Map<String, String> headers) {
        if (!headers.containsKey("authorization")) {
            return new ArrayList<>();
        }
        String username =
                jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

        List<Category> allUserPendingCategories =
                userRepository.getAllUserPendingCategories(username);
        List<String> categoriesNames =
                allUserPendingCategories.stream().map(category -> category.getName()).collect(Collectors.toList());
        return categoriesNames;
    }

//    @GetMapping(value = "/subcategories/{categoryName}")
//    public List<String> getPendindCategories(@PathVariable String categoryName,
//                                             @RequestHeader Map<String, String> headers) {
//        if (!headers.containsKey("authorization")) {
//            return new ArrayList<>();
//        }
//        String username =
//                jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));
//
//        List<Category> allUserPendingCategories =
//                userRepository.getAllUserPendingCategories(username);
//        List<String> categoriesNames =
//                allUserPendingCategories.stream().map(category -> category.getName()).collect(Collectors.toList());
//        return categoriesNames;
//    }

    @PostMapping(value = "/add")
    public List<Category> addToPendingCategories(
            @RequestBody String payload,
            @RequestHeader Map<String, String> headers) throws IOException {
        if (!headers.containsKey("authorization")) {
            return new ArrayList<>();
        }
        String username =
                jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

        JsonNode jsonNode = objectMapper.readTree(payload);
        Category category = new Category(jsonNode.get("title").asText());
        categoryRepository.save(category);
        User user = userRepository.findByUsername(username);
        user.getCategories().add(category);
        userRepository.save(user);
        return user.getCategories();
    }

    Type getType(String type) {
        try {
            return Type.valueOf(type);
        } catch (Exception e) {
            throw new RuntimeException("Category not found");
        }

    }
}
