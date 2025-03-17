package domein;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Site {
    @Column(unique = true, nullable = false)
    private String name;
    @Id
    private String siteId;

}
