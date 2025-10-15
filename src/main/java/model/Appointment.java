package com.riara.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int appointmentId, studentId;
    private LocalDate appointmentDate;
    private LocalTime timeSlot;
    private String status;

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public LocalTime getTimeSlot() { return timeSlot; }
    public void setTimeSlot(LocalTime timeSlot) { this.timeSlot = timeSlot; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}