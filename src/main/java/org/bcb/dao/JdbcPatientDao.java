package org.bcb.dao;

import org.bcb.exception.DaoException;
import org.bcb.model.Patient;
import org.bcb.model.LabTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;

public class JdbcPatientDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcPatientDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public Patient getPatientByChartNumber(String chartNumber) {
        String sql = "SELECT * FROM patient WHERE chart_number = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, chartNumber);

        return mapToPatient(rowSet);
    }

    public Patient updatePatient(Patient patient) {
        Patient updated = null;
        String sql = "UPDATE patient set chart_number = ?, name = ?, sex = ?, species = ?, birthday = ? " +
                "where patient_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, patient.getChartNumber(), patient.getName(), patient.getSex(), patient.getSpecies(), patient.getDateOfBirth(), patient.getId());
            if (rowsAffected == 0) {
                throw new DaoException("No rows updated, expected at least 1.");
            } else {
                updated = getPatientByChartNumber(patient.getChartNumber());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
        return updated;
    }

    // Don't want to allow deletion of patients. Can be marked inactive instead.
    public Patient deletePatient(Patient patient) {
        Patient updated = null;
        String sql = "UPDATE patient set active = false " +
                "where patient_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, patient.getChartNumber(), patient.getName(), patient.getSex(), patient.getSpecies(), patient.getDateOfBirth(), patient.getId());
            if (rowsAffected == 0) {
                throw new DaoException("No rows updated, expected at least 1.");
            } else {
                updated = getPatientByChartNumber(patient.getChartNumber());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
        return updated;
    }


//TODO:
//    public void linkTestToPatient(LabTest labTest, Patient patient) {
//        String sql = "INSERT INTO patient_test(patient_id, test_id) VALUES " +
//                "(?, ?);";
//        try {
//            jdbcTemplate.update(sql, patient.getId(), labTest.getTestId())
//        }
//    }

    private Patient mapToPatient(SqlRowSet rowSet) {
        Patient patient = new Patient(
                rowSet.getInt("patient_id"),
                rowSet.getString("chart_number"),
                rowSet.getString("name"),
                rowSet.getString("sex"),
                rowSet.getString("species"),
                rowSet.getDate("birthday").toLocalDate()
        );

    }
}
