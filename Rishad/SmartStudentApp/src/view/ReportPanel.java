package view;

import model.Budget;
import model.Expense;
import model.Student;
import service.BudgetService;
import service.ExpenseReport;
import service.ExpenseService;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportPanel extends JPanel {

    private Student student;
    private ExpenseService expenseService;
    private BudgetService budgetService;
    private JTextArea reportArea;
    private JComboBox<String> monthBox;

    public ReportPanel(Student student) {
        this.student = student;
        expenseService = new ExpenseService();
        budgetService  = new BudgetService();
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Monthly Expense Reports");
        title.setFont(new Font("Arial", Font.BOLD, 15));
        add(title, BorderLayout.NORTH);

        // report text area (read-only, monospaced so columns align)
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(new JScrollPane(reportArea), BorderLayout.CENTER);

        // bottom controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        controlPanel.add(new JLabel("Select Month:"));

        // populate with last 6 months
        String[] months = buildMonthOptions();
        monthBox = new JComboBox<>(months);

        JButton generateBtn = new JButton("Generate Report");

        controlPanel.add(monthBox);
        controlPanel.add(generateBtn);
        add(controlPanel, BorderLayout.SOUTH);

        generateBtn.addActionListener(e -> generateReport());
    }

    private String[] buildMonthOptions() {
        String[] options = new String[6];
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        for (int i = 0; i < 6; i++) {
            options[i] = LocalDate.now().minusMonths(i).format(fmt);
        }
        return options;
    }

    private void generateReport() {
        String month = (String) monthBox.getSelectedItem();
        List<Expense> expenses = expenseService.getMonthlyExpenses(student.getUserID(), month);

        // use ExpenseReport which implements ReportGenerator interface
        ExpenseReport report = new ExpenseReport(expenses, month);
        StringBuilder sb = new StringBuilder(report.generateReport());

        // add category breakdown
        sb.append("\n--- Category Breakdown ---\n");
        Map<String, Double> byCategory = new HashMap<>();
        for (Expense e : expenses) {
            double existing = byCategory.getOrDefault(e.getCategory(), 0.0);
            byCategory.put(e.getCategory(), existing + e.getAmount());
        }

        if (byCategory.isEmpty()) {
            sb.append("  No expenses recorded.\n");
        } else {
            for (Map.Entry<String, Double> entry : byCategory.entrySet()) {
                sb.append(String.format("  %-15s : %.2f BDT\n",
                        entry.getKey(), entry.getValue()));
            }
        }

        // add budget vs actual section
        Budget budget = budgetService.getBudget(student.getUserID(), month);
        if (budget != null) {
            double spent = expenseService.getMonthlyTotal(student.getUserID(), month);
            sb.append("\n--- Budget vs Actual ---\n");
            sb.append(String.format("  Budget Limit : %.2f BDT\n", budget.getMonthlyLimit()));
            sb.append(String.format("  Total Spent  : %.2f BDT\n", spent));

            double diff = budget.getMonthlyLimit() - spent;
            if (diff >= 0) {
                sb.append(String.format("  Saved        : %.2f BDT\n", diff));
            } else {
                sb.append(String.format("  Over Budget  : %.2f BDT\n", Math.abs(diff)));
            }
        } else {
            sb.append("\n(No budget was set for this month)\n");
        }

        reportArea.setText(sb.toString());
    }
}
