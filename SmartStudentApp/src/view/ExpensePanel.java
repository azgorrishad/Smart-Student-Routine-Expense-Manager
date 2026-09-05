package view;

import model.Expense;
import model.Student;
import service.ExpenseService;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.LocalDate;
import java.util.List;

public class ExpensePanel extends JPanel {

    private Student student;
    private ExpenseService expenseService;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public ExpensePanel(Student student) {
        this.student = student;
        expenseService = new ExpenseService();
        buildUI();
        loadData();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("My Expenses");
        title.setFont(new Font("Arial", Font.BOLD, 15));
        add(title, BorderLayout.NORTH);

        String[] columns = {"ID", "Date", "Category", "Amount (BDT)", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        expenseTable = new JTable(tableModel);
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expenseTable.setRowHeight(22);
        expenseTable.getColumnModel().getColumn(0).setMaxWidth(40);

        add(new JScrollPane(expenseTable), BorderLayout.CENTER);

        // bottom panel: total on left, buttons on right
        JPanel bottomPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total: 0.00 BDT");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 13));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        JButton addBtn     = new JButton("Add Expense");
        JButton deleteBtn  = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showAddDialog());
        deleteBtn.addActionListener(e -> deleteSelected());
        refreshBtn.addActionListener(e -> loadData());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Expense> list = expenseService.getAllExpenses(student.getUserID());
        double total = 0;

        for (Expense e : list) {
            tableModel.addRow(new Object[]{
                e.getExpenseID(),
                e.getDate(),
                e.getCategory(),
                String.format("%.2f", e.getAmount()),
                e.getDescription()
            });
            total += e.getAmount();
        }

        totalLabel.setText(String.format("All-time Total: %.2f BDT", total));
    }

    private void showAddDialog() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parent, "Add Expense", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(320, 255);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(5, 2, 8, 7));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField amountField = new JTextField();
        String[] categories   = {"Food", "Transport", "Books", "Entertainment", "Utilities", "Other"};
        JComboBox<String> catBox = new JComboBox<>(categories);
        JTextField dateField   = new JTextField(LocalDate.now().toString());
        JTextField descField   = new JTextField();

        panel.add(new JLabel("Amount (BDT) *:")); panel.add(amountField);
        panel.add(new JLabel("Category *:"));      panel.add(catBox);
        panel.add(new JLabel("Date (yyyy-MM-dd) *:")); panel.add(dateField);
        panel.add(new JLabel("Description:"));     panel.add(descField);

        JButton saveBtn   = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        panel.add(saveBtn);
        panel.add(cancelBtn);

        dialog.add(panel);

        saveBtn.addActionListener(e -> {
            String amtText = amountField.getText().trim();
            String date    = dateField.getText().trim();
            String cat     = (String) catBox.getSelectedItem();
            String desc    = descField.getText().trim();

            if (amtText.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Amount and Date are required.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amtText);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid positive number for amount.");
                return;
            }

            boolean ok = expenseService.addExpense(amount, cat, date, desc, student.getUserID());
            if (ok) {
                loadData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to save expense.");
            }
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void deleteSelected() {
        int row = expenseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Delete this expense record?", "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            expenseService.deleteExpense(id);
            loadData();
        }
    }
}
