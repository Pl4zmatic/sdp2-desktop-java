package repository;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class GenericDaoJpa<T> implements GenericDao<T> {
    private static final String PU_NAME = "shopfloor";
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME);
    protected static final EntityManager em = emf.createEntityManager();
    private final Class<T> type;

    public GenericDaoJpa(Class<T> type) {
        this.type = type;
    }

    public static void closePersistency() {
        em.close();
        emf.close();
    }

    public static void startTransaction() {
        em.getTransaction().begin();
    }

    public static void commitTransaction() {
        em.getTransaction().commit();
    }

    public static void rollbackTransaction() {
        em.getTransaction().rollback();
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("SELECT entity FROM " + type.getSimpleName() + " entity", type).getResultList();
    }

    @Override
    public <U> T get(U id) {
        return em.find(type, id);
    }

    @Override
    public T update(T object) {
        return em.merge(object);
    }

    @Override
    public void delete(T object) {
        em.remove(em.merge(object));
    }

    @Override
    public void insert(T object) {
        em.persist(object);
    }

    @Override
    public <U> boolean exists(U id) {
        return em.find(type, id) != null;
    }
}
