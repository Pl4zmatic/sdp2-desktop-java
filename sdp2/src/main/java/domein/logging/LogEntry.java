package domein.logging;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@NoArgsConstructor
@Getter
@Setter
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp = LocalDateTime.now();
    private String action;
    private String details;

    public LogEntry(String action, String details) {
        this.action = action;
        this.details = details;
    }
}
