package dao;

import database.DatabaseConnection;
import model.Expense;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// handles all database operations for expenses
public class ExpenseDAO {

    public boolean save(Expense expense) {
        String sql = "INSERT INTO expenses (amount, category, date, description, studentID) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, expense.getAmount());
            pstmt.setString(2, expense.getCategory());
            pstmt.setString(3, expense.getDate());
            pstmt.setString(4, expense.getDescription());
            pstmt.setInt(5, expense.getStudentID());
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving expense: " + e.getMessage());
            return false;
        }
    }

    public List<Expense> findByStudent(int studentID) {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE studentID = ? ORDER BY date DESC";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Expense e = new Expense();
                e.setExpenseID(rs.getInt("expenseID"));
                e.setAmount(rs.getDouble("amount"));
                e.setCategory(rs.getString("category"));
                e.setDate(rs.getString("date"));
                e.setDescription(rs.getString("description"));
                e.setStudentID(rs.getInt("studentID"));
                list.add(e);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error fetching expenses: " + e.getMessage());
        }
        return list;
    }

    // find expenses for a specific month like "2026-09"
    public List<Expense> findByMonth(int studentID, String monthYear) {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE studentID = ? AND date LIKE ? ORDER BY date DESC";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentID);
            pstmt.setString(2, monthYear + "%");  // matches "2026-09-01", "2026-09-15", etc.
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Expense e = new Expense();
                e.setExpenseID(rs.getInt("expenseID"));
                e.setAmount(rs.getDouble("amount"));
                e.setCategory(rs.getString("category"));
                e.setDate(rs.getString("date"));
                e.setDescription(rs.getString("description"));
                e.setStudentID(rs.getInt("studentID"));
                list.add(e);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error fetching expenses by month: " + e.getMessage());
        }
        return list;
    }

    public boolean delete(int expenseID) {
        String sql = "DELETE FROM expenses WHERE expenseID = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, expenseID);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting expense: " + e.getMessage());
            return false;
        }
    }

    // returns total amount spent in a given month
    public double getTotalForMonth(int studentID, String monthYear) {
        String sql = "SELECT SUM(amount) FROM expenses WHERE studentID = ? AND date LIKE ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentID);
            pstmt.setString(2, monthYear + "%");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting total: " + e.getMessage());
        }
        return 0;
    }
}
