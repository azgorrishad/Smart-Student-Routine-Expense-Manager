package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static Connection conn = null;

    // singleton - only one connection shared across the app
    public static Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:student_app.db");
                createTables();
            } catch (Exception e) {
                System.out.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return conn;
    }

    private static void createTables() throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "userID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "name TEXT," +
                "email TEXT," +
                "major TEXT," +
                "phoneNumber TEXT)");

        stmt.execute("CREATE TABLE IF NOT EXISTS routines (" +
                "routineID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "courseCode TEXT," +
                "courseName TEXT," +
                "dayOfWeek TEXT," +
                "startTime TEXT," +
                "location TEXT," +
                "studentID INTEGER," +
                "FOREIGN KEY(studentID) REFERENCES users(userID))");

        stmt.execute("CREATE TABLE IF NOT EXISTS expenses (" +
                "expenseID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "amount REAL," +
                "category TEXT," +
                "date TEXT," +
                "description TEXT," +
                "studentID INTEGER," +
                "FOREIGN KEY(studentID) REFERENCES users(userID))");

        stmt.execute("CREATE TABLE IF NOT EXISTS budgets (" +
                "budgetID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "monthYear TEXT," +
                "monthlyLimit REAL," +
                "studentID INTEGER," +
                "FOREIGN KEY(studentID) REFERENCES users(userID))");

        stmt.execute("CREATE TABLE IF NOT EXISTS reminders (" +
                "reminderID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "message TEXT," +
                "reminderTime TEXT," +
                "isActive INTEGER DEFAULT 1," +
                "studentID INTEGER," +
                "routineID INTEGER," +
                "FOREIGN KEY(studentID) REFERENCES users(userID)," +
                "FOREIGN KEY(routineID) REFERENCES routines(routineID))");

        stmt.close();
    }
}
