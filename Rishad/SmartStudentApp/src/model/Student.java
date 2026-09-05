package model;

// Student extends User - adds personal information
// This demonstrates Inheritance (OOP concept)
public class Student extends User {

    private String name;
    private String email;
    private String major;
    private String phoneNumber;

    public Student() {
        super();
    }

    public Student(int userID, String username, String password,
                   String name, String email, String major, String phoneNumber) {
        super(userID, username, password);
        this.name = name;
        this.email = email;
        this.major = major;
        this.phoneNumber = phoneNumber;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMajor() { return major; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setMajor(String major) { this.major = major; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return "Student: " + name + " (" + getUsername() + ")";
    }
}
