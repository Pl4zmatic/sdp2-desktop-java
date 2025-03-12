package service;

import domein.Session;
import domein.logging.LogEntry;
import domein.machine.Machine;
import domein.user.User;
import repository.LogDaoJpa;

import java.util.Collections;
import java.util.List;

public class LogService {
    private final LogDaoJpa logDao;
    private static LogService instance;

    private LogService() {
        this.logDao = new LogDaoJpa();
    }

    public static LogService getInstance() {
        if (instance == null) {
            instance = new LogService();
        }
        return instance;
    }

    public List<LogEntry> getAllLogs() {
        try {
            return Collections.unmodifiableList(logDao.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }




    public void logUserAction(String action, String details) {
        try {
            User currentUser = Session.getCurrentUser();
            String userInfo = currentUser != null ?
                    " door " + currentUser.getFullName() + " (" + currentUser.getEmail() + ")" :
                    " door onbekende gebruiker";

            LogEntry logEntry = new LogEntry(action, details + userInfo);

            LogDaoJpa.startTransaction();
            logDao.insert(logEntry);
            LogDaoJpa.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            LogDaoJpa.rollbackTransaction();
        }
    }

    public void logUserCreation(User user) {
        String details = "User Added: " + user.getFullName() + " (" + user.getEmail() + ") with role " + user.getRol();
        logUserAction("USER_ADD", details);
    }

    public void logUserEdit(User oldUser, User updatedUser) {
        StringBuilder changes = new StringBuilder("User Changed: " + updatedUser.getFullName() + " (" + updatedUser.getEmail() + ")");

        // Voeg details toe over wat er precies is gewijzigd
        if (!oldUser.getFirstName().equals(updatedUser.getFirstName())) {
            changes.append(", First Name: ").append(oldUser.getFirstName()).append(" → ").append(updatedUser.getFirstName());
        }
        if (!oldUser.getLastName().equals(updatedUser.getLastName())) {
            changes.append(", Last Name: ").append(oldUser.getLastName()).append(" → ").append(updatedUser.getLastName());
        }
        if (!oldUser.getEmail().equals(updatedUser.getEmail())) {
            changes.append(", Email: ").append(oldUser.getEmail()).append(" → ").append(updatedUser.getEmail());
        }
        if (!oldUser.getAdres().equals(updatedUser.getAdres())) {
            changes.append(", Adress Changed");
        }
        if ((oldUser.getGsmNummer() == null && updatedUser.getGsmNummer() != null) ||
                (oldUser.getGsmNummer() != null && !oldUser.getGsmNummer().equals(updatedUser.getGsmNummer()))) {
            changes.append(", Phone Number Changed");
        }
        if (!oldUser.getRol().equals(updatedUser.getRol())) {
            changes.append(", Role: ").append(oldUser.getRol()).append(" → ").append(updatedUser.getRol());
        }

        logUserAction("USER_EDIT", changes.toString());
    }

    public void logUserDeletion(User user) {
        String details = "User Deleted: " + user.getFullName() + " (" + user.getEmail() + ")";
        logUserAction("USER_DELETE", details);
    }

    public void logPasswordReset(User user) {
        String details = "Password Reset: " + user.getFullName() + " (" + user.getEmail() + ")";
        logUserAction("PASSWORD_RESET", details);
    }


    // Machine Logging

    public void logMachineCreation(Machine machine) {
        String details = "Machine Added: " + machine.getCode() + "in Site : " + machine.getSiteNaam();
        logUserAction("MACHINE_ADD", details);
    }

    public void logMachineDelete(Machine machine) {
        String details = "Machine Deleted: " + machine.getCode();
        logUserAction("MACHINE_DELETE", details);
    }


    public void logMachineEdit(Machine machineOld, Machine machineNew) {
        StringBuilder changes = new StringBuilder("Machine Changed : " + machineOld.getCode());
        System.out.println(machineOld.getCurrentState());
        System.out.println(machineNew.getCurrentState());
        if(!machineOld.getProductInfo().equals(machineNew.getProductInfo())) {
            changes.append(", Product Info: ").append(machineOld.getProductInfo()).append(" → ").append(machineNew.getProductInfo());
        }
        if(!machineOld.getCurrentState().equals(machineNew.getCurrentState())) {
            changes.append(", Current State: ").append(machineOld.getCurrentState()).append(" → ").append(machineNew.getCurrentState());
        }

        logUserAction("MACHINE_EDIT", changes.toString());
    }


}