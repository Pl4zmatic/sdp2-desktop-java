package domein.machine;

public class MaintenanceState extends MachineState{
    
    public MaintenanceState(Machine machine) {
        super(machine);
    }

    public String startMachine() {
        machine.setCurrentState(new RunningState(machine));
        return "The machine has been started";
    }



    @Override
    public String toString() {
        return "maintenance";
    }
}
