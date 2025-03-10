package domein.machine.stateMachines.machine;

import domein.machine.Machine;

public class StoppedState extends MachineState {

	public StoppedState(Machine machine) {
		super(machine);
	}


	public String startMachine() {
		machine.setCurrentState(new RunningState(machine));
		return "The machine has been started";
	}


	public String startMaintenanceMachine() {
		machine.setCurrentState(new MaintenanceState(machine));
		return "The machine is under maintenance";
	}

	@Override
	public String toString() {
		return "stopped";
	}
}
