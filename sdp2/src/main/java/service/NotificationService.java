package service;

import java.util.List;

import domein.Notification;
import jakarta.persistence.EntityNotFoundException;
import repository.NotificationDaoJpa;

public class NotificationService {
    private final NotificationDaoJpa notificationDaoJpa;
    private static NotificationService instance;

    public NotificationService() {
        this.notificationDaoJpa = new NotificationDaoJpa();
    }

    public static NotificationService getInstance() {
        if(instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public boolean update(Notification n) {
        Notification existingNotification = notificationDaoJpa.getNotificationById(n.getId());

        if(existingNotification != null) {
            NotificationDaoJpa.startTransaction();
            notificationDaoJpa.update(existingNotification);
            NotificationDaoJpa.commitTransaction();
            return true;
        } else {
            throw new EntityNotFoundException("Notification with id %d not found".formatted(n.getId()));
        }
    }

    public List<Notification> getAllNotifications() {
        return notificationDaoJpa.getAllNotifications();
    }

    public boolean sendNotification(Notification n) {
        try {
            NotificationDaoJpa.startTransaction();
            notificationDaoJpa.insert(n);
            NotificationDaoJpa.commitTransaction();
            return true;
        } catch (Exception e) {
            NotificationDaoJpa.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }

    public boolean deleteNotification(Notification n) {
        Notification existingNotification = notificationDaoJpa.getNotificationById(n.getId());

        if(existingNotification != null) {
            NotificationDaoJpa.startTransaction();
            notificationDaoJpa.delete(existingNotification);
            NotificationDaoJpa.commitTransaction();
            return true;
        } else {
            throw new EntityNotFoundException("Notification with id %d not found".formatted(n.getId()));
        }
    }
}
