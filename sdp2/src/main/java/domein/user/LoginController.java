package domein.user;

import repository.UserDaoJpa;

public class LoginController {
    private final UserDaoJpa userDao;

    public LoginController() {
        this.userDao = new UserDaoJpa();
    }

    public boolean login(String email, String password)
    {
        User user = userDao.getUserByEmail(email);
        return user.checkPassword(password);
    }
}
