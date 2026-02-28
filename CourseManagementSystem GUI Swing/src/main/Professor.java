package main;

import java.util.ArrayList;
import java.util.List;

public class Professor {
    private String name;
    private String subject;
    private String email;
    private int    experience;
    private List<Course> teachingCourses = new ArrayList<>();

    public Professor(String name, String subject, String email, int experience) {
        this.name       = name;
        this.subject    = subject;
        this.email      = email;
        this.experience = experience;
    }

    public String getName()       { return name; }
    public String getSubject()    { return subject; }
    public String getEmail()      { return email; }
    public int    getExperience() { return experience; }
    public List<Course> getTeachingCourses() { return teachingCourses; }

    public void applyToTeach(Course c) {
        if (!teachingCourses.contains(c)) teachingCourses.add(c);
    }
}
