package fr.cyu.jee.service;

import fr.cyu.jee.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class UserRepository extends JpaRepository<Integer, User> {

    public UserRepository(EntityManager em) {
        super(em, "users", User.class);
    }

    public Optional<User> findByEmail(String email) {
        try {
            return Optional.of(
                    getEntityManager()
                    .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmailAndPassword(String email, String password) {
        try {
            return Optional.of(
                    getEntityManager()
                            .createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
                            .setParameter("email", email)
                            .setParameter("password", password)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
