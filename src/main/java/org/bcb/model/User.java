package org.bcb.model;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private boolean isDoctor;
    private String username;
    private String password;
    private boolean isLockedOut = false;

    public User() {
    }

    public User(int id, String firstName, String lastName, boolean isDoctor, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isDoctor = isDoctor;
        this.username = username;
        this.password = password;
    }

    public User(String firstName, String lastName, boolean isDoctor, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isDoctor = isDoctor;
        this.username = username;
        this.password = password;
    }

    public boolean validatePassword(String password) {
        return password.equals(this.password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isDoctor() {
        return isDoctor;
    }

    public void setDoctor(boolean doctor) {
        isDoctor = doctor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLockedOut() {
        return isLockedOut;
    }

    public void setLockedOut(boolean lockedOut) {
        isLockedOut = lockedOut;
    }
}
