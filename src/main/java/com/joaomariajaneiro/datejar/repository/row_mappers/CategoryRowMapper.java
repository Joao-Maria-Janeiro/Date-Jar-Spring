package com.joaomariajaneiro.datejar.repository.row_mappers;

import com.joaomariajaneiro.datejar.model.Category;

import com.joaomariajaneiro.datejar.model.enums.Type;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CategoryRowMapper implements RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Type> values = Arrays.asList(Type.values());
        return new Category(
                rs.getInt("id"),
                rs.getString("name"),
                values.get(rs.getInt("type"))
        );
    }
}
