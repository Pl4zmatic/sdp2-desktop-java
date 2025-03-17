package repository;

import java.util.List;

import domein.Notification;
import jakarta.persistence.NoResultException;

public class NotificationDaoJpa extends GenericDaoJpa<Notification> implements NotificationDao {
    
    public NotificationDaoJpa() {
        super(Notification.class);
    }

    public Notification getNotificationById(int id) {
        try {
            return em.createQuery("SELECT n FROM notifications n WHERE n.id = :id", Notification.class)
                .setParameter("id", id)
                .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

    public List<Notification> getAllNotifications() {
        return em.createQuery("SELECT n FOM Notifications n", Notification.class)
            .getResultList();
    }
}
