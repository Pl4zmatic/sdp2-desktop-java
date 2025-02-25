package repository;

import domein.user.User;
import jakarta.persistence.EntityNotFoundException;

public interface UserDao extends GenericDao<User> {
    User getUserByEmail(String email) throws EntityNotFoundException;
}
