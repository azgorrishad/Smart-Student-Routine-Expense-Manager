package view;

import model.Budget;
import model.Student;
import service.BudgetService;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BudgetPanel extends JPanel {

    private Student student;
    private BudgetService budgetService;
    private JTextField limitInput;
    private JLabel limitLabel, spentLabel, remainingLabel, statusLabel;
    private JProgressBar progressBar;
    private String currentMonth;  // e.g. "2026-09"

    public BudgetPanel(Student student) {
        this.student = student;
        budgetService = new BudgetService();
        currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        buildUI();
        refreshInfo();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Budget Manager — " + currentMonth);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        add(title, BorderLayout.NORTH);

        // main info area
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        limitLabel     = new JLabel("Monthly Limit : Not Set");
        spentLabel     = new JLabel("Amount Spent  : 0.00 BDT");
        remainingLabel = new JLabel("Remaining     : --");

        Font infoFont = new Font("Arial", Font.PLAIN, 14);
        limitLabel.setFont(infoFont);
        spentLabel.setFont(infoFont);
        remainingLabel.setFont(infoFont);

        infoPanel.add(limitLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(spentLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(remainingLabel);
        infoPanel.add(Box.createVerticalStrut(18));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        infoPanel.add(progressBar);
        infoPanel.add(Box.createVerticalStrut(12));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
        infoPanel.add(statusLabel);

        add(infoPanel, BorderLayout.CENTER);

        // bottom: input + buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        bottomPanel.add(new JLabel("Set Monthly Budget (BDT):"));
        limitInput = new JTextField(10);
        JButton setBtn     = new JButton("Set");
        JButton refreshBtn = new JButton("Refresh");

        bottomPanel.add(limitInput);
        bottomPanel.add(setBtn);
        bottomPanel.add(refreshBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setBtn.addActionListener(e -> handleSetBudget());
        refreshBtn.addActionListener(e -> refreshInfo());
    }

    private void handleSetBudget() {
        String text = limitInput.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a budget amount.");
            return;
        }

        double limit;
        try {
            limit = Double.parseDouble(text);
            if (limit <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive number.");
            return;
        }

        boolean ok = budgetService.setBudget(student.getUserID(), currentMonth, limit);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Budget saved successfully!");
            refreshInfo();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save budget.");
        }
    }

    // reloads budget and spending data from the database
    public void refreshInfo() {
        Budget budget = budgetService.getBudget(student.getUserID(), currentMonth);
        double spent  = budgetService.getSpentAmount(student.getUserID(), currentMonth);

        spentLabel.setText(String.format("Amount Spent  : %.2f BDT", spent));

        if (budget != null) {
            double limit     = budget.getMonthlyLimit();
            double remaining = limit - spent;
            int percent      = Math.min(100, (int) ((spent / limit) * 100));

            limitLabel.setText(String.format("Monthly Limit : %.2f BDT", limit));
            remainingLabel.setText(String.format("Remaining     : %.2f BDT", remaining));
            progressBar.setValue(percent);
            progressBar.setString(percent + "%");

            if (spent >= limit) {
                progressBar.setForeground(Color.RED);
                statusLabel.setText("Warning: Budget exceeded! You've overspent this month.");
                statusLabel.setForeground(Color.RED);
            } else if (spent >= limit * 0.9) {
                progressBar.setForeground(Color.ORANGE);
                statusLabel.setText("Warning: Over 90% of your budget has been used!");
                statusLabel.setForeground(new Color(180, 80, 0));
            } else {
                progressBar.setForeground(new Color(34, 139, 34));
                statusLabel.setText("Budget is on track.");
                statusLabel.setForeground(new Color(34, 100, 34));
            }

            limitInput.setText(String.valueOf(limit));
        } else {
            limitLabel.setText("Monthly Limit : Not Set");
            remainingLabel.setText("Remaining     : --");
            progressBar.setValue(0);
            progressBar.setString("No budget set");
            statusLabel.setText("Set a budget to track your spending.");
            statusLabel.setForeground(Color.DARK_GRAY);
        }
    }
}
