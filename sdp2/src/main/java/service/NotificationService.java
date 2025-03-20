package service;

import java.util.List;
import java.util.stream.Collectors;

import utils.Observer;

import domein.notification.Notification;
import domein.notification.UserNotification;
import domein.user.User;
import jakarta.persistence.EntityNotFoundException;
import repository.NotificationDaoJpa;
import repository.UserNotificationDao;
import repository.UserNotificationDaoJpa;
import utils.Subject;

public class NotificationService{
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


    public List<Notification> getAllNotificationsByUser(User user) {
        List<UserNotification> idList = userNotificationDaoJpa.getUserNotificationsByUser(user);
        if(idList.isEmpty()){
            return null;
        }
        return notificationDaoJpa.getNotificationsByIds(idList.stream().map(UserNotification::getId).collect(Collectors.toList()));
    }

    public boolean hasNotifications(User user){
        return userNotificationDaoJpa.hasNotificationsByUser(user);
    }

    public void createNotification(Notification n, List<User> users) {


        try {
            NotificationDaoJpa.startTransaction();

            // Insert the notification
            notificationDaoJpa.insert(n);

            for (User user : users) {
                UserNotification userNotification = new UserNotification(user, n);
                userNotificationDaoJpa.insert(userNotification);
            }

            // Commit the transaction after both Notification and UserNotification are inserted
            NotificationDaoJpa.commitTransaction();
        } catch (Exception e) {
            // Rollback the transaction if any error occurs
            NotificationDaoJpa.rollbackTransaction();
            throw new RuntimeException("Error occurred while creating notification and user notifications", e);
        }
    }


    public boolean deleteUserNotification(User user, Notification n){
        UserNotification exisitingUserNotification = userNotificationDaoJpa.getUserNotification(user,n);
        if (exisitingUserNotification != null) {
            UserNotificationDaoJpa.startTransaction();
            userNotificationDaoJpa.delete(exisitingUserNotification);
            UserNotificationDaoJpa.commitTransaction();
            return true;
        } else {
            throw new EntityNotFoundException("User doesnt have this Notification");
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
