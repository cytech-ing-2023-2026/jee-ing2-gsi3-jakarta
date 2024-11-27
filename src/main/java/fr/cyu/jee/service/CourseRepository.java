package fr.cyu.jee.service;

import fr.cyu.jee.model.Course;
import jakarta.persistence.EntityManager;

public class CourseRepository extends JpaRepository<Integer, Course> {

    public CourseRepository(EntityManager em) {
        super(em, "courses", Course.class);
    }
}
