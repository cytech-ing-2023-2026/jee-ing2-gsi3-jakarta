package fr.cyu.jee.service;

import jakarta.persistence.EntityManager;

public class ServiceKey<T> {

    private String name;

    public ServiceKey(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ServiceKey<AuthService> AUTH_SERVICE = new ServiceKey<>("authService");
    public static ServiceKey<EntityManager> ENTITY_MANAGER = new ServiceKey<>("entityManager");
    public static ServiceKey<CourseRepository> COURSE_REPOSITORY = new ServiceKey<>("courseRepository");
    public static ServiceKey<UserRepository> USER_REPOSITORY = new ServiceKey<>("userRepository");
    public static ServiceKey<UserRepository> GRADE_REPOSITORY = new ServiceKey<>("gradeRepository");
    public static ServiceKey<AuthService> SUBJECT_REPOSITORY = new ServiceKey<>("SubjectRepository");

}
