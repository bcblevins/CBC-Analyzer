package org.bcb.dao;

import org.bcb.exception.DaoException;
import org.bcb.model.BloodParameter;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import javax.sql.DataSource;


public class JdbcBloodParameterDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcBloodParameterDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public BloodParameter getBloodParameterById(int id) {
        BloodParameter bloodParameter = null;
        SqlRowSet rowSet;
        String sql = "SELECT * FROM parameter WHERE parameter_id = ?";
        try {
            rowSet = jdbcTemplate.queryForRowSet(sql, id);
            if (rowSet.next()) {
                bloodParameter = mapToBloodParameter(rowSet);
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return bloodParameter;
    }

    public BloodParameter getBloodParameterByName(String name) {
        BloodParameter bloodParameter = null;
        SqlRowSet rowSet;
        String sql = "SELECT * FROM parameter WHERE name ilike ?";
        try {
            rowSet = jdbcTemplate.queryForRowSet(sql, name);
            if (rowSet.next()) {
                bloodParameter = mapToBloodParameter(rowSet);
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return bloodParameter;
    }

//    public List<BloodParameter> getResultsFromTest(LabTest test) {
//        List<BloodParameter> results = new ArrayList<>();
//        String sql = "SELECT test.*, parameter.parameter_id, parameter.name, result.result_value, parameter.range_low, parameter.range_high, parameter.unit " +
//                "FROM test " +
//                "JOIN result ON result.test_id = test.test_id " +
//                "JOIN parameter ON parameter.parameter_id = result.parameter_id " +
//                "WHERE test.test_id = ?";
//        try {
//            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, test.getId());
//            test = mapToBloodParameter(rowSet);
//        }  catch (CannotGetJdbcConnectionException e) {
//            throw new DaoException("Could not connect to database");
//        }
//        return test;
//    }
    private BloodParameter mapToBloodParameter(SqlRowSet rowSet) {
        return new BloodParameter(
                rowSet.getInt("parameter_id"),
                rowSet.getBigDecimal("result_value").doubleValue(),
                rowSet.getString("name"),
                rowSet.getBigDecimal("range_low").doubleValue(),
                rowSet.getBigDecimal("range_high").doubleValue(),
                rowSet.getString("unit")
        );
    }

}
