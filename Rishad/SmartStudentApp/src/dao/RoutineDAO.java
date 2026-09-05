package dao;

import database.DatabaseConnection;
import model.Routine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// handles all database operations for class routines
public class RoutineDAO {

    public boolean save(Routine routine) {
        String sql = "INSERT INTO routines (courseCode, courseName, dayOfWeek, startTime, location, studentID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, routine.getCourseCode());
            pstmt.setString(2, routine.getCourseName());
            pstmt.setString(3, routine.getDayOfWeek());
            pstmt.setString(4, routine.getStartTime());
            pstmt.setString(5, routine.getLocation());
            pstmt.setInt(6, routine.getStudentID());
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving routine: " + e.getMessage());
            return false;
        }
    }

    public List<Routine> findByStudent(int studentID) {
        List<Routine> list = new ArrayList<>();
        String sql = "SELECT * FROM routines WHERE studentID = ? ORDER BY dayOfWeek, startTime";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Routine r = new Routine();
                r.setRoutineID(rs.getInt("routineID"));
                r.setCourseCode(rs.getString("courseCode"));
                r.setCourseName(rs.getString("courseName"));
                r.setDayOfWeek(rs.getString("dayOfWeek"));
                r.setStartTime(rs.getString("startTime"));
                r.setLocation(rs.getString("location"));
                r.setStudentID(rs.getInt("studentID"));
                list.add(r);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error fetching routines: " + e.getMessage());
        }
        return list;
    }

    public boolean update(Routine routine) {
        String sql = "UPDATE routines SET courseCode=?, courseName=?, dayOfWeek=?, startTime=?, location=? " +
                     "WHERE routineID=?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, routine.getCourseCode());
            pstmt.setString(2, routine.getCourseName());
            pstmt.setString(3, routine.getDayOfWeek());
            pstmt.setString(4, routine.getStartTime());
            pstmt.setString(5, routine.getLocation());
            pstmt.setInt(6, routine.getRoutineID());
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating routine: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int routineID) {
        String sql = "DELETE FROM routines WHERE routineID = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, routineID);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting routine: " + e.getMessage());
            return false;
        }
    }
}
