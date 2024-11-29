package fr.cyu.jee.dto;

import fr.cyu.jee.model.Student;
import fr.cyu.jee.model.Subject;
import fr.cyu.jee.model.Teacher;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AddCourseDTO {

    @NotNull
    private LocalDateTime beginDate;

    @NotNull
    private Duration duration;

    @NotNull
    private Subject subject;

    @NotNull
    private Teacher teacher;

    private String students;

    public @NotNull LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(@NotNull LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(@NotNull Duration duration) {
        this.duration = duration;
    }

    public @NotNull Subject getSubject() {
        return subject;
    }

    public void setSubject(@NotNull Subject subject) {
        this.subject = subject;
    }

    public @NotNull Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(@NotNull Teacher teacher) {
        this.teacher = teacher;
    }

    public String getStudents() {
        return students;
    }

    public void setStudents(String students) {
        this.students = students;
    }
}
