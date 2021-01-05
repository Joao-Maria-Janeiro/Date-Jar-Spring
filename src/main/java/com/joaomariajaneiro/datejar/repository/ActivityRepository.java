package com.joaomariajaneiro.datejar.repository;

import com.joaomariajaneiro.datejar.model.Activity;
import com.joaomariajaneiro.datejar.repository.row_mappers.ActivityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityRepository {

    private final JdbcTemplate jdbcTemplate;

    public ActivityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Activity> getActivitiesOfCategory(int categoryId) {
        return jdbcTemplate.query(
                "  SELECT a.id, a.name FROM Activity a JOIN Category c ON a.category_id=c.id " +
                        " WHERE c.id= ?",
                new Object[]{categoryId}, new ActivityRowMapper()
        );
    }

    public int save(Activity activity, int categoryId) {
        return jdbcTemplate.update("  INSERT INTO Activity (id, name, category_id) VALUES (" +
                        "(SELECT " +
                        "COALESCE(max(id), 0) + 1 FROM Activity),?, ?)",
                activity.getName(), categoryId);
    }

    public int update(String newActivityName, String oldActivityName, int categoryId,
                      String username) {
        return jdbcTemplate.update("UPDATE Activity SET name = ? WHERE id = (SELECT a.id FROM " +
                        "Activity a JOIN Category c ON a.category_id=c.id WHERE c.id = ? AND a" +
                        ".name = ?) AND (category_id IN (SELECT id FROM Category WHERE user_id = " +
                        "(SELECT id FROM Users WHERE username = ?)) OR category_id IN (SELECT " +
                        "id FROM Category WHERE user_id = (SELECT partner_id FROM Users WHERE " +
                        "username = ?)))",
                newActivityName, categoryId, oldActivityName, username, username);
    }

    public int delete(Long activityId, Long categoryId,
                      String username, int categoryType) {
        return jdbcTemplate.update("DELETE FROM Activity WHERE id = (SELECT a.id FROM Activity a " +
                        "JOIN Category c ON a.category_id= c.id " +
                        "WHERE c.id = ? AND a.id = ? AND c.type = ? AND (c" +
                        ".user_id = (SELECT u1.id FROM Users u1 where u1.username = ?) OR c" +
                        ".user_id =(SELECT u2.partner_id FROM Users u2 WHERE u2.username=?)))",
                categoryId, activityId, categoryType, username, username);
    }
}


