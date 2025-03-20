package repository;


import domein.notification.UserNotification;
import domein.user.User;

import java.util.List;

public interface UserNotificationDao extends GenericDao<UserNotification> {
    List<UserNotification> getUserNotificationsByUser(User user);
    boolean hasNotificationsByUser(User user);
}
