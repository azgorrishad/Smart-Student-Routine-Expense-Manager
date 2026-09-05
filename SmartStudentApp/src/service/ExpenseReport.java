package service;

import model.Expense;
import java.util.List;

// ExpenseReport implements ReportGenerator - demonstrates Polymorphism
public class ExpenseReport implements ReportGenerator {

    private List<Expense> expenseList;
    private String period;  // e.g. "2026-09"

    public ExpenseReport(List<Expense> expenseList, String period) {
        this.expenseList = expenseList;
        this.period = period;
    }

    @Override
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== Expense Report: ").append(period).append(" =====\n\n");
        sb.append(String.format("%-12s %-15s %s\n", "Date", "Category", "Amount (BDT)"));
        sb.append("------------------------------------------\n");

        double total = 0;
        for (Expense e : expenseList) {
            sb.append(String.format("%-12s %-15s %.2f\n",
                    e.getDate(), e.getCategory(), e.getAmount()));
            total += e.getAmount();
        }

        sb.append("------------------------------------------\n");
        sb.append(String.format("Total Spent  : %.2f BDT\n", total));

        return sb.toString();
    }

    public List<Expense> getExpenseList() { return expenseList; }
    public String getPeriod() { return period; }
}
