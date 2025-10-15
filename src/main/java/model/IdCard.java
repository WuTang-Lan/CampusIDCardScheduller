package com.riara.model;

import java.time.LocalDate;

public class IdCard {
    private int cardId;
    private int studentId;
    private String status; // Processing, Ready, Collected
    private LocalDate readyDate;
    private LocalDate collectedDate;

    // Getters & Setters
    public int getCardId() { return cardId; }
    public void setCardId(int cardId) { this.cardId = cardId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getReadyDate() { return readyDate; }
    public void setReadyDate(LocalDate readyDate) { this.readyDate = readyDate; }

    public LocalDate getCollectedDate() { return collectedDate; }
    public void setCollectedDate(LocalDate collectedDate) { this.collectedDate = collectedDate; }
}