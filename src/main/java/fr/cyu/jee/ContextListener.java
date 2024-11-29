package fr.cyu.jee;

import fr.cyu.jee.model.Admin;
import fr.cyu.jee.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import fr.cyu.jee.service.GradeRepository;


import java.time.LocalDate;

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
        UserRepository repository = serviceManager.registerService(ServiceKey.USER_REPOSITORY, new UserRepository(em));

        serviceManager.registerService(ServiceKey.AUTH_SERVICE, new AuthService(repository));

        sce.getServletContext().log("Populating database...");

        //TODO Add objects to DB

        serviceManager.registerService(ServiceKey.GRADE_REPOSITORY, new GradeRepository(entityManager));
        UserRepository repository2 = serviceManager.registerService(ServiceKey.USER_REPOSITORY, new UserRepository(em));


        repository.save(new Admin("notch@minecraft.net", "markus", "Markus", "Persson", LocalDate.of(1979, 6, 1)));

        sce.getServletContext().log("Server started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("Stopping server...");

        emf.close();

        sce.getServletContext().log("Server stopped");
    }
}
