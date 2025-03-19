package domein.notification;

import domein.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_notifications")
@Setter
@Getter
@NoArgsConstructor
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;


    private boolean isRead;

    public UserNotification(User user, Notification notification) {
        this.user = user;
        this.notification = notification;
        this.isRead = false;
    }
}