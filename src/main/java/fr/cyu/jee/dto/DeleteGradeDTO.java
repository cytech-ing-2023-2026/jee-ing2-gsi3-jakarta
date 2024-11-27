package fr.cyu.jee.dto;

import fr.cyu.jee.model.Grade;
import jakarta.validation.constraints.NotNull;

public class DeleteGradeDTO {

    @NotNull
    private Grade grade;

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}