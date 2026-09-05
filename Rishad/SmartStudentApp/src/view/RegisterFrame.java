package view;

import service.AuthenticationService;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class RegisterFrame extends JFrame {

    private JTextField nameField, emailField, majorField, phoneField, usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private AuthenticationService authService;
    private JFrame loginFrame;

    public RegisterFrame(JFrame loginFrame) {
        this.loginFrame = loginFrame;
        authService = new AuthenticationService();
        buildUI();
    }

    private void buildUI() {
        setTitle("Register - Smart Student Manager");
        setSize(420, 390);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel title = new JLabel("Create New Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 7));

        formPanel.add(new JLabel("Full Name *"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Major (e.g. CSE)"));
        majorField = new JTextField();
        formPanel.add(majorField);

        formPanel.add(new JLabel("Phone"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Username *"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password * (min 6)"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Confirm Password *"));
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");
        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        registerBtn.addActionListener(e -> handleRegister());
        backBtn.addActionListener(e -> goBack());
    }

    private void handleRegister() {
        String name     = nameField.getText().trim();
        String email    = emailField.getText().trim();
        String major    = majorField.getText().trim();
        String phone    = phoneField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm  = new String(confirmPasswordField.getPassword());

        // required fields check
        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name, Username, and Password are required.", "Missing Fields",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 6 characters.", "Weak Password",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = authService.register(username, password, name, email, major, phone);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Account created successfully! You can now log in.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            goBack();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Username already taken. Please choose a different one.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBack() {
        loginFrame.setVisible(true);
        this.dispose();
    }
}
