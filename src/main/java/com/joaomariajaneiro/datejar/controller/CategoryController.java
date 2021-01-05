package com.joaomariajaneiro.datejar.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaomariajaneiro.datejar.exceptions.AuthenticationException;
import com.joaomariajaneiro.datejar.model.Category;
import com.joaomariajaneiro.datejar.model.enums.Type;
import com.joaomariajaneiro.datejar.security.JwtUtil;
import com.joaomariajaneiro.datejar.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/categories")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/all")
    public Map<String, List<Category>> getPendindCategories(@RequestHeader Map<String, String> headers) {
        if (!headers.containsKey("authorization")) {
            throw new AuthenticationException();
        }
        String username =
                jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

        return categoryService.getAllUserPendingCategories(username);

    }

    @PostMapping(value = "/add")
    public String addToPendingCategories(
            @RequestBody String payload,
            @RequestHeader Map<String, String> headers) throws IOException {
        if (!headers.containsKey("authorization")) {
            throw new AuthenticationException();
        }
        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil
                            .JWT_PREFIX, ""));

            JsonNode jsonNode = objectMapper.readTree(payload);
            Category category = new Category(jsonNode.get("title").asText(), getType(jsonNode.get
                    ("type").asText()));
            categoryService.save(category, username);
        } catch (Exception e) {
            return "There was an error creating the category";
        }
        return "success";
    }

    @PostMapping(value = "/edit")
    public String updateCategoryName(@RequestBody String payload,
                                     @RequestHeader Map<String, String> headers) throws JsonProcessingException {
        if (!headers.containsKey("authorization")) {
            throw new AuthenticationException();
        }
        JsonNode jsonNode = objectMapper.readTree(payload);

        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

            categoryService.updateName(jsonNode.get("new_category_name").asText(),
                    jsonNode.get("id").asInt(), username);
            return "Success";
        } catch (Exception e) {
            return "There was an error updating your activity";
        }
    }

    @PostMapping(value = "/remove/{categoryId}")
    public String removeCategory(@PathVariable Long categoryId,
                                 @RequestHeader Map<String, String> headers) {
        if (!headers.containsKey("authorization")) {
            throw new AuthenticationException();
        }

        try {
            String username =
                    jwtTokenUtil.extractUsername(headers.get("authorization").replace(JwtUtil.JWT_PREFIX, ""));

            categoryService.removeCategory(categoryId, username);
            return "Success";
        } catch (Exception e) {
            return "There was an error deleting your category";
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
