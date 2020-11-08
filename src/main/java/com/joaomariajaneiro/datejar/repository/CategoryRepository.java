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
        return jdbcTemplate.query("SELECT c.id, c.name, c.type FROM Users u JOIN " +
                        "Category c " +
                        "ON u.id=c.user_id WHERE u.username = ?", new Object[]{username},
                new CategoryRowMapper());
    }

    public int save(Category category, String username) {
        return jdbcTemplate.update("INSERT INTO Category (id, name, type, user_id) " +
                        "VALUES (" +
                        "(SELECT setval(pg_get_serial_sequence('category', 'id'), coalesce(max" +
                        "(id)+1, 1),false) FROM Category)," +
                        "?, ?, " +
                        "(SELECT id FROM Users WHERE username = ?))", category.getName(),
                category.getType().ordinal(), username);
    }

    public int updateName(String categoryName, int categoryType, String username, String newCategoryName) {
        return jdbcTemplate.update("UPDATE Category" +
                        "    SET name = ? WHERE name = (SELECT c.name FROM Category c JOIN Users u ON c.user_id=u.id WHERE c.name=? AND c.type=? AND u.username = ?)",
                newCategoryName, categoryName, categoryType, username);
    }
}


