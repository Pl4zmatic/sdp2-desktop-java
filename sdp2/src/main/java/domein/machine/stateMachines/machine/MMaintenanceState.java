package domein.machine.stateMachines.machine;

import domein.machine.Machine;

public class MMaintenanceState extends MachineState {

	public MMaintenanceState(Machine machine) {
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
