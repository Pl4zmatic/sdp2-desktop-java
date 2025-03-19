package repository;

import java.util.ArrayList;
import java.util.List;

import domein.notification.Notification;
import jakarta.persistence.NoResultException;

public class NotificationDaoJpa extends GenericDaoJpa<Notification> implements NotificationDao {
    
    public NotificationDaoJpa() {
        super(Notification.class);
    }

    @Override
    public List<Notification> getNotificationsByIds(List<Long> ids) {
        try {
            List<Notification> notifList = new ArrayList<>();
            ids.forEach(id -> {
                notifList.add(em.createQuery("SELECT n FROM Notification n WHERE n.id = :id", Notification.class)
                        .setParameter("id", id)
                        .getSingleResult());
            });

            return notifList;

        } catch(NoResultException nre) {
            return new ArrayList<Notification>();
        }
    }

    public Notification getNotificationById(Long id){
        try {
            return em.createQuery("SELECT u FROM Notification u WHERE u.id = :id", Notification.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Notification> getAllNotifications() {
        return em.createQuery("SELECT n FROM Notification n", Notification.class)
            .getResultList();
    }
}
