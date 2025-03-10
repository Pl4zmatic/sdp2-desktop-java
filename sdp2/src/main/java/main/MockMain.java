package main;

import domein.machine.Machine;
import domein.machine.MachineService;
import repository.MachineDao;
import repository.MachineDaoJpa;

import java.util.List;

public class MockMain {


    public static void main(String[] args) {
        MachineDaoJpa machineDaoJpa = new MachineDaoJpa();

        List<Machine> listMachines = machineDaoJpa.getAllMachines();
        listMachines.forEach(System.out::println);


    }
}

