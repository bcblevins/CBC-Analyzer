package org.bcb.model;
import java.time.LocalDateTime;
import java.util.*;

public class LabTest{
    private int id;
    private int patientId;
    private Map<String, BloodParameter> results;
    private List<BloodParameter> bloodParameterList;
    private LocalDateTime timeStamp;
    private List<Tag> tags = new ArrayList<>();
    private String type;

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

    public LocalDateTime getTimestamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    public void appendTags(Tag tag) {
        this.tags.add(tag);
    }
    public List<String> getTagNames() {
        List<String> tagNames = new ArrayList<>();
        if (!this.tags.isEmpty()) {
            for (Tag tag : tags) {
                tagNames.add(tag.getName());
            }
        }
        return tagNames;
    }

    public List<BloodParameter> getBloodParameterList() {
        return bloodParameterList;
    }

    public void setBloodParameterList(List<BloodParameter> bloodParameterList) {
        this.bloodParameterList = bloodParameterList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
