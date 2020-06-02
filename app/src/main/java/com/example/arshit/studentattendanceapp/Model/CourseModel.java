package com.example.arshit.studentattendanceapp.Model;

public class CourseModel {

    private String id;
    private String CourseCode;
    private String CourseName;

    public CourseModel() { }

    public CourseModel(String id, String courseCode, String courseName) {
        this.id = id;
        CourseCode = courseCode;
        CourseName = courseName;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getCourseCode() { return CourseCode; }

    public void setCourseCode(String courseCode) { CourseCode = courseCode; }

    public String getCourseName() { return CourseName; }

    public void setCourseName(String courseName) { CourseName = courseName; }
}
