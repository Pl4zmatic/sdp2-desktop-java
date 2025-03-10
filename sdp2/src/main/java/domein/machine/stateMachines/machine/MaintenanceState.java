package domein.machine.stateMachines.machine;

import domein.machine.Machine;

public class MaintenanceState extends MachineState {

	public MaintenanceState(Machine machine) {
		super(machine);
	}

	public String setStartable() {
		machine.setCurrentState(new StartableState(machine));
		return "The machine has been put into startable";
	}

	@Override
	public String toString() {
		return "maintenance";
	}
}
