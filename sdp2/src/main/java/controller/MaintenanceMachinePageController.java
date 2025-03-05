package controller;

import domein.machine.Machine;
import domein.machine.MachineService;
import domein.machine.Maintenance;
import javafx.fxml.FXML;
import utils.MaintenanceStatus;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class MaintenanceMachinePageController {
    
    private MachineService ms;

    @FXML
    private void initialize() {
        Scanner in = new Scanner(System.in);
        List<Machine> machines = ms.getAllMachines();

        machines.forEach(machine -> System.out.println(machine.getCode()));
        int indexMachine = in.nextInt();
        Machine machine = machines.get(indexMachine); 
        System.out.print("Provide the start date: ");
        LocalDate startDate = LocalDate.parse(in.next());
        System.out.print("Provide start time: ");
        LocalTime startTime = LocalTime.parse(in.next());
        System.out.print("Provide end time: ");
        LocalTime endTime = LocalTime.parse(in.next());
        System.out.print("Provide the name of the technician: ");
        String nameTechnician = in.next();
        System.out.print("Provide the reason: ");
        String reason = in.next();
        System.out.print("Provide sthe maintenance report: ");
        String maintenanceReport = in.next();
        System.out.print("Provide the remarks: ");
        String remarks = in.next();
        System.out.print("Provide the state: ");
        String stateString = in.next();
        MaintenanceStatus status = null;
        switch(stateString){
        	case "completed" -> { status = MaintenanceStatus.COMPLETED; }
        	case "in progress" -> { status = MaintenanceStatus.INPROGRESS; }
        	case "planned" -> { status = MaintenanceStatus.PLANNED; }
        
        }
        

       // machine.maintenanceMachine(machine, new Maintenance(machine, startDate, startTime, endTime, nameTechnician, reason, maintenanceReport, remarks, status));
    }
}
