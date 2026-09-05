package service;

import dao.RoutineDAO;
import model.Routine;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

// checks the routine every minute and shows a popup if a class is 30 mins away
public class ReminderService {

    private RoutineDAO routineDAO;
    private int studentID;
    private Timer reminderTimer;

    public ReminderService(int studentID) {
        this.studentID = studentID;
        routineDAO = new RoutineDAO();
    }

    // starts checking in background using Swing Timer (runs on the EDT)
    public void startChecking(JFrame parentFrame) {
        // check every 60 seconds = 60000 ms
        reminderTimer = new Timer(60000, e -> checkForUpcomingClasses(parentFrame));
        reminderTimer.start();

        // also do an immediate check when app opens
        checkForUpcomingClasses(parentFrame);
    }

    public void stopChecking() {
        if (reminderTimer != null) {
            reminderTimer.stop();
        }
    }

    private void checkForUpcomingClasses(JFrame parentFrame) {
        List<Routine> routines = routineDAO.findByStudent(studentID);

        // get today's full day name e.g. "Monday"
        String today = LocalDate.now()
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        LocalTime now = LocalTime.now();

        for (Routine r : routines) {
            if (!r.getDayOfWeek().equalsIgnoreCase(today)) continue;

            try {
                LocalTime classTime = LocalTime.parse(r.getStartTime());
                long minutesUntilClass = now.until(classTime, ChronoUnit.MINUTES);

                // show notification only if class starts between 28 and 32 minutes from now
                if (minutesUntilClass >= 28 && minutesUntilClass <= 32) {
                    String msg = "Class Reminder!\n\n" +
                                 r.getCourseName() + " starts in 30 minutes.\n" +
                                 "Room: " + r.getLocation();
                    JOptionPane.showMessageDialog(parentFrame, msg,
                            "Upcoming Class", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                // skip this routine if time format is invalid
            }
        }
    }
}
