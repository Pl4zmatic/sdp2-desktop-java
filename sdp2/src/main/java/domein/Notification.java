package domein;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Notification {
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
