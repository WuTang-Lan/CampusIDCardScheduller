package com.riara.dao;

import com.riara.model.IdCard;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IdCardDAO {

    public boolean save(IdCard card) {
        String sql = "INSERT INTO id_cards (student_id, status, ready_date) VALUES (?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, card.getStudentId());
            s.setString(2, card.getStatus());
            s.setDate(3, card.getReadyDate() != null ? Date.valueOf(card.getReadyDate()) : null);
            return s.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatus(int studentId, String status, LocalDate readyDate) {
        String sql = "UPDATE id_cards SET status = ?, ready_date = ? WHERE student_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, status);
            s.setDate(2, readyDate != null ? Date.valueOf(readyDate) : null);
            s.setInt(3, studentId);
            return s.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public IdCard findByStudentId(int studentId) {
        String sql = "SELECT * FROM id_cards WHERE student_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, studentId);
            ResultSet r = s.executeQuery();
            if (r.next()) {
                IdCard card = new IdCard();
                card.setCardId(r.getInt("card_id"));
                card.setStudentId(r.getInt("student_id"));
                card.setStatus(r.getString("status"));
                card.setReadyDate(r.getDate("ready_date") != null ? r.getDate("ready_date").toLocalDate() : null);
                card.setCollectedDate(r.getDate("collected_date") != null ? r.getDate("collected_date").toLocalDate() : null);
                return card;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}