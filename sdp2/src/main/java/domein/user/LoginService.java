package domein.user;

import domein.Session;
//import jakarta.persistence.EntityNotFoundException;
import repository.UserDaoJpa;

public class LoginService {
    private final UserDaoJpa userDao;

    public LoginService() {
        this.userDao = new UserDaoJpa();
    }

    public boolean login(String email, String password)
    {
        try {
            User user = userDao.getUserByEmail(email);

            if (user != null && user.checkPassword(password))
                Session.setCurrentUser(user);
                return true;

        } catch (Exception e) {
            return false;
        }
    }
}
