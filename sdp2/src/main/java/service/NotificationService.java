package service;

import java.util.List;
import java.util.stream.Collectors;

import controller.Observer;
import domein.notification.Notification;
import domein.notification.UserNotification;
import domein.user.User;
import jakarta.persistence.EntityNotFoundException;
import repository.NotificationDaoJpa;
import repository.UserNotificationDao;
import repository.UserNotificationDaoJpa;

public class NotificationService implements Subject{
    private final NotificationDaoJpa notificationDaoJpa;
    private final UserNotificationDaoJpa userNotificationDaoJpa;
    private static NotificationService instance;

    private List<Observer> observers;

    public NotificationService() {
        this.notificationDaoJpa = new NotificationDaoJpa();
        this.userNotificationDaoJpa = new UserNotificationDaoJpa();
    }

    public static NotificationService getInstance() {
        if(instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }


    public List<Notification> getAllNotificationsByUser(Long userId) {
        List<UserNotification> idList = userNotificationDaoJpa.getUserNotificationsByUser(userId);
        return notificationDaoJpa.getNotificationsByIds(idList.stream().map(UserNotification::getId).collect(Collectors.toList()));
    }

    public boolean createAndAddNotification(Notification n) {
        try {
            NotificationDaoJpa.startTransaction();
            notificationDaoJpa.insert(n);
            NotificationDaoJpa.commitTransaction();
            notifyObservers();
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

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        observers.forEach(observer -> observer.updateNotifications());
    }
}
