package main;

import domein.machine.Machine;
import domein.site.Site;
import domein.user.User;
import repository.MachineDaoJpa;
import repository.SiteDaoJpa;
import repository.UserDaoJpa;
import utils.Rollen;

import java.time.LocalDate;
import java.util.Date;

public class populateDb {
    public static void main(String[] args) {
//        UserDaoJpa userDaoJpa = new UserDaoJpa();
//        UserDaoJpa.startTransaction();
//        userDaoJpa.insert(new User("Admin",
//                "de Admin",
//                LocalDate.of(2003, 5, 14),
//                "admin@mail.com", "admin","Adminstraat 59",
//                "1234561",
//                Rollen.ADMINISTRATOR));
//        userDaoJpa.insert(new User("Verantwoordelijke",
//                "de Verantwoordelijke",
//                LocalDate.of(1958, 3, 21),
//                "Verantwoordelijke@mail.com",
//                "Verantwoordelijke",
//                "Verantwoordelijkestraat 59",
//                "1234562",
//                Rollen.VERANTWOORDELIJKE));
//        userDaoJpa.insert(new User(
//                "Technieker",
//                "de Technieker",
//                LocalDate.of(1985, 7, 14),
//                "technieker@mail.com",
//                "Technieker",
//                "Techniekstraat 42",
//                "9876543",
//                Rollen.TECHNIEKER
//        ));
//        UserDaoJpa.commitTransaction();

        SiteDaoJpa siteDaoJpa = new SiteDaoJpa();

        SiteDaoJpa.startTransaction();
        siteDaoJpa.insert(new Site("AntwerpenA", "Antwerpen", "K. De Gieter"));
        SiteDaoJpa.commitTransaction();

        MachineDaoJpa machineDaoJpa = new MachineDaoJpa();

        MachineDaoJpa.startTransaction();

        Site site = siteDaoJpa.findByName("AntwerpenA");
        machineDaoJpa.insert(new Machine(site, "a-04", "Antwerpen"
                , "Info over het product"
                , "groen", "L.DeVlieger"));
        machineDaoJpa.insert(new Machine(site, "a-03", "Antwerpen"
                , "Info over het product"
                , "groen", "L.DeVlieger"));
        machineDaoJpa.insert(new Machine(site, "a-02", "Antwerpen"
                , "Info over het product"
                , "groen", "L.DeVlieger"));
        machineDaoJpa.insert(new Machine(site, "a-05", "Antwerpen"
                , "Info over het product"
                , "groen", "L.DeVlieger"));
        machineDaoJpa.insert(new Machine(site, "a-01", "Antwerpen"
                , "Info over het product"
                , "groen", "L.DeVlieger"));

        MachineDaoJpa.commitTransaction();
    }
}
