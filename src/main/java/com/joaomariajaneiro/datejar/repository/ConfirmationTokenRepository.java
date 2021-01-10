package com.joaomariajaneiro.datejar.repository;

import com.joaomariajaneiro.datejar.model.ConfirmationToken;
import com.joaomariajaneiro.datejar.repository.row_mappers.ConfirmationTokenRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConfirmationTokenRepository {
    private final JdbcTemplate jdbcTemplate;

    public ConfirmationTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ConfirmationToken findByUser(String username) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM confirmationtoken WHERE user_id = (SELECT id FROM Users WHERE " +
                        "username = ?) ORDER BY created_date LIMIT 1",
                new Object[]{username}, new ConfirmationTokenRowMapper()
        );
    }

    public int save(ConfirmationToken confirmationToken, String username) {
        return jdbcTemplate.update("INSERT INTO confirmationtoken (id, token, created_date, " +
                        "user_id)" +
                        "    VALUES ((SELECT COALESCE(max(id), 0) + 1 FROM confirmationtoken),?," +
                        "?,(SELECT" +
                        " id FROM Users WHERE username = ?))", confirmationToken.getToken(),
                confirmationToken.getCreatedDate(), username);
    }
}

