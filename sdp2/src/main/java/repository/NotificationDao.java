package repository;

import domein.Notification;
import jakarta.persistence.EntityNotFoundException;

public interface NotificationDao extends GenericDao<Notification> {
    Notification getNotificationById(long id) throws EntityNotFoundException;
}
