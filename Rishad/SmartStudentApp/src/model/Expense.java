package model;

// Represents a single spending entry
public class Expense {

    private int expenseID;
    private double amount;
    private String category;     // Food, Transport, Books, etc.
    private String date;         // stored as yyyy-MM-dd
    private String description;
    private int studentID;

    public Expense() {}

    public Expense(int expenseID, double amount, String category,
                   String date, String description, int studentID) {
        this.expenseID = expenseID;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.studentID = studentID;
    }

    public int getExpenseID() { return expenseID; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public int getStudentID() { return studentID; }

    public void setExpenseID(int expenseID) { this.expenseID = expenseID; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(String date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }
    public void setStudentID(int studentID) { this.studentID = studentID; }
}
