package main;

import java.util.ArrayList;
import java.util.List;

public class CourseManager {

    private List<Course>    courses    = new ArrayList<>();
    private List<Student>   students   = new ArrayList<>();
    private List<Professor> professors = new ArrayList<>();

    public CourseManager() {
        // Seed courses
        courses.add(new Course("Data Structures & Algorithms", 15000));
        courses.add(new Course("Object-Oriented Programming",  12000));
        courses.add(new Course("Database Management Systems",  13500));
        courses.add(new Course("Web Development Fundamentals", 11000));
        courses.add(new Course("Machine Learning Basics",      18000));
        courses.add(new Course("Computer Networks",            10500));
        courses.add(new Course("Operating Systems",            12500));
        courses.add(new Course("Software Engineering",         14000));

        // Seed students
        students.add(new Student("alice", "alice123", "Alice Johnson", "alice@gqc.edu"));
        students.add(new Student("bob",   "bob123",   "Bob Smith",     "bob@gqc.edu"));
        students.add(new Student("carol", "carol123", "Carol White",   "carol@gqc.edu"));
    }

    // ── Courses ──
    public List<Course> getCourses() { return courses; }

    public void addCourse(String name, double fee) {
        courses.add(new Course(name, fee));
    }

    public boolean removeCourse(Course c) {
        return courses.remove(c);
    }

    // ── Students ──
    public List<Student> getStudents() { return students; }

    public Student authenticate(String username, String password) {
        for (Student s : students) {
            if (s.getUsername().equals(username) && s.getPassword().equals(password))
                return s;
        }
        return null;
    }

    // ── Professors ──
    public List<Professor> getProfessors() { return professors; }

    public void addProfessor(Professor p) { professors.add(p); }
}
