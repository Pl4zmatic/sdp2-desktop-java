package repository;

import domein.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

public class UserDaoJpa extends GenericDaoJpa<User> implements UserDao {

    public UserDaoJpa() {
        super(User.class);
    }

    @Override
    public User getUserByEmail(String email) throws EntityNotFoundException {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw new EntityNotFoundException("User with email " + email + " not found.");
        }
    }
}
