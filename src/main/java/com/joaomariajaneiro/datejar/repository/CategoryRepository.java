package com.joaomariajaneiro.datejar.repository;

import com.joaomariajaneiro.datejar.model.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;


    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Category> getAllUserPendingCategories(String username) {
        jdbcTemplate.query("SELECT c.id, c.name, c.type, c.user_id FROM Users u JOIN Category c on u.id=c.user_id WHERE u.username = ?", new Object[]{username}, )
    }


}


