package com.joaomariajaneiro.datejar.repository.row_mappers;

import com.joaomariajaneiro.datejar.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("picture"),
                rs.getInt("partner_id"),
                rs.getBoolean("is_enabled")
        );
    }
}
