package model;

// Represents one class in a student's weekly routine
public class Routine {

    private int routineID;
    private String courseCode;   // e.g. CSE-301
    private String courseName;   // e.g. Data Structures
    private String dayOfWeek;    // e.g. Monday
    private String startTime;    // e.g. 09:00
    private String location;     // room number / building
    private int studentID;

    public Routine() {}

    public Routine(int routineID, String courseCode, String courseName,
                   String dayOfWeek, String startTime, String location, int studentID) {
        this.routineID = routineID;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.location = location;
        this.studentID = studentID;
    }

    public int getRoutineID() { return routineID; }
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public String getDayOfWeek() { return dayOfWeek; }
    public String getStartTime() { return startTime; }
    public String getLocation() { return location; }
    public int getStudentID() { return studentID; }

    public void setRoutineID(int routineID) { this.routineID = routineID; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setLocation(String location) { this.location = location; }
    public void setStudentID(int studentID) { this.studentID = studentID; }
}
