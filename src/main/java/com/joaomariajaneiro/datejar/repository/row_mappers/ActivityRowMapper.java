package com.joaomariajaneiro.datejar.repository.row_mappers;

import com.joaomariajaneiro.datejar.model.Activity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActivityRowMapper implements RowMapper<Activity> {
    @Override
    public Activity mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Activity(
                resultSet.getInt("id"),
                resultSet.getString("name")
        );
    }
}
