package view;

import model.Student;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom TableModel for JTable — Swing MVC (Syllabus Unit 4)
 */
public class StudentTableModel extends AbstractTableModel {

    private static final String[] COLUMNS = {
        "ID", "Name", "Roll Number", "Course", "Branch", "CGPA", "Email", "Phone", "Age", "Gender"
    };

    private List<Student> students;

    public StudentTableModel() {
        this.students = new ArrayList<>();
    }

    public void setStudents(List<Student> students) {
        this.students = students;
        fireTableDataChanged();
    }

    public Student getStudentAt(int row) {
        return students.get(row);
    }

    @Override public int getRowCount()    { return students.size(); }
    @Override public int getColumnCount() { return COLUMNS.length;  }
    @Override public String getColumnName(int col) { return COLUMNS[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        Student s = students.get(row);
        return switch (col) {
            case 0 -> s.getId();
            case 1 -> s.getName();
            case 2 -> s.getRollNumber();
            case 3 -> s.getCourse();
            case 4 -> s.getBranch();
            case 5 -> String.format("%.2f", s.getCgpa());
            case 6 -> s.getEmail();
            case 7 -> s.getPhone();
            case 8 -> s.getAge();
            case 9 -> s.getGender();
            default -> "";
        };
    }

    @Override
    public Class<?> getColumnClass(int col) {
        if (col == 0) return Integer.class;
        if (col == 8) return Integer.class;
        return String.class;
    }
}
