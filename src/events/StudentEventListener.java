package events;

import java.util.EventListener;

/**
 * Custom EventListener interface (Syllabus Unit 2 - Custom Event Types)
 * Any class that wants to listen for student changes implements this.
 */
public interface StudentEventListener extends EventListener {
    void studentChanged(StudentEvent event);
}
