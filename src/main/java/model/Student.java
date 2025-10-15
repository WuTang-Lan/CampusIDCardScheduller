package com.riara.model;

public class Student {
    private int studentId;
    private String fullName;
    private String regNo;
    private String email;
    private String password;

    // Constructors
    public Student() {}

    public Student(String fullName, String regNo, String email, String password) {
        this.fullName = fullName;
        this.regNo = regNo;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}