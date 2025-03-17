package service;

import domein.Notification;
import repository.NotificationDaoJpa;

public class NotificationService {
    private final NotificationDaoJpa notificationDaoJpa;
    private static NotificationService instance;

    public NotificationService() {
        this.notificationDaoJpa = new NotificationDaoJpa();
    }

    public NotificationService getNotificationService() {
        if(instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public boolean update(Notification n) {
        
    }
}
