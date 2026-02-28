package main;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String username;
    private String password;
    private String name;
    private String email;

    private List<Course> registeredCourses = new ArrayList<>();
    private List<Course> paidCourses       = new ArrayList<>();

    public Student(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name     = name;
        this.email    = email;
    }

    public String getUsername()  { return username; }
    public String getPassword()  { return password; }
    public String getName()      { return name; }
    public String getEmail()     { return email; }

    public List<Course> getRegisteredCourses() { return registeredCourses; }
    public List<Course> getPaidCourses()        { return paidCourses; }

    public boolean isRegistered(Course c) { return registeredCourses.contains(c); }
    public boolean hasPaid(Course c)      { return paidCourses.contains(c); }

    public void register(Course c) {
        if (!isRegistered(c)) registeredCourses.add(c);
    }

    public void pay(Course c) {
        if (isRegistered(c) && !hasPaid(c)) paidCourses.add(c);
    }
}
