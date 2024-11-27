package fr.cyu.jee.dto;

import fr.cyu.jee.model.Course;
import jakarta.validation.constraints.NotNull;

public class DeleteCourseDTO {

    @NotNull
    private Course course;

    public @NotNull Course getCourse() {
        return course;
    }

    public void setCourse(@NotNull Course course) {
        this.course = course;
    }
}
