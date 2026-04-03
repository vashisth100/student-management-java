-- Student Management System — Database Schema
-- SQLite (auto-created when app runs)
-- For reference only — this runs automatically via DatabaseConnection.java

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

-- Sample Data (auto-inserted by DatabaseConnection.java on first run)
INSERT INTO students (name, roll_number, course, branch, cgpa, email, phone, age, gender)
VALUES
  ('Priyanshu Sharma', '24BCE10360', 'B.Tech', 'CSE',  8.5, 'priyanshu@vit.ac.in', '9876543210', 20, 'Male'),
  ('Ananya Verma',     '24BCE10361', 'B.Tech', 'ECE',  9.1, 'ananya@vit.ac.in',    '9876543211', 20, 'Female'),
  ('Rohit Mishra',     '24BCE10362', 'B.Tech', 'MECH', 7.8, 'rohit@vit.ac.in',     '9876543212', 21, 'Male'),
  ('Sneha Gupta',      '24BCE10363', 'B.Tech', 'IT',   8.9, 'sneha@vit.ac.in',     '9876543213', 20, 'Female'),
  ('Arjun Patel',      '24BCE10364', 'M.Tech', 'CSE',  8.2, 'arjun@vit.ac.in',     '9876543214', 23, 'Male');
