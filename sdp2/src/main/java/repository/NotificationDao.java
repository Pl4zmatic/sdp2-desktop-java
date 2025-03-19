package repository;

import domein.notification.Notification;
import domein.notification.UserNotification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.util.List;

public interface NotificationDao extends GenericDao<Notification> {
    List<Notification> getNotificationsByIds(List<Long> id);
    Notification getNotificationById(Long id) throws NoResultException;
}
