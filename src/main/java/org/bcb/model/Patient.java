package org.bcb.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Patient{
    private int id;
    private String chartNumber;
    private String name;
    private String sex;
    private String species;
    private LocalDate dateOfBirth;
    private String ageTag;
    private List<Tag> tags = new ArrayList<>();
    private boolean isQuitPatient;
    private boolean isPatientFound = true;
    private boolean isActive = true;
    private boolean isAgeTagSet = false;

    public void setPatientFound(boolean patientFound) {
        isPatientFound = patientFound;
    }
    public boolean isPatientFound() {
        return isPatientFound;
    }
    public Patient (boolean isQuitPatient) {
        this.isQuitPatient = isQuitPatient;
    }
    public Patient(int id, String chartNumber, String name, String sex, String species, LocalDate dateOfBirth) {
        this.id = id;
        this.chartNumber = chartNumber;
        this.name = name;
        this.sex = sex;
        this.species = species;
        this.dateOfBirth = dateOfBirth;
        setAgeTag();
    }
    public Patient(String chartNumber, String name, String sex, String species, LocalDate dateOfBirth) {
        this.chartNumber = chartNumber;
        this.name = name;
        this.sex = sex;
        this.species = species;
        this.dateOfBirth = dateOfBirth;
    }
    public Patient(int id, String chartNumber, String name, String sex, String species, LocalDate dateOfBirth, boolean isActive, List<Tag> tags) {
        this.id = id;
        this.chartNumber = chartNumber;
        this.name = name;
        this.sex = sex;
        this.species = species;
        this.dateOfBirth = dateOfBirth;
        this.isActive = isActive;
        this.tags = tags;
    }
    public Patient(String chartNumber, String name, String sex, String species, LocalDate dateOfBirth, boolean isActive, List<Tag> tags) {
        this.id = id;
        this.chartNumber = chartNumber;
        this.name = name;
        this.sex = sex;
        this.species = species;
        this.dateOfBirth = dateOfBirth;
        this.isActive = isActive;
        this.tags = tags;
    }
    public String getAgeTag() {
        return ageTag;
    }
    public void setAgeTag() {
        if (this.dateOfBirth != null && !isAgeTagSet) {
            Period period = Period.between(this.dateOfBirth, LocalDate.now());
            if (period.getYears() >= 10) {
                this.ageTag = "senior";
            } else if (period.getYears() >= 5) {
                this.ageTag = "adult";
            } else if (period.getYears() >= 1) {
                this.ageTag = "adolescent";
            } else {
                this.ageTag = "puppy";
            }
            tags.add(0, new Tag(ageTag, false));
            this.isAgeTagSet = true;
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


    public List<Tag> getTags() {
        return tags;
    }
    public List<String> getTagNames() {
        List<String> tagNames = new ArrayList<>();
        for (Tag tag : tags) {
            tagNames.add(tag.getName());
        }
        return tagNames;
    }

    public void setTags(List<Tag> tags) {
        this.isAgeTagSet = false;
        this.tags = tags;
        setAgeTag();
    }
    public void appendTags(Tag tag) {
        this.tags.add(tag);
    }
}

