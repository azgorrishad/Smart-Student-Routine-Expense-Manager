package service;

import dao.ExpenseDAO;
import model.Expense;
import java.util.List;

// business logic for managing expenses
public class ExpenseService {

    private ExpenseDAO expenseDAO;

    public ExpenseService() {
        expenseDAO = new ExpenseDAO();
    }

    public boolean addExpense(double amount, String category, String date,
                              String description, int studentID) {
        if (amount <= 0 || category == null || date == null) {
            return false;
        }

        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setDate(date);
        expense.setDescription(description);
        expense.setStudentID(studentID);

        return expenseDAO.save(expense);
    }

    public List<Expense> getAllExpenses(int studentID) {
        return expenseDAO.findByStudent(studentID);
    }

    public List<Expense> getMonthlyExpenses(int studentID, String monthYear) {
        return expenseDAO.findByMonth(studentID, monthYear);
    }

    public double getMonthlyTotal(int studentID, String monthYear) {
        return expenseDAO.getTotalForMonth(studentID, monthYear);
    }

    public boolean deleteExpense(int expenseID) {
        return expenseDAO.delete(expenseID);
    }
}
