package fr.cyu.jee.dto;

import fr.cyu.jee.model.Grade;
import fr.cyu.jee.model.Subject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Optional;

public class UpdateGradeDTO {

    @NotNull
    private Grade grade;

    @PositiveOrZero
    @NotNull
    private Double value;

    private Subject subject;

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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