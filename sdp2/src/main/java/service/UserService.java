package service;

import domein.Session;
import domein.user.User;
import repository.UserDaoJpa;
import org.mindrot.jbcrypt.BCrypt;
import utils.Rollen;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class UserService {
    private final UserDaoJpa userDao;
    private final LogService logService;
    private static UserService instance;

    public UserService() {
        this.userDao = new UserDaoJpa();
        this.logService = LogService.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void logout() {
        Session.clear();
    }

    public boolean login(String email, String password) {
        try {
            // Haal het gehashte wachtwoord op uit de database
            String hashedPassword = userDao.getHashedPasswordByEmail(email);

            if (hashedPassword != null && BCrypt.checkpw(password, hashedPassword)) {
                // Zet de ingelogde gebruiker in de sessie als het wachtwoord klopt
                User user = userDao.getUserByEmail(email);
                if (!user.getDeleted()) {
                    Session.setCurrentUser(user);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Session.clear();
        return false;  // Return false als login mislukt
    }

    public boolean register(String firstName, String lastName, LocalDate birthDate, String email, String password,
                            String adres, String gsmNummer, Rollen rol) {
        try {
            // Controleer of een gebruiker met dit e-mailadres al bestaat
            if (userDao.getUserByEmail(email) != null) {
                return false;
            }

            // Maak een nieuwe gebruiker aan
            User newUser = new User(firstName, lastName, birthDate,email, password, adres, gsmNummer, rol);

            // Voeg de gebruiker toe aan de database
            UserDaoJpa.startTransaction();
            userDao.insert(newUser);
            UserDaoJpa.commitTransaction();

            // Log de actie
            logService.logUserCreation(newUser);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            UserDaoJpa.rollbackTransaction();
            return false;
        }
    }

    public boolean deleteUser(String email) {
        try {
            User user = userDao.getUserByEmail(email);

            if (user != null) {
                UserDaoJpa.startTransaction();
                userDao.softDelete(user);
                UserDaoJpa.commitTransaction();

                // Log de actie
                logService.logUserDeletion(user);

                return true;
            } else {
                System.out.println("No user found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            UserDaoJpa.rollbackTransaction();
        }
        return false;
    }

    public List<User> getAllActiveUsers() {
        try {
            return Collections.unmodifiableList(userDao.findAllActive());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<User> getAllUsers() {
        try {
            return Collections.unmodifiableList(userDao.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean editUser(User updatedUser) {
        try {
            User existingUser = userDao.getUserByEmail(updatedUser.getEmail());
            if (existingUser != null) {
                // Maak een kopie van de bestaande gebruiker voor logging
                User oldUser = new User(existingUser);
                UserDaoJpa.startTransaction();
                userDao.update(updatedUser);
                UserDaoJpa.commitTransaction();

                // Log de actie
                logService.logUserEdit(oldUser, existingUser);

                return true;
            } else {
                System.out.println("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            UserDaoJpa.rollbackTransaction();
        }
        return false;
    }

    public boolean resetPassword(User updatedUser, String newPw) {
        try {
            User existingUser = userDao.getUserByEmail(updatedUser.getEmail());
            if (existingUser != null) {
                UserDaoJpa.startTransaction();

                existingUser.setPassword(newPw);

                userDao.update(existingUser);
                UserDaoJpa.commitTransaction();

                // Log de actie
                logService.logPasswordReset(existingUser);

                return true;
            } else {
                System.out.println("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            UserDaoJpa.rollbackTransaction();
        }
        return false;
    }
}