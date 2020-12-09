package com.joaomariajaneiro.datejar.repository;

import com.joaomariajaneiro.datejar.model.User;
import com.joaomariajaneiro.datejar.repository.row_mappers.UsersRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

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

    public int associateUser(String username, String partnerUsername) {
        return jdbcTemplate.update("UPDATE Users" +
                        "    SET partner_id = (SELECT id FROM Users WHERE username=?) WHERE " +
                        "username" +
                        " = ?",
                partnerUsername, username);
    }

    public int save(User user) {
        return jdbcTemplate.update("INSERT INTO Users (id, username, password, email, picture)" +
                        " VALUES (" +
                        "(SELECT COALESCE(max(id), 0) + 1 FROM users)," +
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


