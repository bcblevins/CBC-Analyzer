package org.bcb.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    private int id;
    private String chartNumber;
    private String name;
    private String sex;
    private String species;
    private LocalDate dateOfBirth;
    private String ageFlag;
    private List<String> flags = new ArrayList<>();
    private String recordFilePath;
    private boolean isNullPatient;
    private boolean isPatientFound = true;

    public void setPatientFound(boolean patientFound) {
        isPatientFound = patientFound;
    }

    public boolean isPatientFound() {
        return isPatientFound;
    }

    private boolean isActive = true;
    public Patient (boolean isNullPatient) {
        this.isNullPatient = isNullPatient;
    }

    public Patient(String chartNumber, String name, String sex, String species, LocalDate dateOfBirth, List<String> flags, String recordFilePath) {
        this.chartNumber = chartNumber;
        this.name = name;
        this.sex = sex;
        this.species = species;
        this.dateOfBirth = dateOfBirth;
        this.flags = flags;
        this.recordFilePath = recordFilePath;
        setAgeFlag();
    }
    public Patient(int id, String chartNumber, String name, String sex, String species, LocalDate dateOfBirth) {
        this.id = id;
        this.chartNumber = chartNumber;
        this.name = name;
        this.sex = sex;
        this.species = species;
        this.dateOfBirth = dateOfBirth;
    }

    public Patient(String chartNumber, String name, String sex, String species, LocalDate dateOfBirth) {
        this.chartNumber = chartNumber;
        this.name = name;
        this.sex = sex;
        this.species = species;
        this.dateOfBirth = dateOfBirth;
    }

    public String getAgeFlag() {
        return ageFlag;
    }

    public void setAgeFlag() {
        Period period = Period.between(this.dateOfBirth, LocalDate.now());
        if (period.getYears() >= 10) {
            this.ageFlag = "senior";
        } else if (period.getYears() >= 5) {
            this.ageFlag = "adult";
        } else if (period.getYears() >= 1){
            this.ageFlag = "adolescent";
        } else {
            this.ageFlag = "puppy";
        }
    }

    public String getChartNumber() {
        return chartNumber;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getSpecies() {
        return species;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public List<String> getFlags() {
        return flags;
    }

    public String getRecordFilePath() {
        return recordFilePath;
    }

    public boolean isNullPatient() {
        return isNullPatient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
