package com.example.arshit.studentattendanceapp.Model;

public class AttendanceModel {

    private String id;
    private String present;
    private String absent;

    public AttendanceModel() {
    }

    public AttendanceModel(String id, String present, String absent) {
        this.id = id;
        this.present = present;
        this.absent = absent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }
}
