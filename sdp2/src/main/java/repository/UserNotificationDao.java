package repository;

import domein.notification.Notification;
import domein.notification.UserNotification;

import java.util.List;

public interface UserNotificationDao extends GenericDao<UserNotification> {
    List<UserNotification> getUserNotificationsByUser(long userId);
}
