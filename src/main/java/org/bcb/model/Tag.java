package org.bcb.model;

public class Tag {
    private int id;
    private String name;
    private boolean isDiagnosis;

    public Tag(int id, String name, boolean isDiagnosis) {
        this.id = id;
        this.name = name;
        this.isDiagnosis = isDiagnosis;
    }

    public Tag(String name, boolean isDiagnosis) {
        this.name = name;
        this.isDiagnosis = isDiagnosis;
    }
    public String toString() {
        return name + " [" + (isDiagnosis ? "diagnosis" : "not a diagnosis") + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDiagnosis() {
        return isDiagnosis;
    }

    public void setDiagnosis(boolean diagnosis) {
        isDiagnosis = diagnosis;
    }
}
