package domein.notification;

import java.time.LocalDateTime;
import java.util.Set;

import domein.notification.UserNotification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Setter
@Getter
@NoArgsConstructor
public class Notification {
    @Id
    private long id;
    @Transient
    private LocalDateTime dateAndTime;
    private String type;
    private String message;
    private String title;
    private String status;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserNotification> userNotifications;
    public Notification(String type, String message, String title, String status) {
        this.dateAndTime = LocalDateTime.now();
        this.type = type;
        this.message = message;
        this.title = title;
        this.status = status;
    }
}
