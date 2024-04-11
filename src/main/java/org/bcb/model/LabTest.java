package org.bcb.model;
import java.time.LocalDateTime;
import java.util.*;

public class LabTest {
    private int id;
    private int patientId;
    private Map<String, BloodParameter> results;
    private LocalDateTime timeStamp;

    public LabTest() {
    }

    public LabTest(int id, int patientId, Map<String, BloodParameter> results, LocalDateTime timeStamp) {
        this.id = id;
        this.patientId = patientId;
        this.results = results;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public Map<String, BloodParameter> getResults() {
        return results;
    }

    public void setResults(Map<String, BloodParameter> results) {
        this.results = results;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
