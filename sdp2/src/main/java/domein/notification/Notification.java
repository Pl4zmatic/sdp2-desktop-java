package domein.notification;

import java.time.LocalDateTime;
import java.util.Set;

import domein.notification.UserNotification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.NotificationType;

@Entity
@Table(name = "notifications")
@Setter
@Getter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Transient
    private LocalDateTime dateAndTime;

    private String message;
    private String title;
    private NotificationType type;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserNotification> userNotifications;

    public Notification(NotificationType type, String message, String title) {
        this.dateAndTime = LocalDateTime.now();
        this.type = type;
        this.message = message;
        this.title = title;

    }

    @Override
    public String toString() {
        return message;
    }
}
