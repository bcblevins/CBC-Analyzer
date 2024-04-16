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
    private List<String> tags = new ArrayList<>();
    private String recordFilePath;
    private boolean isQuitPatient;
    private boolean isPatientFound = true;
    private boolean isActive = true;
    public void setPatientFound(boolean patientFound) {
        isPatientFound = patientFound;
    }

    public boolean isPatientFound() {
        return isPatientFound;
    }


    public Patient (boolean isQuitPatient) {
        this.isQuitPatient = isQuitPatient;
    }

    public Patient(String chartNumber, String name, String sex, String species, LocalDate dateOfBirth, List<String> tags, String recordFilePath) {
        this.chartNumber = chartNumber;
        this.name = name;
        this.sex = sex;
        this.species = species;
        this.dateOfBirth = dateOfBirth;
        this.tags = tags;
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

    public Patient(int id, String name, String sex, String species, LocalDate dateOfBirth, boolean isActive) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.species = species;
        this.dateOfBirth = dateOfBirth;
        this.isActive = isActive;
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
    public String toString() {
        return chartNumber + " : " + name + " : " + species + " : " + sex + " : " + dateOfBirth.toString() + " : " + (isActive ? "Active" : "Inactive");
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

    public List<String> getTags() {
        return tags;
    }

    public String getRecordFilePath() {
        return recordFilePath;
    }

    public boolean isQuitPatient() {
        return isQuitPatient;
    }

    public void setQuitPatient(boolean quitPatient) {
        isQuitPatient = quitPatient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}

