package main;

public class Course {
    private static int counter = 1;
    private int    id;
    private String name;
    private double fee;

    public Course(String name, double fee) {
        this.id   = counter++;
        this.name = name;
        this.fee  = fee;
    }

    public int    getId()   { return id; }
    public String getName() { return name; }
    public double getFee()  { return fee; }

    @Override public String toString() { return name + " (₹" + String.format("%.0f", fee) + ")"; }
}
