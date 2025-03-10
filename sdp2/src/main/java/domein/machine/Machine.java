package domein.machine;

import domein.machine.stateMachines.machine.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    public Machine(String siteNaam, String code, String locatie,
                   String productInfo,String productieStatus,
                    String techniekerNaam
                   ) {
        this.siteNaam = siteNaam;

		this.startDate = null;

        this.code = code;
        this.locatie = locatie;
        this.productInfo = productInfo;
        this.productieStatus = productieStatus;
        this.uptimeInHours = 0;
        this.techniekerNaam = techniekerNaam;
        this.currentState = new StartableState(this);
        this.currentStateString = getCurrentState();
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

	public void stopMachine() {
		this.currentState.stopMachine();
		updateCurrentState();
	}

	public void startMachine() {
		this.currentState.startMachine();
		updateCurrentState();
	}

	public void updateUptime(){
		this.uptimeInHours = getUptime();
	}
	public void updateCurrentState(){
		this.currentStateString = getCurrentState();

	}
	@PostLoad
	public void postLoad(){
		switch(currentStateString){
				case "stopped":
					this.currentState = new StoppedState(this);
					break;
				case "running":
					this.currentState = new RunningState(this);
					break;
				case "startable":
					this.currentState = new StartableState(this);
					break;
				case "maintenance":
					this.currentState = new MaintenanceState(this);
					break;
		}
	}

	@Override
	public String toString(){

		return this.currentStateString + "Machine [codenaam=" + this.code + ", siteNaam=" + siteNaam + ", locatie=" + locatie
				+ ", productInfo=" + productInfo + ", productieStatus=" + productieStatus + ", uptimeInHours=" + this.getUptime()
				+ ", techniekerNaam=" + techniekerNaam + ", laatsteOnderhoudDatum=" + laatsteOnderhoudDatum ;
	}




}
