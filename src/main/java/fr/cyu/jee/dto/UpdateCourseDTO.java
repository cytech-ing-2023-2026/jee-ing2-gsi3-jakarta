package fr.cyu.jee.dto;

import fr.cyu.jee.model.Course;
import fr.cyu.jee.model.Subject;
import fr.cyu.jee.model.Teacher;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;

public class UpdateCourseDTO {

    @NotNull
    private Course course;

    @NotNull
    private LocalDateTime beginDate;

    @NotNull
    private Duration duration;

    @NotNull
    private Subject subject;

    @NotNull
    private Teacher teacher;

    private String students;

    public @NotNull Course getCourse() {
        return course;
    }

    public void setCourse(@NotNull Course course) {
        this.course = course;
    }

    public @NotNull LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(@NotNull LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public @NotNull Duration getDuration() {
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
