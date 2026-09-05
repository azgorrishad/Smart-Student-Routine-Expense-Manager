package dao;

import database.DatabaseConnection;
import model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// handles all database operations related to users
public class UserDAO {

    public boolean saveUser(Student student) {
        String sql = "INSERT INTO users (username, password, name, email, major, phoneNumber) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, student.getUsername());
            pstmt.setString(2, student.getPassword());
            pstmt.setString(3, student.getName());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, student.getMajor());
            pstmt.setString(6, student.getPhoneNumber());
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    public Student findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Student student = new Student();
                student.setUserID(rs.getInt("userID"));
                student.setUsername(rs.getString("username"));
                student.setPassword(rs.getString("password"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setMajor(rs.getString("major"));
                student.setPhoneNumber(rs.getString("phoneNumber"));
                rs.close();
                pstmt.close();
                return student;
            }
        } catch (SQLException e) {
            System.out.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    public boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }
}
