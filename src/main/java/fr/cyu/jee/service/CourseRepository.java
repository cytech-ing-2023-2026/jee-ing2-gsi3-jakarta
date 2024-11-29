package fr.cyu.jee.service;

import fr.cyu.jee.model.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class CourseRepository extends JpaRepository<Integer, Course> {

    public CourseRepository(EntityManager em) {
        super(em, "courses", Course.class);
    }

    @Transactional
    public Set<Course> getStudentCourses(int studentId, LocalDate from, LocalDate to) {
        try {
            TypedQuery<Course> query = getEntityManager().createQuery(
                    "SELECT c FROM Course c JOIN c.students s " +
                            "WHERE s.id = :studentId AND c.beginDate BETWEEN :from AND :to",
                    Course.class
            );
            query.setParameter("studentId", studentId);
            query.setParameter("from", LocalDateTime.of(from, LocalTime.MIDNIGHT));
            query.setParameter("to", LocalDateTime.of(to, LocalTime.MAX));
            return new HashSet<>(query.getResultList());
        } catch (NoResultException e) {
            return Set.of();
        }
    }

    @Transactional
    public Set<Course> getTeacherCourses(int teacherId, LocalDate from, LocalDate to) {
        try {
            TypedQuery<Course> query = getEntityManager().createQuery(
                    "SELECT c FROM Course c " +
                            "WHERE c.teacher.id = :teacherId AND c.beginDate BETWEEN :from AND :to",
                    Course.class
            );
            query.setParameter("teacherId", teacherId);
            query.setParameter("from", LocalDateTime.of(from, LocalTime.MIDNIGHT));
            query.setParameter("to", LocalDateTime.of(to, LocalTime.MAX));
            return new HashSet<>(query.getResultList());
        } catch (NoResultException e) {
            return Set.of();
        }
    }
}
