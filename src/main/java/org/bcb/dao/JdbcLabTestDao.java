package org.bcb.dao;

import org.bcb.app.Main;
import org.bcb.exception.DaoException;
import org.bcb.model.BloodParameter;
import org.bcb.model.LabTest;
import org.bcb.model.Patient;
import org.bcb.model.Tag;
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
    JdbcTagDao jdbcTagDao = new JdbcTagDao(Main.dataSource);

    public JdbcLabTestDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public LabTest getLabTestById(int id) {
        LabTest test = null;
        String sql = "SELECT * FROM test WHERE test_id = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            //TODO: following line is false, why?
            if (rowSet.next()) {
                test = mapToLabTestSimple(rowSet);
            }

        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return test;
    }
    public List<LabTest> getLabTestByDate(LocalDate date, Patient patient) {
        List<LabTest> tests = new ArrayList<>();
        String sql = "SELECT test.*, parameter.parameter_id, parameter.name, result.result_value, parameter.range_low, parameter.range_high, parameter.unit " +
                "FROM test " +
                "JOIN result ON result.test_id = test.test_id " +
                "JOIN parameter ON parameter.parameter_id = result.parameter_id " +
                "WHERE DATE(time_stamp) = ? AND patient_id = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, date, patient.getId());
            while (rowSet.next()) {
                LabTest labTest = mapToLabTest(rowSet);
                labTest.setTags(jdbcTagDao.getTagsForTest(labTest));
                tests.add(labTest);
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return tests;
    }
    public List<LabTest> getLabTestsByPatient(Patient patient) {
        List<LabTest> tests = new ArrayList<>();
        String sql = "SELECT test.*, parameter.parameter_id, parameter.name, result.result_value, parameter.range_low, parameter.range_high, parameter.unit " +
                "FROM test " +
                "JOIN result ON result.test_id = test.test_id " +
                "JOIN parameter ON parameter.parameter_id = result.parameter_id " +
                "WHERE patient_id = ? " +
                "ORDER BY test.time_stamp;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, patient.getId());
            while (rowSet.next()) {
                LabTest labTest = mapToLabTest(rowSet);
                labTest.setTags(jdbcTagDao.getTagsForTest(labTest));
                tests.add(labTest);
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return tests;
    }
    public List<LabTest> getLabTestsByTags(List<String> tags, boolean wildCard) {
        List<LabTest> tests = new ArrayList<>();
        String sql = "SELECT test.*, parameter.parameter_id, parameter.name, result.result_value, parameter.range_low, parameter.range_high, parameter.unit " +
                "FROM test " +
                "JOIN result ON result.test_id = test.test_id " +
                "JOIN parameter ON parameter.parameter_id = result.parameter_id " +
                "JOIN test_tag ON test_tag.test_id = test.test_id " +
                "JOIN tag ON tag.tag_id = test_tag.tag_id " +
                "WHERE tag.name ILIKE ?";
        try {
            for (String tag : tags) {
                if (wildCard) {
                    tag = "%" + tag + "%";
                }
                SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, tag);
                while (rowSet.next()) {
                    LabTest labTest = mapToLabTest(rowSet);
                    labTest.setTags(jdbcTagDao.getTagsForTest(labTest));
                    tests.add(labTest);
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
        newTest.setBloodParameterList(bloodParameterList);
        return newTest;
    }

    //TODO: Not reaching here
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
    public void linkTagToLabTest(LabTest labTest, Tag tag) {
        int rowsAffected;
        String sql = "INSERT INTO test_tag (test_id, tag_id) values " +
                "(?, ?);";
        try {
            rowsAffected = jdbcTemplate.update(sql, labTest.getId(), tag.getId());
            if (rowsAffected == 0) {
                throw new DaoException("Failed to link tag to test");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
    }

    private LabTest mapToLabTestSimple (SqlRowSet rowSet) {
        LabTest labTest = new LabTest();
        labTest.setType("CBC");
        labTest.setId(rowSet.getInt("test_id"));
        labTest.setPatientId(rowSet.getInt("patient_id"));
        if (!rowSet.wasNull()) {
            labTest.setTimeStamp(rowSet.getTimestamp("time_stamp").toLocalDateTime());
        }

        return labTest;
    }
    private LabTest mapToLabTest (SqlRowSet rowSet) {
        LabTest labTest = new LabTest();
        boolean isFirstPass = true;
        Map<String, BloodParameter> results = new HashMap();
        labTest.setType("CBC");
        boolean isTimeToStop = false;

        while (true) {
            if (isFirstPass) {
                labTest.setId(rowSet.getInt("test_id"));
                labTest.setPatientId(rowSet.getInt("patient_id"));
                if (!rowSet.wasNull()) {
                    labTest.setTimeStamp(rowSet.getTimestamp("time_stamp").toLocalDateTime());
                }
                isFirstPass = false;
            }
            int parameterId = rowSet.getInt("parameter_id");
            BloodParameter bloodParameter = new BloodParameter(
                    parameterId,
                    rowSet.getBigDecimal("result_value").doubleValue(),
                    rowSet.getString("name"),
                    rowSet.getBigDecimal("range_low").doubleValue(),
                    rowSet.getBigDecimal("range_high").doubleValue(),
                    rowSet.getString("unit")
            );
            results.put(bloodParameter.getName(), bloodParameter);

            if (parameterId == 6) {
                break;
            }
            rowSet.next();
        }
        labTest.setResults(results);
        labTest.setBloodParameterList(new ArrayList<>(results.values()));
        return labTest;
    }
}
