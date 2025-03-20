package main;

import domein.machine.Machine;
import domein.notification.Notification;
import domein.user.User;
import repository.MachineDaoJpa;
import repository.NotificationDao;
import repository.NotificationDaoJpa;
import repository.UserDaoJpa;
import service.NotificationService;
import service.UserService;
import utils.Rollen;
import utils.NotificationType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class populateDb {
    public static void main(String[] args) {
//        UserDaoJpa userDaoJpa = new UserDaoJpa();
//        UserDaoJpa.startTransaction();
//        userDaoJpa.insert(new User("Admin",
//                "de Admin",
//                LocalDate.of(2003, 5, 14),
//                "admin@mail.com", "admin","Adminstraat 59",
//                "1234561",
//                Rollen.ADMINISTRATOR));
//        userDaoJpa.insert(new User("Verantwoordelijke",
//                "de Verantwoordelijke",
//                LocalDate.of(1958, 3, 21),
//                "Verantwoordelijke@mail.com",
//                "Verantwoordelijke",
//                "Verantwoordelijkestraat 59",
//                "1234562",
//                Rollen.VERANTWOORDELIJKE));
//        userDaoJpa.insert(new User(
//                "Technieker",
//                "de Technieker",
//                LocalDate.of(1985, 7, 14),
//                "technieker@mail.com",
//                "Technieker",
//                "Techniekstraat 42",
//                "9876543",
//                Rollen.TECHNIEKER
//        ));
//        UserDaoJpa.commitTransaction();
        NotificationService ns = new NotificationService();
        List<User> userList = UserService.getInstance().getAllActiveUsers();

        ns.createNotification(new Notification(NotificationType.MAINTENANCE, "Test message", "Test title"), userList );
    }
}
