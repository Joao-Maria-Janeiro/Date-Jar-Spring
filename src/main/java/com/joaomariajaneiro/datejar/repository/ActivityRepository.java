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
                        "max(id) + 1 FROM Activity),?, ?)",
                activity.getName(), categoryId);
    }

    public int update(String newActivityName, String oldActivityName, int categoryId) {
        return jdbcTemplate.update("  UPDATE Activity" +
                        "    SET name = ? WHERE id = (SELECT a.id FROM Activity a JOIN " +
                        "Category c ON a.category_id=" +
                        "        c.id WHERE c.id = ? AND a.name = ?)",
                newActivityName, categoryId, oldActivityName);
    }

    public int delete(String activityName, String categoryName,
                      String username, int categoryType) {
        return jdbcTemplate.update("DELETE FROM Activity" +
                        "    WHERE id = (SELECT a.id FROM Activity a JOIN Category c ON a" +
                        ".category_id=" +
                        "        c.id JOIN Users u ON c.user_id=u.id WHERE u.username = ? AND c" +
                        ".name = ? AND a.name = ? AND c.type = ?)",
                username, categoryName, activityName, categoryType);
    }
}



