package fr.cyu.jee.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.Set;

@Entity
public class Student extends User {

    @OneToMany(mappedBy = "student", cascade = {CascadeType.REMOVE, CascadeType.MERGE}, orphanRemoval = true)
    private Set<Grade> grades;

    @ManyToMany(mappedBy = "students")
    private Set<Course> courses;

    protected Student() {}

    public Student(String email, String password, String firstName, String lastName, LocalDate dob, Set<Grade> grades, Set<Course> courses){
        super(email, password, firstName, lastName, dob);
        this.grades = grades;
        this.courses = courses;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    public void addGrade(Subject subject, double value) {
        grades.add(new Grade(subject, this, value));
    }

    public Set<Course> getCourses() {
        return courses;
    }
    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
    public String getFirstName() {
        return super.getFirstName();
    }
    public String getLastName() {
        return super.getLastName();
    }
    public String getFullName() {
        return getFirstName() + " " + getLastName().toUpperCase();
    }
}
