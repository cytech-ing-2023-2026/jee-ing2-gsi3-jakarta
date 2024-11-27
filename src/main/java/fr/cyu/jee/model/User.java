package fr.cyu.jee.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dob;

    protected User() {}
    protected User(String email, String password, String firstName, String lastName, LocalDate dob) {
        this.email = email;
        setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    //Used by Hibernate
    private void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public UserType getUserType() {
        return switch (this) {
            case Admin ignored -> UserType.ADMIN;
            case Teacher ignored -> UserType.TEACHER;
            case Student ignored -> UserType.STUDENT;
            default -> throw new RuntimeException("Unknown user: " + this.getClass().getName());
        };
    }
}
