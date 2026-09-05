package model;

// Stores the monthly spending limit set by the student
public class Budget {

    private int budgetID;
    private String monthYear;    // format: "2026-09"
    private double monthlyLimit; // maximum allowed spending
    private int studentID;

    public Budget() {}

    public Budget(int budgetID, String monthYear, double monthlyLimit, int studentID) {
        this.budgetID = budgetID;
        this.monthYear = monthYear;
        this.monthlyLimit = monthlyLimit;
        this.studentID = studentID;
    }

    public int getBudgetID() { return budgetID; }
    public String getMonthYear() { return monthYear; }
    public double getMonthlyLimit() { return monthlyLimit; }
    public int getStudentID() { return studentID; }

    public void setBudgetID(int budgetID) { this.budgetID = budgetID; }
    public void setMonthYear(String monthYear) { this.monthYear = monthYear; }
    public void setMonthlyLimit(double monthlyLimit) { this.monthlyLimit = monthlyLimit; }
    public void setStudentID(int studentID) { this.studentID = studentID; }
}
