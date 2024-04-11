package org.bcb.dao;

import org.bcb.exception.DaoException;
import org.bcb.model.BloodParameter;
import org.bcb.model.LabTest;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcLabTestDao {
    private final JdbcTemplate jdbcTemplate;
    public JdbcLabTestDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public LabTest getLabTestById(int id) {
        LabTest test = null;
        String sql = "SELECT test.*, parameter.parameter_id, parameter.name, result.result_value, parameter.range_low, parameter.range_high, parameter.unit " +
                "FROM test " +
                "JOIN result ON result.test_id = test.test_id " +
                "JOIN parameter ON parameter.parameter_id = result.parameter_id " +
                "WHERE test.test_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            test = mapToLabTest(rowSet);
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return test;
    }

    private LabTest mapToLabTest (SqlRowSet rowSet) {
        LabTest labTest = new LabTest();
        boolean isFirstPass = true;
        Map<String, BloodParameter> results = new HashMap();

        while (rowSet.next()) {
            if (isFirstPass) {
                labTest.setId(rowSet.getInt("test_id"));
                labTest.setPatientId(rowSet.getInt("patient_id"));
                if (!rowSet.wasNull()) {
                    labTest.setTimeStamp(rowSet.getTimestamp("time_stamp").toLocalDateTime());
                }
                isFirstPass = false;
            }
            BloodParameter bloodParameter = new BloodParameter(
                    rowSet.getInt("parameter_id"),
                    rowSet.getBigDecimal("result_value").doubleValue(),
                    rowSet.getString("name"),
                    rowSet.getBigDecimal("range_low").doubleValue(),
                    rowSet.getBigDecimal("range_high").doubleValue(),
                    rowSet.getString("unit")
            );
            results.put(bloodParameter.getName(), bloodParameter);

        }
        labTest.setResults(results);
        return labTest;
    }
}
