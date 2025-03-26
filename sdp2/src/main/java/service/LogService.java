package service;

import domein.Session;
import domein.logging.LogEntry;
import domein.machine.Machine;
import domein.user.User;
import repository.LogDaoJpa;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class LogService{
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

    public void logUserEdit(User updatedUser) {
        StringBuilder changes = new StringBuilder("User Changed: " + updatedUser.getFullName() + " (" + updatedUser.getEmail() + ")");



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
        String details = "Machine Added: " + machine.getCode() + "in Site : " + machine.getSite().getName();
        logUserAction("MACHINE_ADD", details);
    }

    public void logMachineDelete(String code) {
        String details = "Machine Deleted: " + code;
        logUserAction("MACHINE_DELETE", details);
    }


    public void logMachineEdit(Machine machineNew) {
        StringBuilder changes = new StringBuilder("Machine Changed : " + machineNew.getCode());


        logUserAction("MACHINE_EDIT", changes.toString());
    }


}