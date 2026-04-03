package events;

import model.Student;
import java.util.EventObject;

/**
 * Custom Event (Syllabus Unit 2 - Custom Event Types)
 * Fired whenever a student record is added, updated, or deleted.
 */
public class StudentEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    public enum EventType { ADDED, UPDATED, DELETED, SELECTED }

    private final Student  student;
    private final EventType eventType;

    public StudentEvent(Object source, Student student, EventType eventType) {
        super(source);
        this.student   = student;
        this.eventType = eventType;
    }

    public Student getStudent()      { return student; }
    public EventType getEventType()  { return eventType; }

    @Override
    public String toString() {
        return "StudentEvent{type=" + eventType + ", student=" + student + "}";
    }
}
