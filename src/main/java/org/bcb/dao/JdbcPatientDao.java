package org.bcb.dao;

import org.bcb.exception.DaoException;
import org.bcb.model.Patient;
import org.bcb.model.LabTest;
import org.bcb.model.Tag;
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
        Patient patient = null;
        String sql = "SELECT * FROM patient WHERE chart_number = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, chartNumber);
            patient = mapToPatient(rowSet);
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return patient;
    }

    public Patient getPatientById(int id) {
        Patient patient = null;
        String sql = "SELECT * FROM patient WHERE patient_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            patient = mapToPatient(rowSet);
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return patient;
    }
    public Patient createPatient(Patient patient) {
        Patient nPatient = null;
        String sql = "INSERT INTO patient(chart_number, name, sex, species, birthday) values " +
                "(?, ?, ?, ?, ?) " +
                "returning patient_id;";
        try {
            int id = jdbcTemplate.queryForObject(sql, int.class, patient.getChartNumber(), patient.getName(), patient.getSex(), patient.getSpecies(), patient.getDateOfBirth());
            nPatient = getPatientById(id);
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
        return nPatient;
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


    public void linkTestToPatient(Patient patient, LabTest labTest) {
        int rowsAffected;
        String sql = "INSERT INTO patient_test(patient_id, test_id) VALUES " +
                "(?, ?);";
        try {
            rowsAffected = jdbcTemplate.update(sql, patient.getId(), labTest.getId());
            if (rowsAffected == 0) {
                throw new DaoException("Failed to link test to patient");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
    }
    public void linkTagToPatient(Patient patient, Tag tag) {
        int rowsAffected;
        String sql = "INSERT INTO patient_test(patient_id, tag_id) values " +
                "(?, ?);";
        try {
            rowsAffected = jdbcTemplate.update(sql, patient, tag);
            if (rowsAffected == 0) {
                throw new DaoException("Failed to link tag to patient");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
    }

    private Patient mapToPatient(SqlRowSet rowSet) {
        Patient patient = new Patient(
                rowSet.getInt("patient_id"),
                rowSet.getString("chart_number"),
                rowSet.getString("name"),
                rowSet.getString("sex"),
                rowSet.getString("species"),
                rowSet.getDate("birthday").toLocalDate()
        );
        return patient;
    }
}
