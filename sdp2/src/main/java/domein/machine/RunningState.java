package domein.machine;

public class RunningState extends MachineState{
    
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
