package dao;

import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Student Data Access Object (DAO)
 * Syllabus Unit 3: JDBC — PreparedStatements, ResultSet, connection pooling concept,
 * SQL-to-Java type mapping, Transactions (coding transactions).
 */
public class StudentDAO {

    // ── CREATE ────────────────────────────────────────────────────────────────
    public boolean addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (name, roll_number, course, branch, cgpa, email, phone, age, gender) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Use Transaction for data integrity (Syllabus: Coding Transactions)
        Connection conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // SQL to Java Type Mapping (Syllabus Unit 3)
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getRollNumber());
            pstmt.setString(3, student.getCourse());
            pstmt.setString(4, student.getBranch());
            pstmt.setDouble(5, student.getCgpa());
            pstmt.setString(6, student.getEmail());
            pstmt.setString(7, student.getPhone());
            pstmt.setInt(8,    student.getAge());
            pstmt.setString(9, student.getGender());

            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
            return rowsAffected > 0;
        } catch (SQLException e) {
            conn.rollback();
            conn.setAutoCommit(true);
            throw e;
        }
    }

    // ── READ ALL ──────────────────────────────────────────────────────────────
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id ASC";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapRow(rs));  // SQL to Java mapping
            }
        }
        return students;
    }

    // ── READ BY ID ────────────────────────────────────────────────────────────
    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    // ── SEARCH ────────────────────────────────────────────────────────────────
    public List<Student> searchStudents(String keyword) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE "
                   + "LOWER(name) LIKE ? OR LOWER(roll_number) LIKE ? OR "
                   + "LOWER(course) LIKE ? OR LOWER(branch) LIKE ?";

        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            String pattern = "%" + keyword.toLowerCase() + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);
            pstmt.setString(4, pattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(mapRow(rs));
            }
        }
        return students;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET name=?, roll_number=?, course=?, branch=?, "
                   + "cgpa=?, email=?, phone=?, age=?, gender=? WHERE id=?";

        Connection conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getRollNumber());
            pstmt.setString(3, student.getCourse());
            pstmt.setString(4, student.getBranch());
            pstmt.setDouble(5, student.getCgpa());
            pstmt.setString(6, student.getEmail());
            pstmt.setString(7, student.getPhone());
            pstmt.setInt(8,    student.getAge());
            pstmt.setString(9, student.getGender());
            pstmt.setInt(10,   student.getId());

            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
            return rowsAffected > 0;
        } catch (SQLException e) {
            conn.rollback();
            conn.setAutoCommit(true);
            throw e;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";

        Connection conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
            return rowsAffected > 0;
        } catch (SQLException e) {
            conn.rollback();
            conn.setAutoCommit(true);
            throw e;
        }
    }

    // ── STATISTICS ────────────────────────────────────────────────────────────
    public double getAverageCGPA() throws SQLException {
        String sql = "SELECT AVG(cgpa) FROM students";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    public int getTotalCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM students";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // ── SQL → Java type mapping helper ────────────────────────────────────────
    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setRollNumber(rs.getString("roll_number"));
        s.setCourse(rs.getString("course"));
        s.setBranch(rs.getString("branch"));
        s.setCgpa(rs.getDouble("cgpa"));
        s.setEmail(rs.getString("email"));
        s.setPhone(rs.getString("phone"));
        s.setAge(rs.getInt("age"));
        s.setGender(rs.getString("gender"));
        return s;
    }
}
