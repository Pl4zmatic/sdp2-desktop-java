package main;

import domein.machine.Machine;
import service.MachineService;

import java.util.List;

public class MockMain {


    public static void main(String[] args) {
        MachineService ms = new MachineService();

        List<Machine> listMachines = ms.getAllMachines();
        listMachines.forEach(System.out::println);

        listMachines.forEach(ms::setMachineInStartable);


    }
}

