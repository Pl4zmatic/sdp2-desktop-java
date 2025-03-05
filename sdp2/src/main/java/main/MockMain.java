package main;

import domein.machine.Machine;
import domein.machine.MachineService;
import repository.MachineDao;
import repository.MachineDaoJpa;

public class MockMain {


    public static void main(String[] args) {
        MachineDaoJpa machineDaoJpa = new MachineDaoJpa();

        MachineDaoJpa.startTransaction();
        try {

            machineDaoJpa.insert(new Machine("AntwerpenA", "a-02", "Antwerpen"
                    , "Info over het product"
                    , "groen", "L.DeVlieger"));
        }
        catch (Exception e) {
            MachineDaoJpa.rollbackTransaction();
        }
        // String siteNaam, String code, String locatie,
        //                   String productInfo,String productieStatus,
        //                    String techniekerNaam
        MachineDaoJpa.commitTransaction();
    }
}

