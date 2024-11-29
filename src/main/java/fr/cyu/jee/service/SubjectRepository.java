package fr.cyu.jee.service;

import fr.cyu.jee.model.Subject;
import jakarta.persistence.EntityManager;

public class SubjectRepository extends JpaRepository<Integer, Subject> {

    public SubjectRepository(EntityManager em) {
        super(em, "subjects", Subject.class);
    }
}
