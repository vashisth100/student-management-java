package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC Database Connection (Syllabus Unit 3 - JDBC)
 * Uses SQLite — no external server needed. DB file auto-created on first run.
 */
public class DatabaseConnection {

    // SQLite stores the DB as a single file in the project root
    private static final String DB_URL = "jdbc:sqlite:students.db";
    private static Connection connection = null;

    // ── Singleton Connection ──────────────────────────────────────────────────
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load SQLite JDBC Driver (Unit 3: JDBC Drivers for RDBM Systems)
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("[DB] Connected to SQLite: " + DB_URL);
                initializeDatabase();
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC driver not found. Add sqlite-jdbc.jar to /lib folder.\n" + e.getMessage());
            }
        }
        return connection;
    }

    // ── Auto-create table on first launch ────────────────────────────────────
    private static void initializeDatabase() throws SQLException {
        String createTable = """
                CREATE TABLE IF NOT EXISTS students (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    name        TEXT    NOT NULL,
                    roll_number TEXT    NOT NULL UNIQUE,
                    course      TEXT    NOT NULL,
                    branch      TEXT    NOT NULL,
                    cgpa        REAL    NOT NULL DEFAULT 0.0,
                    email       TEXT,
                    phone       TEXT,
                    age         INTEGER,
                    gender      TEXT
                );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTable);

            // Seed some sample data only if the table is empty
            String countSQL = "SELECT COUNT(*) FROM students";
            var rs = stmt.executeQuery(countSQL);
            if (rs.next() && rs.getInt(1) == 0) {
                seedSampleData(stmt);
            }
        }
        System.out.println("[DB] Table 'students' ready.");
    }

    // ── Seed initial demo records ─────────────────────────────────────────────
    private static void seedSampleData(Statement stmt) throws SQLException {
        String[] inserts = {
            "INSERT INTO students (name,roll_number,course,branch,cgpa,email,phone,age,gender) VALUES ('Priyanshu Sharma','24BCE10360','B.Tech','CSE',8.5,'priyanshu@vit.ac.in','9876543210',20,'Male')",
            "INSERT INTO students (name,roll_number,course,branch,cgpa,email,phone,age,gender) VALUES ('Ananya Verma','24BCE10361','B.Tech','ECE',9.1,'ananya@vit.ac.in','9876543211',20,'Female')",
            "INSERT INTO students (name,roll_number,course,branch,cgpa,email,phone,age,gender) VALUES ('Rohit Mishra','24BCE10362','B.Tech','MECH',7.8,'rohit@vit.ac.in','9876543212',21,'Male')",
            "INSERT INTO students (name,roll_number,course,branch,cgpa,email,phone,age,gender) VALUES ('Sneha Gupta','24BCE10363','B.Tech','IT',8.9,'sneha@vit.ac.in','9876543213',20,'Female')",
            "INSERT INTO students (name,roll_number,course,branch,cgpa,email,phone,age,gender) VALUES ('Arjun Patel','24BCE10364','M.Tech','CSE',8.2,'arjun@vit.ac.in','9876543214',23,'Male')",
        };
        for (String sql : inserts) {
            stmt.execute(sql);
        }
        System.out.println("[DB] Sample data seeded.");
    }

    // ── Close connection ──────────────────────────────────────────────────────
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error closing connection: " + e.getMessage());
        }
    }
}
