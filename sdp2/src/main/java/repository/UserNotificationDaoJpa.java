package repository;

import domein.notification.Notification;
import domein.notification.UserNotification;
import domein.user.User;
import jakarta.persistence.NoResultException;

import java.util.List;

public class UserNotificationDaoJpa extends GenericDaoJpa<UserNotification> implements UserNotificationDao {



        public UserNotificationDaoJpa() {
            super(UserNotification.class);
        }

        @Override
        public List<UserNotification> getUserNotificationsByUser(User user) {
            try {
                return em.createQuery("SELECT u FROM UserNotification u WHERE u.user = :user", UserNotification.class)
                        .setParameter("user", user)
                        .getResultList();
            } catch (NoResultException ex) {
                return null;
            }
        }

        public UserNotification getUserNotification(User user , Notification notification){
            try {
                return em.createQuery("SELECT u FROM UserNotification u WHERE u.user = :user and u.notification = :notification", UserNotification.class)
                        .setParameter("user", user).setParameter("notification", notification)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        }

        public boolean hasNotificationsByUser(User user){
            try {
                return em.createQuery("SELECT u from UserNotification u where u.user = :user", UserNotification.class)
                        .setParameter("user", user)
                        .getSingleResult() != null;
            } catch (NoResultException e) {
                return false;
            }
        }
}
