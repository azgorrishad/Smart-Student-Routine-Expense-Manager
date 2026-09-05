package model;

// A reminder tied to a routine class - used for in-app notifications
public class Reminder {

    private int reminderID;
    private String message;
    private String reminderTime;  // time to show the reminder
    private boolean isActive;
    private int studentID;
    private int routineID;

    public Reminder() {}

    public Reminder(int reminderID, String message, String reminderTime,
                    boolean isActive, int studentID, int routineID) {
        this.reminderID = reminderID;
        this.message = message;
        this.reminderTime = reminderTime;
        this.isActive = isActive;
        this.studentID = studentID;
        this.routineID = routineID;
    }

    public int getReminderID() { return reminderID; }
    public String getMessage() { return message; }
    public String getReminderTime() { return reminderTime; }
    public boolean isActive() { return isActive; }
    public int getStudentID() { return studentID; }
    public int getRoutineID() { return routineID; }

    public void setReminderID(int reminderID) { this.reminderID = reminderID; }
    public void setMessage(String message) { this.message = message; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }
    public void setActive(boolean active) { isActive = active; }
    public void setStudentID(int studentID) { this.studentID = studentID; }
    public void setRoutineID(int routineID) { this.routineID = routineID; }
}
