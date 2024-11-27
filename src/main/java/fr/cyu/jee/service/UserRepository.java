package fr.cyu.jee.service;

import fr.cyu.jee.model.User;
import fr.cyu.jee.model.UserType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;

public class UserRepository extends JpaRepository<Integer, User> {
    public UserRepository(EntityManager em) {
        super(em, "users", User.class);
    }

    public Optional<User> findById(int id) {
        try {
            return Optional.of(
                    getEntityManager()
                            .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                            .setParameter("id", id)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
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

    public <T extends User> Optional<T> findByEmailAndType(String email, UserType type) {
        return (Optional<T>) findByEmail(email).filter(user -> user.getUserType() == type);
    }


    public List findAllByTypeOrdered(UserType type) {
        return getEntityManager()
                .createNativeQuery("SELECT * FROM users u WHERE u.dtype = :?1 ORDER BY u.id ASC", User.class)
                .setParameter(1, type.getDtype())
                .getResultList();
    };


    public List<User> findAllByOrderByIdAsc(){
        return getEntityManager()
                .createQuery("SELECT u FROM User u ORDER BY u.id ASC", User.class)
                .getResultList();
    };
}



