import domein.user.LoginController;
import domein.user.User;
import org.mindrot.jbcrypt.BCrypt;
import repository.GenericDaoJpa;
import repository.UserDao;
import repository.UserDaoJpa;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserDaoJpa userDao = new UserDaoJpa();
        Scanner scanner = new Scanner(System.in);
        LoginController lc = new LoginController();



        System.out.println("Voer je email in: ");
        String email = scanner.nextLine();

        System.out.println("Voer je wachtwoord in: ");
        String password = scanner.nextLine();

        boolean isAuthenticated = lc.login(email, password);

        if(isAuthenticated)
            System.out.println("Je bent ingelogd!");
        else
            System.out.println("Ongeldig e-mailadres of wachtwoord");

        scanner.close();
    }
}
