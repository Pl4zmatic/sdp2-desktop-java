package service;

import domein.Session;
import domein.machine.Maintenance;
import domein.notification.Notification;
import domein.user.User;

import java.util.List;

public class ServiceController {





    private LogService logService;
    private MachineService machineService;
    private MaintenanceService maintenanceService;
    private NotificationService notificationService;
    private static UserService userService;


    private static ServiceController instance;


    public ServiceController() {
        this.notificationService = NotificationService.getInstance();

    }

    public static ServiceController getInstance(){
        if (instance == null) {
            instance = new ServiceController();
        }
        return instance;
    }



    public List<Notification> getAllNotificationsByUser(User user){
        return notificationService.getAllNotificationsByUser(user);
    }

    public boolean hasNotificationsByUser(User user){
        return notificationService.hasNotifications(user);
    }





}
