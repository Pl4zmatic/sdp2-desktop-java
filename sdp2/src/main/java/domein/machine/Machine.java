package domein.machine;

import jakarta.persistence.Tuple;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.Date;

public class Machine {

    @Getter@Setter
    private String siteNaam;
    @Getter@Setter
    private String code;
    @Getter@Setter
    private String locatie;
    @Getter@Setter
    private String productInfo;
    @Getter@Setter
    private String status;
    @Getter@Setter
    private String productieStatus;
    @Getter@Setter
    private LocalDateTime uptime;
    @Getter@Setter
    private String techniekerNaam;
    @Getter@Setter
    private Pair<LocalDateTime, String> laatsteOnderhoud;
    private int aantalDagenSindsLaatsteOnderhoud;
    @Getter@Setter
    private Date datumToekomstigeOnderhoud;


    public Machine(String siteNaam, String code, String locatie,
                   String productInfo, String status, String productieStatus,
                   LocalDateTime uptime, String techniekerNaam
                   ) {
        this.siteNaam = siteNaam;
        this.code = code;
        this.locatie = locatie;
        this.productInfo = productInfo;
        this.status = status;
        this.productieStatus = productieStatus;
        this.uptime = uptime;
        this.techniekerNaam = techniekerNaam;

    }

    public int getAantalDagenSindsLaatsteOnderhoud() {
        return (int) Duration.between(laatsteOnderhoud.getKey(), LocalDateTime.now()).toDays();
    }




}
