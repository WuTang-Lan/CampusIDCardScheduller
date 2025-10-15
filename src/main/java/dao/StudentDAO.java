package com.riara.dao;

import com.riara.model.Student;
import java.sql.*;

public class StudentDAO {
    public Student findByEmail(String email) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement("SELECT * FROM students WHERE email = ?")) {
            s.setString(1, email);
            ResultSet r = s.executeQuery();
            if (r.next()) {
                Student s1 = new Student();
                s1.setStudentId(r.getInt("student_id"));
                s1.setEmail(r.getString("email"));
                s1.setPassword(r.getString("password"));
                s1.setFullName(r.getString("full_name"));
                s1.setRegNo(r.getString("reg_no"));
                return s1;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
    // In StudentDAO.java
    public Student findById(int id) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement("SELECT * FROM students WHERE student_id = ?")) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            if (r.next()) {
                Student s1 = new Student();
                s1.setStudentId(r.getInt("student_id"));
                s1.setEmail(r.getString("email"));
                s1.setFullName(r.getString("full_name"));
                return s1;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}