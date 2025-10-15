package com.riara.dao;

import com.riara.model.Notification;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public boolean save(Notification notif) {
        String sql = "INSERT INTO notifications (student_id, message, type, is_read) VALUES (?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, notif.getStudentId());
            s.setString(2, notif.getMessage());
            s.setString(3, notif.getType());
            s.setBoolean(4, notif.isRead());
            return s.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Notification> getByStudentId(int studentId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE student_id = ? ORDER BY sent_at DESC";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, studentId);
            ResultSet r = s.executeQuery();
            while (r.next()) {
                Notification n = new Notification();
                n.setNotificationId(r.getInt("notification_id"));
                n.setStudentId(r.getInt("student_id"));
                n.setMessage(r.getString("message"));
                n.setType(r.getString("type"));
                n.setRead(r.getBoolean("is_read"));
                n.setSentAt(r.getTimestamp("sent_at").toLocalDateTime());
                list.add(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}