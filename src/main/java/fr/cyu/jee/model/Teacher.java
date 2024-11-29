package fr.cyu.jee.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Teacher extends User {

    @ManyToOne()
    private Subject subject;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "teacher", cascade = CascadeType.REMOVE)
    private Set<Course> assignedCourses;

    protected Teacher() {
    }

    public Teacher(String email, String password, String firstName, String lastName, LocalDate dob, Subject subject){
        super(email, password, firstName, lastName, dob);
        this.subject = subject;
        this.assignedCourses = new HashSet<>();
    }

    public Set<Course> getAssignedCourses() {
        return assignedCourses;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getFirstName() {
        return super.getFirstName(); // Accès à la méthode héritée de User
    }

    public String getLastName() {
        return super.getLastName(); // Accès à la méthode héritée de User
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName().toUpperCase();
    }
}