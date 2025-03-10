package domein.machine;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class MachineState {

	protected final Machine machine;

	public String startMaintenanceMachine() {
		return "The machine is under maintenance";
	}

	public String startMachine() {
		return "The machine has been started";
	}

	public String stopMachine() {
		return "The machine has been stopped";
	}

	public String setStartable() {
		return "The machine has been put into startable";
	}
}
