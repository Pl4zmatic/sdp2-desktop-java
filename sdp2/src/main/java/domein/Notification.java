package domein;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
    
    public Notification(String type, String message, String title, String status) {
        this.dateAndTime = LocalDateTime.now();
        this.type = type;
        this.message = message;
        this.title = title;
        this.status = status;
    }
}
