package com.example.sqliteapplication;

public class UserRecord {
    public int id;
    public String name;
    public String contact;
    public String dob;
    public boolean archived;

    public UserRecord(int id, String name, String contact, String dob, boolean archived) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.dob = dob;
        this.archived = archived;
    }

    // ArrayAdapter uses this text for each row in the list
    @Override
    public String toString() {
        return (archived ? "[ARCHIVED] " : "") + name
                + "\nContact: " + contact
                + "\nDOB: " + dob;
    }
}