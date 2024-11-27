package fr.cyu.jee.model;

import java.time.LocalDate;
import java.util.HashSet;

public enum UserType {
    ADMIN("Admin"), TEACHER("Teacher"), STUDENT("Student");

    private String dtype;

    UserType(String dtype) {
        this.dtype = dtype;
    }

    public String getDtype() {
        return dtype;
    }

    public User createUser(String email, String password, String firstName, String lastName, LocalDate dob, Subject subject) {
        return switch (this) {
            case ADMIN -> new Admin(email, password, firstName, lastName, dob);
            case TEACHER -> new Teacher(email, password, firstName, lastName, dob, subject);
            case STUDENT -> new Student(email, password, firstName, lastName, dob, new HashSet<>(), new HashSet<>());
        };
    }
}
