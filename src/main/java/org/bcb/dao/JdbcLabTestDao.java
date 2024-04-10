package org.bcb.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcLabTestDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcLabTestDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
