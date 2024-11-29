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
    private double value;

    private Subject subject;

    public Optional<Grade> getGrade() {
        return Optional.ofNullable(grade);
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Optional<Double> getValue() {
        return Optional.of(value);
    }

    public void setValue(double value) {
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