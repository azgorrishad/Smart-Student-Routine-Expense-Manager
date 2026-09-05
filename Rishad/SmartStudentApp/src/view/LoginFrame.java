package view;

import model.Student;
import service.AuthenticationService;
import javax.swing.BorderFactory;
import javax.swing.Box;
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

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthenticationService authService;

    public LoginFrame() {
        authService = new AuthenticationService();
        buildUI();
    }

    private void buildUI() {
        setTitle("Smart Student Manager - Login");
        setSize(370, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // title at the top
        JLabel title = new JLabel("Smart Student Manager", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 17));
        mainPanel.add(title, BorderLayout.NORTH);

        // username + password fields
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> openRegister());

        // pressing Enter in the password field triggers login
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your username and password.",
                    "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = authService.login(username, password);

        if (student != null) {
            DashboardFrame dashboard = new DashboardFrame(student);
            dashboard.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void openRegister() {
        RegisterFrame registerFrame = new RegisterFrame(this);
        registerFrame.setVisible(true);
        this.setVisible(false);
    }
}
