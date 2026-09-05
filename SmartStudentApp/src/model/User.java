package model;

// Base class - holds the common account info
public class User {

    private int userID;
    private String username;
    private String password; // always stored as a hash, never plain text

    public User() {}

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public int getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setUserID(int userID) { this.userID = userID; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
