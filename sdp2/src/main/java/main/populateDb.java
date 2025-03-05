package main;

import domein.user.User;
import repository.UserDaoJpa;
import utils.Rollen;

public class populateDb {
    public static void main(String[] args) {
        UserDaoJpa userDaoJpa = new UserDaoJpa();
        UserDaoJpa.startTransaction();
        userDaoJpa.insert(new User("Admin", "de Admin", "admin@mail.com", "admin","Adminstraat 59", "1234561", Rollen.ADMINISTRATOR));
        UserDaoJpa.commitTransaction();
    }
}
