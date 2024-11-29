package fr.cyu.jee.dto;

import fr.cyu.jee.model.Grade;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public class DeleteGradeDTO {

    @NotNull
    private Grade grade;

    // Retourne un Optional<Grade> pour le grade
    public Optional<Grade> getGrade() {
        return Optional.ofNullable(grade);
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
