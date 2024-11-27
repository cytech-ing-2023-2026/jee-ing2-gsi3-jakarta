package fr.cyu.jee.dto;

import fr.cyu.jee.model.Student;
import fr.cyu.jee.model.Subject;
import fr.cyu.jee.model.Teacher;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddCourseDTO {

    @NotNull
    private LocalDateTime beginDate;

    @NotNull
    private String duration;

    @NotNull
    private Subject subject;

    @NotNull
    private Teacher teacher;

    private List<Student> students;

    public @NotNull LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(@NotNull LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public Optional<Duration> getDuration() {
        try {
            String[] hoursAndMinutes = duration.trim().split(":");
            return Optional.of(Duration.ofHours(Integer.parseInt(hoursAndMinutes[0])).plusMinutes(Integer.parseInt(hoursAndMinutes[1])));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public void setDuration(@NotNull String duration) {
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

    public List<Student> getStudents() {
        return students == null ? new ArrayList<>() : students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
