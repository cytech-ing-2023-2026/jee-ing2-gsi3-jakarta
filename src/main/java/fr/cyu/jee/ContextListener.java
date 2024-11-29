package fr.cyu.jee;

import fr.cyu.jee.dto.DTOUtil;
import fr.cyu.jee.dto.RegisterDTO;
import fr.cyu.jee.model.Admin;
import fr.cyu.jee.model.Student;
import fr.cyu.jee.model.Subject;
import fr.cyu.jee.model.Teacher;
import fr.cyu.jee.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import fr.cyu.jee.service.GradeRepository;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;

public class ContextListener implements ServletContextListener {

    private EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().log("Starting server...");
        sce.getServletContext().log("Registering services...");
        ServiceManager serviceManager = ServiceManager.getInstance();
        EntityManager em;

        emf = Persistence.createEntityManagerFactory("default");
        em = serviceManager.registerService(ServiceKey.ENTITY_MANAGER, emf.createEntityManager());
        em.setFlushMode(FlushModeType.AUTO);

        //TODO Register other services

        serviceManager.registerService(ServiceKey.COURSE_REPOSITORY, new CourseRepository(em));
        serviceManager.registerService(ServiceKey.GRADE_REPOSITORY, new GradeRepository(em));
        SubjectRepository subjectRepository = serviceManager.registerService(ServiceKey.SUBJECT_REPOSITORY, new SubjectRepository(em));
        UserRepository userRepository = serviceManager.registerService(ServiceKey.USER_REPOSITORY, new UserRepository(em));

        serviceManager.registerService(ServiceKey.AUTH_SERVICE, new AuthService(userRepository));

        sce.getServletContext().log("Populating database...");

        //TODO Add objects to DB
        Subject java = subjectRepository.save(new Subject("Java"));
        subjectRepository.save(new Subject("Statistiques"));
        subjectRepository.save(new Subject("Architecture réseau"));

        userRepository.save(new Admin("notch@minecraft.net", "markus", "Markus", "Persson", LocalDate.of(1979, 6, 1)));
        userRepository.save(new Teacher("notch_teacher@minecraft.net", "markus", "Markus", "Persson-Teacher", LocalDate.of(1979, 6, 1), java));
        userRepository.save(new Student("notch_student@minecraft.net", "markus", "Markus", "Persson-Student", LocalDate.of(1979, 6, 1), new HashSet<>(), new HashSet<>()));

        sce.getServletContext().log("Server started");

        sce.getServletContext().log(DTOUtil.convertToBean(Map.ofEntries(
                Map.entry("firstName", "Raf"),
                Map.entry("lastName", "From"),
                Map.entry("dob", "From"),
                Map.entry("email", "From"),
                Map.entry("password", "From"),
                Map.entry("userType", "TEACHER"),
                Map.entry("subject", 1)
        ), RegisterDTO.class).toString());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("Stopping server...");

        emf.close();

        sce.getServletContext().log("Server stopped");
    }
}
