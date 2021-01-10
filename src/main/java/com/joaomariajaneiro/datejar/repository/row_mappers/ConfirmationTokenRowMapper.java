package com.joaomariajaneiro.datejar.repository.row_mappers;

import com.joaomariajaneiro.datejar.model.ConfirmationToken;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfirmationTokenRowMapper implements RowMapper<ConfirmationToken> {
    @Override
    public ConfirmationToken mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ConfirmationToken(
                resultSet.getLong("id"),
                resultSet.getString("token"),
                resultSet.getDate("created_date"),
                resultSet.getLong("user_id")
        );
    }
}
