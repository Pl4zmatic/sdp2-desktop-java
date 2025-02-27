package domein.user;

import domein.Session;
import repository.UserDaoJpa;
import org.mindrot.jbcrypt.BCrypt;
import utils.Rollen;

public class UserService {
    private final UserDaoJpa userDao;

    public UserService() {
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



    public boolean register(String firstName, String lastName, String email, String password,
                            String adres, String gsmNummer, Rollen rol) {
        try {
            // Controleer of een gebruiker met dit e-mailadres al bestaat
            if (userDao.getUserByEmail(email) != null) {
                return false;
            }

            // Maak een nieuwe gebruiker aan
            User newUser = new User(firstName, lastName, email, password, adres, gsmNummer, rol);

            // Voeg de gebruiker toe aan de database
            UserDaoJpa.startTransaction();
            userDao.insert(newUser);
            UserDaoJpa.commitTransaction();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            UserDaoJpa.rollbackTransaction();
            return false;
        }
    }

}
