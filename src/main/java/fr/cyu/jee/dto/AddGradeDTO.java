package fr.cyu.jee.dto;

import fr.cyu.jee.model.Subject;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Optional;

public class AddGradeDTO {

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @PositiveOrZero
    private Double grade;

    private Subject subject;

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    @PositiveOrZero
    public Double getGrade() {
        return grade;
    }

    public void setGrade(@PositiveOrZero Double grade) {
        this.grade = grade;
    }

    public Optional<Subject> getSubjectOptional() {
        return Optional.ofNullable(subject);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}