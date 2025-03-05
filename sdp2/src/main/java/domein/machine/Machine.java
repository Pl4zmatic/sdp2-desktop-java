package domein.machine;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Tuple;
import javafx.util.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.Date;

@Entity
@Table(name = "machines")
@NoArgsConstructor

@Entity
@Table(name = "machine")
@NoArgsConstructor
@Getter
@ToString
@Setter
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primaire sleutel voor JPA

    @Column(nullable = false)
    private String siteNaam;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String locatie;

    @Column(nullable = false)
    private String productInfo;

    @Column(nullable = false)
    private String productieStatus;
    @Column(nullable = false)
    private LocalDateTime uptime;

    @Column(name = "technieker_naam")
    private String techniekerNaam;

    @Column(name = "laatste_onderhoud_datum")
    private LocalDateTime laatsteOnderhoudDatum;

    @Column(name = "laatste_onderhoud_beschrijving")
    private String laatsteOnderhoudBeschrijving;

    @Transient // Dit veld wordt niet opgeslagen in de database
    private int aantalDagenSindsLaatsteOnderhoud;

    @Column(name = "datum_toekomstige_onderhoud")
    private Date datumToekomstigeOnderhoud;



    @Column(nullable = false)
    private String currentStateString;

    @Transient
    private MachineState currentState;

    public Machine(String siteNaam, String code, String locatie,
                   String productInfo,String productieStatus,
                    String techniekerNaam
                   ) {
        this.siteNaam = siteNaam;
        this.code = code;
        this.locatie = locatie;
        this.productInfo = productInfo;
        this.productieStatus = productieStatus;
        this.uptime = null;
        this.techniekerNaam = techniekerNaam;
        this.currentState = new StoppedState(this);
        this.currentStateString = currentState.toString();
    }


    public String getCurrentState(){
        return currentState.toString();
    }

    public int getAantalDagenSindsLaatsteOnderhoud() {
        return (int) Duration.between(laatsteOnderhoudDatum, LocalDateTime.now()).toDays();
    }

		if (!currentState.toString().equals("stopped")) {
			throw new IllegalStateException("The machine needs to be stopped in order to go through a maintenance.");
		}

		currentState.startMaintenanceMachine();

        currentState.startMaintenanceMachine();

    }


}
