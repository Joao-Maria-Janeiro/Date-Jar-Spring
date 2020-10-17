package com.joaomariajaneiro.datejar.repository.row_mappers;

import com.joaomariajaneiro.datejar.model.Category;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRowMapper implements RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Category(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("type")
        );
    }
}
