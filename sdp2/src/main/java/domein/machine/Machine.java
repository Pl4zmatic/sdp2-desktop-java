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
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "machines")
@NoArgsConstructor
@Getter
@ToString
@Setter
public class Machine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
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
	private int uptimeInHours;

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

	// Dit is zodat de uptime kan berekent worden, elke keer wanneer de machine
	// aangaat
	// startDate = LocalDateTime.now
	// en als machine stopgezet wordt
	// startDate == null

	@Transient
	private LocalDateTime startDate;

	@Column(nullable = false)
	private String currentStateString;

	@Transient
	private MachineState currentState;

	@OneToMany(mappedBy = "machine")
	private Set<Maintenance> onderhouden;

	public Machine(String siteNaam, String code, String locatie, String productInfo, String productieStatus,
			String techniekerNaam) {
		this.siteNaam = siteNaam;
		this.code = code;
		this.locatie = locatie;
		this.productInfo = productInfo;
		this.productieStatus = productieStatus;
		this.uptimeInHours = 0;
		this.techniekerNaam = techniekerNaam;
		this.currentState = new StoppedState(this);
		this.currentStateString = currentState.toString();
		this.laatsteOnderhoudDatum = LocalDateTime.now();
		this.datumToekomstigeOnderhoud = null;
		this.onderhouden = new HashSet<Maintenance>();
	}

	public int getUptime() {
		if (startDate == null) {
			return 0;
		}
		return (int) Duration.between(startDate, LocalDateTime.now()).toHours();
	}

	public String getCurrentState() {
		return currentState.toString();
	}

	public int getAantalDagenSindsLaatsteOnderhoud() {
		return (int) Duration.between(laatsteOnderhoudDatum, LocalDateTime.now()).toDays();
	}

}
