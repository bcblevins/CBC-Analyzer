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
import java.util.ArrayList;
import java.util.List;

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
            if (rowSet.next()) {
                patient = mapToPatient(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return patient;
    }

    public Patient getPatientById(int id) {
        Patient patient = null;
        String sql = "SELECT * FROM patient WHERE patient_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            if (rowSet.next()) {
                patient = mapToPatient(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return patient;
    }

    public List<Patient> getPatientsByName(String name, boolean wildCard) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient " +
                "WHERE name ILIKE ?;";
        try {
            if (wildCard) {
                name = "%" + name + "%";
            }
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, name);
            while (rowSet.next()) {
                patients.add(mapToPatient(rowSet));
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        }
        return patients;
    }

    public Patient createPatient(Patient patient) {
        Patient nPatient = null;
        String sql = "INSERT INTO patient(chart_number, name, sex, species, birthday) values " +
                "(?, ?, ?, ?, ?) " +
                "returning patient_id;";
        try {
            int id = jdbcTemplate.queryForObject(sql, int.class, patient.getChartNumber(), patient.getName(), patient.getSex(), patient.getSpecies(), patient.getDateOfBirth());
            nPatient = getPatientById(id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
        return nPatient;
    }

    public Patient updatePatient(Patient patient) {
        Patient updated = null;
        String sql = "UPDATE patient set chart_number = ?, name = ?, sex = ?, species = ?, birthday = ?, active = ? " +
                "where patient_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, patient.getChartNumber(), patient.getName(), patient.getSex(), patient.getSpecies(), patient.getDateOfBirth(), patient.isActive(), patient.getId());
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

    public Patient changePatientActiveStatus(Patient patient) {
        Patient updated = null;
        String sql = "UPDATE patient set active = ? " +
                "where patient_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, !patient.isActive(), patient.getId());
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
    public int[] deletePatient(Patient patient) {
        int patientsAffected = 0;
        int testsAffected = 0;
        //All tables cascade delete except for test, so we can return how many tests were deleted.
        String sqlP = "DELETE FROM patient WHERE patient_id = ?;";
        String sqlT = "DELETE FROM test WHERE patient_id = ?;";

        try {
            patientsAffected = jdbcTemplate.update(sqlP, patient.getId());
            testsAffected = jdbcTemplate.update(sqlT, patient.getId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Database connection error", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation error", e);
        }

        return new int[]{patientsAffected, testsAffected};
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
        if (rowSet.wasNull()) {
            patient.setPatientFound(false);
        }
        return patient;
    }
}
