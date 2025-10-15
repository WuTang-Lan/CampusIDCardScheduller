package com.riara.dao;

import com.riara.model.Appointment;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public boolean save(Appointment app) {
        String sql = "INSERT INTO appointments (student_id, appointment_date, time_slot, status) VALUES (?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, app.getStudentId());
            s.setDate(2, Date.valueOf(app.getAppointmentDate()));
            s.setTime(3, Time.valueOf(app.getTimeSlot()));
            s.setString(4, app.getStatus());
            return s.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, status);
            s.setInt(2, appointmentId);
            return s.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Appointment> getAll() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC";
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(sql)) {
            while (r.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(r.getInt("appointment_id"));
                a.setStudentId(r.getInt("student_id"));
                a.setAppointmentDate(r.getDate("appointment_date").toLocalDate());
                a.setTimeSlot(r.getTime("time_slot").toLocalTime());
                a.setStatus(r.getString("status"));
                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ NEW: Get appointments for a specific student
    public List<Appointment> getByStudentId(int studentId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE student_id = ? ORDER BY appointment_date DESC";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, studentId);
            ResultSet r = s.executeQuery();
            while (r.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(r.getInt("appointment_id"));
                a.setStudentId(r.getInt("student_id"));
                a.setAppointmentDate(r.getDate("appointment_date").toLocalDate());
                a.setTimeSlot(r.getTime("time_slot").toLocalTime());
                a.setStatus(r.getString("status"));
                list.add(a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}