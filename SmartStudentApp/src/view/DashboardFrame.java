package view;

import model.Student;
import service.ReminderService;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;

public class DashboardFrame extends JFrame {

    private Student currentStudent;
    private ReminderService reminderService;

    public DashboardFrame(Student student) {
        this.currentStudent = student;
        buildUI();
        startReminderService();
    }

    private void buildUI() {
        setTitle("Smart Student Manager - " + currentStudent.getName());
        setSize(870, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // top bar shows who is logged in
        JPanel topBar = new JPanel(new BorderLayout(10, 5));
        topBar.setBackground(new Color(44, 62, 80));
        topBar.setPreferredSize(new java.awt.Dimension(0, 38));

        JLabel infoLabel = new JLabel("  Logged in as: " + currentStudent.getName()
                + "  |  Major: " + currentStudent.getMajor());
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 11));

        topBar.add(infoLabel, BorderLayout.WEST);
        topBar.add(logoutBtn, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // tabs - one per feature
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));

        tabs.addTab("  Routine  ", new RoutinePanel(currentStudent));
        tabs.addTab("  Expenses  ", new ExpensePanel(currentStudent));
        tabs.addTab("  Budget  ", new BudgetPanel(currentStudent));
        tabs.addTab("  Reports  ", new ReportPanel(currentStudent));

        add(tabs, BorderLayout.CENTER);

        logoutBtn.addActionListener(e -> logout());
    }

    private void startReminderService() {
        reminderService = new ReminderService(currentStudent.getUserID());
        reminderService.startChecking(this);
    }

    private void logout() {
        reminderService.stopChecking();
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }
}
