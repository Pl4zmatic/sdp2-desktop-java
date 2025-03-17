package repository;

import domein.Notification;
import jakarta.persistence.EntityNotFoundException;

public interface NotificationDao extends GenericDao<Notification> {
    Notification getNotificationById(int id) throws EntityNotFoundException;
}
