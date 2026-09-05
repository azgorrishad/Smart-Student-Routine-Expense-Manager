package dao;

import database.DatabaseConnection;
import model.Budget;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// handles all database operations for the monthly budget
public class BudgetDAO {

    public boolean save(Budget budget) {
        String sql = "INSERT INTO budgets (monthYear, monthlyLimit, studentID) VALUES (?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, budget.getMonthYear());
            pstmt.setDouble(2, budget.getMonthlyLimit());
            pstmt.setInt(3, budget.getStudentID());
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving budget: " + e.getMessage());
            return false;
        }
    }

    public Budget findByMonth(int studentID, String monthYear) {
        String sql = "SELECT * FROM budgets WHERE studentID = ? AND monthYear = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentID);
            pstmt.setString(2, monthYear);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Budget b = new Budget();
                b.setBudgetID(rs.getInt("budgetID"));
                b.setMonthYear(rs.getString("monthYear"));
                b.setMonthlyLimit(rs.getDouble("monthlyLimit"));
                b.setStudentID(rs.getInt("studentID"));
                rs.close();
                pstmt.close();
                return b;
            }
        } catch (SQLException e) {
            System.out.println("Error finding budget: " + e.getMessage());
        }
        return null;
    }

    public boolean update(Budget budget) {
        String sql = "UPDATE budgets SET monthlyLimit = ? WHERE budgetID = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, budget.getMonthlyLimit());
            pstmt.setInt(2, budget.getBudgetID());
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating budget: " + e.getMessage());
            return false;
        }
    }
}
