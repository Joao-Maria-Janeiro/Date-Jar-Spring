package com.joaomariajaneiro.datejar.repository;

import com.joaomariajaneiro.datejar.model.Category;
import com.joaomariajaneiro.datejar.repository.row_mappers.CategoryRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;


    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Category> getAllUserPendingCategories(String username) {
        return jdbcTemplate.query("SELECT DISTINCT c.id, c.name, c.type FROM Users u, Category c " +
                        " WHERE u.username = ? AND c" +
                        ".user_id =u.id OR c.user_id=u.partner_id",
                new Object[]{username},
                new CategoryRowMapper());
    }


    public int save(Category category, String username) {
        return jdbcTemplate.update("  INSERT INTO Category (id, name, type, user_id) " +
                        "VALUES (" +
                        "(SELECT COALESCE(max(id), 0) + 1 FROM Category)," +
                        "?, ?, " +
                        "(SELECT id FROM Users WHERE username = ?))", category.getName(),
                category.getType().ordinal(), username);
    }

    public int updateName(String newCategoryName, int categoryId) {
        return jdbcTemplate.update("UPDATE Category" +
                        "    SET name = ? WHERE id = ?",
                newCategoryName, categoryId);
    }
}


