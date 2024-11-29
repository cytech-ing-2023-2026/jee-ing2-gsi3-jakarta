package fr.cyu.jee.service;

import fr.cyu.jee.model.Grade;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class GradeRepository {

    private final EntityManager entityManager;

    public GradeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Grade> getAllBySubjectOrdered(int subjectId) {
        String jpql = """
                SELECT g FROM Grade g
                JOIN g.student s
                JOIN g.subject sub
                WHERE sub.id = :subjectId
                ORDER BY s.lastName ASC, s.firstName ASC, sub.name ASC
                """;
        TypedQuery<Grade> query = entityManager.createQuery(jpql, Grade.class);
        query.setParameter("subjectId", subjectId);
        return query.getResultList();
    }

    public List<Grade> getAllOrdered() {
        String jpql = """
                SELECT g FROM Grade g
                JOIN g.student s
                JOIN g.subject sub
                ORDER BY s.lastName ASC, s.firstName ASC, sub.name ASC
                """;
        TypedQuery<Grade> query = entityManager.createQuery(jpql, Grade.class);
        return query.getResultList();
    }

    public List<Grade> getAllByStudentOrdered(int studentId) {
        String jpql = """
                SELECT g FROM Grade g
                JOIN g.student s
                JOIN g.subject sub
                WHERE s.id = :studentId
                ORDER BY s.lastName ASC, s.firstName ASC, sub.name ASC
                """;
        TypedQuery<Grade> query = entityManager.createQuery(jpql, Grade.class);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }

    public List<Grade> getAllBySubjectAndStudentOrdered(int subjectId, int studentId) {
        String jpql = """
                SELECT g FROM Grade g
                JOIN g.student s
                JOIN g.subject sub
                WHERE sub.id = :subjectId AND s.id = :studentId
                ORDER BY s.lastName ASC, s.firstName ASC, sub.name ASC
                """;
        TypedQuery<Grade> query = entityManager.createQuery(jpql, Grade.class);
        query.setParameter("subjectId", subjectId);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }

    public List<Grade> getAllByOptionalFilter(Optional<Integer> subjectId, Optional<Integer> studentId) {
        if (subjectId.isPresent() && studentId.isPresent()) {
            return getAllBySubjectAndStudentOrdered(subjectId.get(), studentId.get());
        } else if (subjectId.isPresent()) {
            return getAllBySubjectOrdered(subjectId.get());
        } else if (studentId.isPresent()) {
            return getAllByStudentOrdered(studentId.get());
        } else {
            return getAllOrdered();
        }
    }

    // Méthode pour récupérer la moyenne des notes par sujet pour un étudiant
    public List<Object[]> getAverageGradesBySubjectForStudent(int studentId) {
        String sql = """
                SELECT sub.name, AVG(g.value)
                FROM Grade g
                JOIN g.subject sub
                WHERE g.student.id = :studentId
                GROUP BY sub.name
                """;
        Query query = entityManager.createQuery(sql);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }

    // Ajouter un grade (par exemple)
    public void addGrade(Grade grade) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(grade);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }
}
