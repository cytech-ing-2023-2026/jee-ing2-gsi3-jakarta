package fr.cyu.jee.model;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDateTime beginDate;

    @Column(nullable = false)
    private Duration duration;

    @ManyToOne(optional = false)
    private Subject subject;

    @ManyToOne(optional = false)
    private Teacher teacher;

    @ManyToMany
    @JoinTable(name="students_courses",
            joinColumns=@JoinColumn(name="course_id"),
            inverseJoinColumns=@JoinColumn(name="student_id")
    )
    private Set<Student> students;

    protected Course() {
    }

    public Course(LocalDateTime beginDate, Duration duration, Subject subject, Teacher teacher, Set<Student> students) {
        this.beginDate = beginDate;
        this.duration = duration;
        this.subject = subject;
        this.teacher = teacher;
        this.students = students;
    }

    public int getId() {
        return id;
    }

    //Used by Hibernate
    private void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndDate() {
        return beginDate.plus(duration);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
