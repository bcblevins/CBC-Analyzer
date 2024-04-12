package org.bcb.dao;

import org.bcb.app.Main;
import org.bcb.exception.DaoException;
import org.bcb.model.BloodParameter;
import org.bcb.model.LabTest;
import org.bcb.model.Patient;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public List<LabTest> getLabTestByDate(LocalDate date, Patient patient) {
        List<LabTest> tests = new ArrayList<>();
        String sql = "SELECT * FROM test " +
                "WHERE DATE(time_stamp) = ? AND patient_id = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, date, patient.getId());
            while (rowSet.next()) {
                tests.add(mapToLabTest(rowSet));
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return tests;
    }
    public List<LabTest> getLabTestsByPatient(Patient patient) {
        List<LabTest> tests = new ArrayList<>();
        String sql = "SELECT * FROM test " +
                "WHERE patient_id = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, patient.getId());
            while (rowSet.next()) {
                tests.add(mapToLabTest(rowSet));
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return tests;
    }
    public List<LabTest> getLabTestsByTags(List<String> tags, boolean wildCard) {
        List<LabTest> tests = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM test " +
                "JOIN test_tag ON test_tag.test_id = test.test_id " +
                "JOIN tag ON tag.tag_id = test_tag.tag_id" +
                "WHERE tag ILIKE ?;";
        try {
            for (String tag : tags) {
                if (wildCard) {
                    tag = "%" + tag + "%";
                }
                SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, tag);
                while (rowSet.next()) {
                    tests.add(mapToLabTest(rowSet));
                }
            }

        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return tests;
    }

    public LabTest createTest(List<BloodParameter> bloodParameterList, LocalDateTime timeStamp, Patient patient) {
        LabTest newTest = null;
        String sql = "INSERT INTO test (patient_id, time_stamp) values " +
                "(?, ?) " +
                "RETURNING test_id;";
        try {
            int id = jdbcTemplate.queryForObject(sql, int.class, patient.getId(), timeStamp);
            newTest = getLabTestById(id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
        linkTestToResults(bloodParameterList, newTest);
        return newTest;
    }

    private void linkTestToResults(List<BloodParameter> bloodParameterList, LabTest labTest) {
        for (BloodParameter bloodParameter : bloodParameterList) {
            String sql = "INSERT INTO result (test_id, parameter_id, result_value) VALUES " +
                    "(?, ?, ?);";
            try {
                int rowsAffected = jdbcTemplate.update(sql, labTest.getId(), bloodParameter.getId(), bloodParameter.getResult());
                if (rowsAffected == 0) {
                    throw new DaoException("Failed to link test to result");
                }
            } catch (CannotGetJdbcConnectionException e) {
                throw new DaoException("Could not connect to database");
            } catch (DataIntegrityViolationException e) {
                throw new DaoException("Data integrity violation");
            }
        }
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
