package main;

import domein.machine.Machine;
import domein.user.User;
import repository.MachineDaoJpa;
import repository.UserDaoJpa;
import utils.Rollen;

public class populateDb {
    public static void main(String[] args) {
        UserDaoJpa userDaoJpa = new UserDaoJpa();
        UserDaoJpa.startTransaction();
        userDaoJpa.insert(new User("Admin", "de Admin", "admin@mail.com", "admin","Adminstraat 59", "1234561", Rollen.ADMINISTRATOR));
        userDaoJpa.insert(new User("Verantwoordelijke", "de Verantwoordelijke", "Verantwoordelijke@mail.com", "Verantwoordelijke","Verantwoordelijkestraat 59", "1234562", Rollen.VERANTWOORDELIJKE));
        UserDaoJpa.commitTransaction();

        MachineDaoJpa machineDaoJpa = new MachineDaoJpa();

        MachineDaoJpa.startTransaction();
        machineDaoJpa.insert(new Machine("AntwerpenA", "a-04", "Antwerpen"
                , "Info over het product"
                , "groen", "L.DeVlieger"));
        machineDaoJpa.insert(new Machine("AntwerpenA", "a-03", "Antwerpen"
                , "Info over het product"
                , "groen", "L.DeVlieger"));
        machineDaoJpa.insert(new Machine("AntwerpenA", "a-02", "Antwerpen"
                , "Info over het product"
                , "groen", "L.DeVlieger"));
        machineDaoJpa.insert(new Machine("AntwerpenA", "a-05", "Antwerpen"
                , "Info over het product"
                , "groen", "L.DeVlieger"));
        machineDaoJpa.insert(new Machine("AntwerpenA", "a-01", "Antwerpen"
                , "Info over het product"
                , "groen", "L.DeVlieger"));

        MachineDaoJpa.commitTransaction();
    }
}
