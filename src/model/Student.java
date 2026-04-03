package model;

import java.io.Serializable;

/**
 * Student JavaBean - Model layer (MVC)
 * Follows JavaBean conventions: default constructor, private fields, public getters/setters, Serializable
 */
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    // Private fields (Encapsulation - JavaBean standard)
    private int id;
    private String name;
    private String rollNumber;
    private String course;
    private String branch;
    private double cgpa;
    private String email;
    private String phone;
    private int age;
    private String gender;

    // ── Default Constructor (required for JavaBean) ──────────────────────────
    public Student() {}

    // ── Parameterized Constructor ─────────────────────────────────────────────
    public Student(String name, String rollNumber, String course, String branch,
                   double cgpa, String email, String phone, int age, String gender) {
        this.name       = name;
        this.rollNumber = rollNumber;
        this.course     = course;
        this.branch     = branch;
        this.cgpa       = cgpa;
        this.email      = email;
        this.phone      = phone;
        this.age        = age;
        this.gender     = gender;
    }

    // ── Getters & Setters (JavaBean properties) ───────────────────────────────
    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }

    public String getName()               { return name; }
    public void setName(String name)      { this.name = name; }

    public String getRollNumber()                      { return rollNumber; }
    public void setRollNumber(String rollNumber)       { this.rollNumber = rollNumber; }

    public String getCourse()             { return course; }
    public void setCourse(String course)  { this.course = course; }

    public String getBranch()             { return branch; }
    public void setBranch(String branch)  { this.branch = branch; }

    public double getCgpa()               { return cgpa; }
    public void setCgpa(double cgpa)      { this.cgpa = cgpa; }

    public String getEmail()              { return email; }
    public void setEmail(String email)    { this.email = email; }

    public String getPhone()              { return phone; }
    public void setPhone(String phone)    { this.phone = phone; }

    public int getAge()                   { return age; }
    public void setAge(int age)           { this.age = age; }

    public String getGender()             { return gender; }
    public void setGender(String gender)  { this.gender = gender; }

    // ── Utility ───────────────────────────────────────────────────────────────
    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', roll='" + rollNumber
                + "', course='" + course + "', cgpa=" + cgpa + "}";
    }
}
