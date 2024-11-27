package fr.cyu.jee.model;

import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Admin extends User{
    protected Admin() {
    }

    public Admin(String email, String password, String firstName, String lastName, LocalDate dob){
        super(email, password, firstName, lastName, dob);
    }
}
