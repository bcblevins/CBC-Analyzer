package org.example;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String id;
    private String name;
    private String sex;
    private String species;
    private LocalDate dateOfBirth;
    private String ageFlag;
    private List<String> flags = new ArrayList<>();
    private String recordFilePath;
    private boolean isNullPatient;
    public Patient (boolean isNullPatient) {
        this.isNullPatient = isNullPatient;
    }

    public Patient(String id, String name, String sex, String species, LocalDate dateOfBirth, List<String> flags, String recordFilePath) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.species = species;
        this.dateOfBirth = dateOfBirth;
        this.flags = flags;
        this.recordFilePath = recordFilePath;
        setAgeFlag();
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

    public String getId() {
        return id;
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
}
