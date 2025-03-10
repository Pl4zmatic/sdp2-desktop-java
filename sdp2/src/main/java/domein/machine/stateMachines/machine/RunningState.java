package domein.machine.stateMachines.machine;

import domein.machine.Machine;

public class RunningState extends MachineState {

	public RunningState(Machine machine) {
		super(machine);
	}

	public String stopMachine() {
		machine.setCurrentState(new StoppedState(machine));
		return "The machine has been stopped";
	}

	@Override
	public String toString() {
		return "running";
	}
}
