package service;

import dao.BudgetDAO;
import dao.ExpenseDAO;
import model.Budget;

// handles budget setting and checking
public class BudgetService {

    private BudgetDAO budgetDAO;
    private ExpenseDAO expenseDAO;

    public BudgetService() {
        budgetDAO = new BudgetDAO();
        expenseDAO = new ExpenseDAO();
    }

    // creates a new budget or updates existing one for the given month
    public boolean setBudget(int studentID, String monthYear, double limit) {
        if (limit <= 0) return false;

        Budget existing = budgetDAO.findByMonth(studentID, monthYear);

        if (existing != null) {
            existing.setMonthlyLimit(limit);
            return budgetDAO.update(existing);
        } else {
            Budget budget = new Budget();
            budget.setMonthYear(monthYear);
            budget.setMonthlyLimit(limit);
            budget.setStudentID(studentID);
            return budgetDAO.save(budget);
        }
    }

    public Budget getBudget(int studentID, String monthYear) {
        return budgetDAO.findByMonth(studentID, monthYear);
    }

    public double getSpentAmount(int studentID, String monthYear) {
        return expenseDAO.getTotalForMonth(studentID, monthYear);
    }

    // returns true if spending reached 90% or more of the budget
    public boolean isBudgetWarning(int studentID, String monthYear) {
        Budget budget = getBudget(studentID, monthYear);
        if (budget == null) return false;

        double spent = expenseDAO.getTotalForMonth(studentID, monthYear);
        return spent >= budget.getMonthlyLimit() * 0.9;
    }
}
