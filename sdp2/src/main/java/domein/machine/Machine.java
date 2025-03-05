package domein.machine;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Tuple;
import javafx.util.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "machines")
@NoArgsConstructor
public class Machine {

	@Getter
	@Setter
	private String siteNaam;
	@Getter
	@Setter
	private String code;
	@Getter
	@Setter
	private String locatie;
	@Getter
	@Setter
	private String productInfo;
	@Getter
	@Setter
	private String productieStatus;
	@Getter
	@Setter
	private LocalDateTime uptime;
	@Getter
	@Setter
	private String techniekerNaam;
	@Getter
	@Setter
	private Pair<LocalDateTime, String> laatsteOnderhoud;
	private int aantalDagenSindsLaatsteOnderhoud;
	@Getter
	@Setter
	private Date datumToekomstigeOnderhoud;
	@Getter
	@Setter
	private List<Machine> listMachines;
	@Getter
	@Setter(AccessLevel.PROTECTED)
	private MachineState currentState;

	public Machine(String siteNaam, String code, String locatie, String productInfo, String productieStatus,
			LocalDateTime uptime, String techniekerNaam) {
		this.siteNaam = siteNaam;
		this.code = code;
		this.locatie = locatie;
		this.productInfo = productInfo;
		this.productieStatus = productieStatus;
		this.uptime = uptime;
		this.techniekerNaam = techniekerNaam;
		this.currentState = new StoppedState(this);

	}

	public int getAantalDagenSindsLaatsteOnderhoud() {
		return (int) Duration.between(laatsteOnderhoud.getKey(), LocalDateTime.now()).toDays();
	}

	public void maintenanceMachine(Machine machine, Maintenance maintenance) {

		if (!currentState.toString().equals("stopped")) {
			throw new IllegalStateException("The machine needs to be stopped in order to go through a maintenance.");
		}

		currentState.startMaintenanceMachine();

	}


}
