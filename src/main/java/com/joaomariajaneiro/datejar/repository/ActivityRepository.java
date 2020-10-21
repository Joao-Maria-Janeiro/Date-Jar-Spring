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

    public List<Activity> getActivitiesOfCategory(String categoryName, String username, int categoryType) {
        return jdbcTemplate.query(
                "SELECT a.id, a.name FROM Activity a JOIN Category c ON a.category_id=c.id JOIN " +
                        "Users u ON c.user_id=u.id WHERE c.name= ? AND u.username = ? AND c.type = ?",
                new Object[]{categoryName, username, categoryType}, new ActivityRowMapper()
        );
    }

    public int save(Activity activity, String categoryName, String username, int categoryType) {
        return jdbcTemplate.update("  INSERT INTO Activity (id, name, category_id) VALUES (" +
                        "(SELECT " +
                        "setval (pg_get_serial_sequence('activity', 'id'), coalesce(max(id)+1, 1)" +
                        ",false) FROM Activity),?, (SELECT c.id FROM Category c JOIN Users u ON u" +
                        ".id=c.user_id WHERE c.name = ? AND u.username = ? AND c.type = ?))",
                activity.getName(), categoryName, username, categoryType);
    }

    public int update(String newActivityName, String oldActivityName, String categoryName,
                      String username, int categoryType) {
        return jdbcTemplate.update("UPDATE Activity" +
                        "    SET name = ? WHERE name = (SELECT a.name FROM Activity a JOIN " +
                        "Category c ON a.category_id=" +
                        "        c.id JOIN Users u ON c.user_id=u.id WHERE u.username = ? AND c" +
                        ".name = ? AND a.name = ? AND c.type = ?)",
                newActivityName, username, categoryName, oldActivityName, categoryType);
    }

    public int delete(String activityName, String categoryName,
                      String username, int categoryType) {
        return jdbcTemplate.update("DELETE FROM Activity" +
                        "    WHERE name = (SELECT a.name FROM Activity a JOIN Category c ON a" +
                        ".category_id=" +
                        "        c.id JOIN Users u ON c.user_id=u.id WHERE u.username = ? AND c" +
                        ".name = ? AND a.name = ? AND c.type = ?)",
                username, categoryName, activityName, categoryType);
    }
}


//AND a.name = ?

//    UPDATE Activity
//    SET name = ? WHERE name = (SELECT a.name FROM Activity a JOIN Category c ON a.category_id=
//        c.id JOIN Users u ON c.user_id=u.id WHERE u.username = ? AND c.name = ? AND a.name = ?)


//    DELETE FROM Activity
//    WHERE name = (SELECT a.name FROM Activity a JOIN Category c ON a.category_id=
//        c.id JOIN Users u ON c.user_id=u.id WHERE u.username = ? AND c.name = ? AND a.name = ?)

