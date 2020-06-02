package com.example.arshit.studentattendanceapp.Model;

public class Teacher {
    private String AdmissionYear;
    private String DOB;
    private String DeptField;
    private String DeptName;
    private String DeptSpec;
    private String Email;

    private String  Name;
    private String  Password;
    private String  PhoneNumber;

    private String  imageURL;

    private String TeacherId;





    public String getName() {
        return Name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getAdmissionYear() {
        return AdmissionYear;
    }

    public String getDOB() {
        return DOB;
    }

    public String getDeptField() {
        return DeptField;
    }

    public String getDeptName() {
        return DeptName;
    }

    public String getDeptSpec() {
        return DeptSpec;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

   public Teacher(){}

    public void setAdmissionYear(String admissionYear) {
        AdmissionYear = admissionYear;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public void setDeptField(String deptField) {
        DeptField = deptField;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public void setDeptSpec(String deptSpec) {
        DeptSpec = deptSpec;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }



    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTeacherId() {
        return TeacherId;
    }


}
