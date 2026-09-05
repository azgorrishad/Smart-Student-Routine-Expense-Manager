package service;

import dao.RoutineDAO;
import model.Routine;
import utils.ValidationUtil;
import java.util.List;

// business logic for managing class routines
public class RoutineService {

    private RoutineDAO routineDAO;

    public RoutineService() {
        routineDAO = new RoutineDAO();
    }

    public boolean addRoutine(String courseCode, String courseName, String dayOfWeek,
                              String startTime, String location, int studentID) {
        if (ValidationUtil.isEmptyOrNull(courseName) ||
            ValidationUtil.isEmptyOrNull(dayOfWeek) ||
            ValidationUtil.isEmptyOrNull(startTime)) {
            return false;
        }

        Routine routine = new Routine();
        routine.setCourseCode(courseCode);
        routine.setCourseName(courseName);
        routine.setDayOfWeek(dayOfWeek);
        routine.setStartTime(startTime);
        routine.setLocation(location);
        routine.setStudentID(studentID);

        return routineDAO.save(routine);
    }

    public List<Routine> getRoutines(int studentID) {
        return routineDAO.findByStudent(studentID);
    }

    public boolean updateRoutine(Routine routine) {
        return routineDAO.update(routine);
    }

    public boolean deleteRoutine(int routineID) {
        return routineDAO.delete(routineID);
    }
}
