package repository;

import java.io.Serializable;

import domein.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UserDaoJpa extends GenericDaoJpa<User> implements UserDao{

    public UserDaoJpa() {
        super(User.class);
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }


    

    public String getHashedPasswordByEmail(String email) {
        try {
            return em.createQuery("SELECT u.password FROM User u WHERE u.email = :email", String.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
