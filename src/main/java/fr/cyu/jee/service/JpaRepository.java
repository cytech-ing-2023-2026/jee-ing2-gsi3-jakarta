package fr.cyu.jee.service;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaRepository<ID, T> {

    private EntityManager em;
    private String tableName;
    private Class<T> entityClass;

    public JpaRepository(EntityManager em, String tableName, Class<T> entityClass) {
        this.em = em;
        this.tableName = tableName;
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public String getTableName() {
        return tableName;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return em.createNativeQuery("SELECT * FROM ?1", entityClass)
                .setParameter(1, tableName)
                .getResultList();
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    public T save(T entity) {
        em.getTransaction().begin();
        T result = em.merge(entity);
        em.getTransaction().commit();
        return result;
    }
}
