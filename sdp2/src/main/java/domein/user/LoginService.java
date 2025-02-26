package domein.user;

import domein.Session;
import repository.UserDaoJpa;
import org.mindrot.jbcrypt.BCrypt;

public class LoginService {
    private final UserDaoJpa userDao;

    public LoginService() {
        this.userDao = new UserDaoJpa();
    }

    public boolean login(String email, String password) {
        try {
            // Haal het gehashte wachtwoord op uit de database
            String hashedPassword = userDao.getHashedPasswordByEmail(email);

            if (hashedPassword != null && BCrypt.checkpw(password, hashedPassword)) {
                // Zet de ingelogde gebruiker in de sessie als het wachtwoord klopt
                User user = userDao.getUserByEmail(email);
                Session.setCurrentUser(user);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;  // Return false als login mislukt
    }
}
