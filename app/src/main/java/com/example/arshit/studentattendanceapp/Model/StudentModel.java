package com.example.arshit.studentattendanceapp.Model;

public class StudentModel {

    private  String id;
    private String UserName;
    private String PhoneNumber;


    public StudentModel() {
    }

    public StudentModel(String id, String userName, String phoneNumber) {
        this.id = id;
        UserName = userName;
        PhoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
