package com.example.wordle.rowMapper;

import com.example.wordle.domain.Histogram;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HistogramRowMapper implements RowMapper<Histogram> {

    @Override
    public Histogram map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Histogram(rs.getLong("attempts"), rs.getLong("sum"));
    }
}
