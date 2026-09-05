package service;

import dao.UserDAO;
import model.Student;
import utils.PasswordUtil;
import utils.ValidationUtil;

// handles user login and registration
public class AuthenticationService {

    private UserDAO userDAO;

    public AuthenticationService() {
        userDAO = new UserDAO();
    }

    // returns Student object if login is successful, null if not
    public Student login(String username, String password) {
        if (ValidationUtil.isEmptyOrNull(username) || ValidationUtil.isEmptyOrNull(password)) {
            return null;
        }

        Student student = userDAO.findByUsername(username);
        if (student == null) {
            return null;  // username does not exist
        }

        if (PasswordUtil.verifyPassword(password, student.getPassword())) {
            return student;
        }

        return null;  // password wrong
    }

    // returns true if account was created, false if username taken or data invalid
    public boolean register(String username, String password, String name,
                            String email, String major, String phone) {
        if (ValidationUtil.isEmptyOrNull(username) ||
            ValidationUtil.isEmptyOrNull(password) ||
            ValidationUtil.isEmptyOrNull(name)) {
            return false;
        }

        if (userDAO.usernameExists(username)) {
            return false;  // username already taken
        }

        String hashedPassword = PasswordUtil.hashPassword(password);

        Student student = new Student();
        student.setUsername(username);
        student.setPassword(hashedPassword);
        student.setName(name);
        student.setEmail(email);
        student.setMajor(major);
        student.setPhoneNumber(phone);

        return userDAO.saveUser(student);
    }
}
