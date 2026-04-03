package controller;

import dao.StudentDAO;
import events.StudentEvent;
import events.StudentEvent.EventType;
import events.StudentEventListener;
import model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Student Controller (MVC - Controller layer)
 * Bridges the View (Swing) and Model (Student + DAO).
 * Also acts as a JavaBean event source — fires StudentEvents to registered listeners.
 *
 * Syllabus Unit 2: JavaBeans — event model, addXxxListener/removeXxxListener pattern.
 * Syllabus Unit 4: MVC Pattern with Swing.
 */
public class StudentController {

    private final StudentDAO dao;

    // JavaBean event listener list (Syllabus Unit 2: Event Model)
    private final List<StudentEventListener> listeners = new ArrayList<>();

    public StudentController() {
        this.dao = new StudentDAO();
    }

    // ── JavaBean Listener Registration (Unit 2 pattern) ──────────────────────
    public void addStudentEventListener(StudentEventListener listener) {
        listeners.add(listener);
    }

    public void removeStudentEventListener(StudentEventListener listener) {
        listeners.remove(listener);
    }

    // Fire event to all registered listeners
    private void fireStudentEvent(Student student, EventType type) {
        StudentEvent event = new StudentEvent(this, student, type);
        for (StudentEventListener l : listeners) {
            l.studentChanged(event);
        }
    }

    // ── CRUD operations (delegate to DAO, fire events to View) ───────────────

    public List<Student> getAllStudents() {
        try {
            return dao.getAllStudents();
        } catch (SQLException e) {
            System.err.println("[Controller] Error fetching students: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Student> searchStudents(String keyword) {
        try {
            return dao.searchStudents(keyword);
        } catch (SQLException e) {
            System.err.println("[Controller] Search error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean addStudent(Student student) {
        try {
            boolean success = dao.addStudent(student);
            if (success) fireStudentEvent(student, EventType.ADDED);
            return success;
        } catch (SQLException e) {
            System.err.println("[Controller] Add error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStudent(Student student) {
        try {
            boolean success = dao.updateStudent(student);
            if (success) fireStudentEvent(student, EventType.UPDATED);
            return success;
        } catch (SQLException e) {
            System.err.println("[Controller] Update error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(int id) {
        try {
            Student student = dao.getStudentById(id);
            boolean success = dao.deleteStudent(id);
            if (success && student != null) fireStudentEvent(student, EventType.DELETED);
            return success;
        } catch (SQLException e) {
            System.err.println("[Controller] Delete error: " + e.getMessage());
            return false;
        }
    }

    public double getAverageCGPA() {
        try { return dao.getAverageCGPA(); }
        catch (SQLException e) { return 0.0; }
    }

    public int getTotalCount() {
        try { return dao.getTotalCount(); }
        catch (SQLException e) { return 0; }
    }

    // Validation helper (controller-level business logic)
    public String validateStudent(Student s) {
        if (s.getName() == null || s.getName().trim().isEmpty())
            return "Name cannot be empty.";
        if (s.getRollNumber() == null || s.getRollNumber().trim().isEmpty())
            return "Roll Number cannot be empty.";
        if (s.getCourse() == null || s.getCourse().trim().isEmpty())
            return "Course cannot be empty.";
        if (s.getCgpa() < 0.0 || s.getCgpa() > 10.0)
            return "CGPA must be between 0.0 and 10.0.";
        if (s.getAge() < 16 || s.getAge() > 60)
            return "Age must be between 16 and 60.";
        if (s.getEmail() != null && !s.getEmail().trim().isEmpty()
                && !s.getEmail().matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            return "Invalid email format.";
        return null; // null = no error
    }
}
