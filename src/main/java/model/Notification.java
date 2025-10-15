package com.riara.model;

import java.time.LocalDateTime;

public class Notification {
    private int notificationId;
    private int studentId;
    private String message;
    private String type; // AppointmentBooked, IDReady, IDCollected
    private boolean isRead;
    private LocalDateTime sentAt;

    // Getters & Setters
    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}