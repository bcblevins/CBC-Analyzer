package org.bcb;

import org.bcb.dao.JdbcPatientDao;
import org.bcb.model.Patient;
import org.bcb.model.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatientDaoTests extends BaseDaoTests {
    private static final List<Tag> tags = Arrays.asList(new Tag("senior", false), new Tag(1, "healthy", true), new Tag(4, "friendly", false));
    private static final Patient PATIENT_1 = new Patient(1, "000000", "Charlie Blevins", "SF", "Canine", LocalDate.parse("2013-03-14"), true, tags);

    private JdbcPatientDao patientDao;

    @Before
    public void setup() {
        patientDao = new JdbcPatientDao(dataSource);
    }

    @Test
    public void getPatientById_with_valid_id_returns_correct() {
        Patient patient = patientDao.getPatientById(1);
        Assert.assertNotNull(patient);
        assertPatientsMatch(PATIENT_1, patient);
    }
    @Test
    public void getPatientByChartNumber_with_valid_id_returns_correct() {
        Patient patient = patientDao.getPatientByChartNumber("000000");
        Assert.assertNotNull(patient);
        assertPatientsMatch(PATIENT_1, patient);
    }
    @Test
    public void getPatientsByName_with_valid_name_returns_correct_list() {
        List<Patient> patients = patientDao.getPatientsByName("Charlie Blevins", false);
        Assert.assertFalse(patients.isEmpty());
        assertPatientsMatch(patients.get(0), PATIENT_1);
    }
    @Test
    public void createPatient_creates_patient() {
        Patient patient = new Patient("000000", "Charlie Blevins", "SF", "Canine", LocalDate.parse("2013-03-14"), true, tags);
        patient = patientDao.createPatient(patient);
        assertPatientsMatch(PATIENT_1, patient);
    }
    @Test
    public void updatePatient_updates_patient() {
        Patient patient = new Patient(1, "000000", "Charlie Bean Blevins", "F", "Canine", LocalDate.parse("2014-03-14"), true, tags);
        Patient updatedPatient = patientDao.updatePatient(patient);
        updatedPatient = patientDao.getPatientById(1);
        assertPatientsMatch(patient, updatedPatient);
    }
    @Test
    public void deletePatient_deletes_patient() {
        int[] patientsAndTestsAffected = patientDao.deletePatient(PATIENT_1);
        Assert.assertEquals(new int[]{1, 1}, patientsAndTestsAffected);
        Assert.assertNull(patientDao.getPatientById(PATIENT_1.getId()));
    }
    //linkTagToPatient
    //unlinkTagFromPatient
    public void assertPatientsMatch(Patient expected, Patient actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getChartNumber(), actual.getChartNumber());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getSex(), actual.getSex());
        Assert.assertEquals(expected.getSpecies(), actual.getSpecies());
        Assert.assertEquals(expected.getDateOfBirth().toString(), actual.getDateOfBirth().toString());
        Assert.assertEquals(expected.isActive(), actual.isActive());
        Assert.assertEquals(expected.getTagNames().toArray(), actual.getTagNames().toArray());
    }
}
