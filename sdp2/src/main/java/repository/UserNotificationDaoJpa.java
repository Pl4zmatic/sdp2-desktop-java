package repository;

import domein.notification.UserNotification;
import domein.user.User;
import jakarta.persistence.NoResultException;

import java.util.List;

public class UserNotificationDaoJpa extends GenericDaoJpa<UserNotification> implements UserNotificationDao {



        public UserNotificationDaoJpa() {
            super(UserNotification.class);
        }

        @Override
        public List<UserNotification> getUserNotificationsByUser(long userId) {
            try {
                return em.createQuery("SELECT u FROM UserNotification u WHERE u.userId = :userId", UserNotification.class)
                        .setParameter("userId", userId)
                        .getResultList();
            } catch (NoResultException ex) {
                return null;
            }
        }
}
