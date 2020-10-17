package com.joaomariajaneiro.datejar.repository;

import com.joaomariajaneiro.datejar.model.User;
import com.joaomariajaneiro.datejar.repository.row_mappers.UsersRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByUsername(String username) {
        User user = jdbcTemplate.queryForObject(
                "SELECT * FROM Users WHERE username LIKE ?",
                new Object[]{username},
                new UsersRowMapper());
        return user;
    }

    public int save(User user) {
        return jdbcTemplate.update("INSERT INTO Users (id, username, password, email, picture)" +
                        " VALUES (" +
                        "(SELECT setval(pg_get_serial_sequence('users', 'id'), coalesce(max(id)+1, 1), false) FROM users)," +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?)",
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getPicture()
        );
    }

}
