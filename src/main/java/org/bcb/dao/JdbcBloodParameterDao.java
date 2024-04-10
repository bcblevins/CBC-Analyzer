package org.bcb.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcBloodParameterDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcBloodParameterDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


}
