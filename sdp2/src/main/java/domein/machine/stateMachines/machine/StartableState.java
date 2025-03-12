package domein.machine.stateMachines.machine;

import domein.machine.Machine;

public class StartableState extends MachineState {

	public StartableState(Machine machine) {
		super(machine);
	}

	public String startMachine() {
		machine.setCurrentState(new RunningState(machine));
		return "The machine has been started";
	}

	public String stopMachine() {
		machine.setCurrentState(new StoppedState(machine));
		return "The machine has been stopped";
	}

	@Override
	public String toString() {
		return "startable";
	}

}
