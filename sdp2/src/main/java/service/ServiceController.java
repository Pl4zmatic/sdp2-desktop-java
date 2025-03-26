package service;

import domein.logging.LogEntry;
import domein.machine.Machine;
import domein.machine.Maintenance;
import domein.machine.report.Image;
import domein.machine.report.Report;
import domein.notification.Notification;
import domein.site.Site;
import domein.user.User;
import repository.UserDaoJpa;
import utils.NotificationType;
import utils.Rollen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceController {

    private final ImageService imageService;
    private final LogService logService;
    private final MachineService machineService;
    private final MaintenanceService maintenanceService;
    private final NotificationService notificationService;
    private final ReportService reportService;
    private final SiteService siteService;
    private final UserService userService;

    private static ServiceController instance;


    private ServiceController() {
        this.imageService = ImageService.getInstance();
        this.logService = LogService.getInstance();
        this.machineService = MachineService.getInstance();
        this.maintenanceService = MaintenanceService.getInstance();
        this.notificationService = NotificationService.getInstance();
        this.reportService = ReportService.getInstance();
        this.siteService = SiteService.getInstance();
        this.userService = UserService.getInstance();

    }

    public static ServiceController getInstance(){
        if (instance == null) {
            instance = new ServiceController();
        }
        return instance;
    }

    //<editor-fold desc="IMAGE">
    // IMAGE SERVICE

    public byte[] getImageBytesFromPath(String path) {
        return imageService.getImageBytesFromPath(path);
    }

    public void downloadImage(String destination, Image image) {
        imageService.download(destination, image);
    }

    public String getImageNameFromPath(String path) {
        return imageService.getImageNameFromPath(path);
    }
    //</editor-fold>

    //<editor-fold desc="LOG">
    // LOG SERVICE

    public List<LogEntry> getAllLogs() {
        return logService.getAllLogs();
    }

    public void logUserAction(String action, String details) {
        logService.logUserAction(action, details);
    }

    public void logUserCreation(User user) {
        logService.logUserCreation(user);
    }

    public void logUserEdit(User updatedUser) {
        logService.logUserEdit(updatedUser);
    }

    public void logUserDeletion(User user) {
        logService.logUserDeletion(user);
    }

    public void logPasswordReset(User user) {
        logService.logPasswordReset(user);
    }

    public void logMachineCreation(Machine machine) {
        logService.logMachineCreation(machine);
    }

    public void logMachineDeletion(String code) {
        logService.logMachineDelete(code);
    }

    public void logMachineEdit( Machine machineNew) {
        logService.logMachineEdit( machineNew);
    }
    //</editor-fold>

    //<editor-fold desc="Machine">
    // MACHINE


    public boolean updateMachine(Machine machine) {

        machineService.update(machine);
        logService.logMachineEdit(machine);
        return true;
    }

    public boolean deleteMachine(String code) {
        machineService.deleteMachine(code);
        logService.logMachineDelete(code);
        return true;
    }

    public List<Machine> getAllMachines() {
       return machineService.getAllMachines();
    }

    public boolean addMachine(Machine machine) {
         machineService.addMachine(machine);
         logService.logMachineCreation(machine);
         return true;
    }

    public List<Machine> getAllActiveMachines() {
        return machineService.getAllActiveMachines();
    }

    public List<String> getAllLocations() {
        return machineService.getAllLocations();
    }
    public List<Machine> getMachinesBySite(String siteNaam){
        return machineService.getMachinesBySite(siteNaam);
    }
    //</editor-fold>

    //<editor-fold desc="MAINTENANCE">
    // MAINTENANCE SERVICE

    public void planMaintenance(Machine machine, String[] startDate, LocalDate endDate, String reason, Report maintenanceReport, String remarks) {
        maintenanceService.planMaintenance(machine, startDate, endDate, reason, maintenanceReport, remarks);
        List<User> users = new ArrayList<>();
        users.add(machine.getTechnieker());

        createNotification(new Notification(NotificationType.MAINTENANCE
                , "Machine : " + machine.getCode() + " will be undergoing maintenance on "
                        + startDate, "Maintenance on one of your machines")
                , users);
    }

    public List<Maintenance> getMaintenanceForCurrentSite() {
        return maintenanceService.getMaintenanceForCurrentSite();
    }

    public Maintenance getMaintenanceById(int id) {
        return maintenanceService.getMaintenanceById(id);
    }

    public List<Maintenance> getAllMaintenances() {
        return maintenanceService.getAllMaintenance();
    }

    public void editMaintenance(Maintenance maintenance) {
        maintenanceService.editMaintenance(maintenance);
        List<User> users = new ArrayList<>();
        users.add(maintenance.getMachine().getTechnieker());
        createNotification(new Notification(NotificationType.MAINTENANCE
                        , "Maintenance on Machine : " + maintenance.getMachine().getCode() + "has changed", "Maintenance changed")
                , users);

    }

    public List<Maintenance> getMaintenanceByTechnieker(){
        return maintenanceService.getMaintenanceByTechnieker();
    }
    //</editor-fold>

    //<editor-fold desc="NOTIFICATION">
    // NOTIFICATION SERVICE
    public List<Notification> getAllNotificationsByUser(User user){
        return notificationService.getAllNotificationsByUser(user);
    }

    public boolean hasNotificationsByUser(User user){
        return notificationService.hasNotifications(user);
    }

    public void createNotification(Notification notification, List<User> users) {
        notificationService.createNotification(notification, users);
    }

    public boolean deleteUserNotification(User user, Notification n) {
        return notificationService.deleteUserNotification(user, n);
    }

    public void deleteNotification(Notification n) {
        notificationService.deleteNotification(n);
    }

    public List<Notification> getAllNotifications(){
        return notificationService.getAllNotifications();
    }

    public List<User> getAllUsersByNotification(Notification notification) {
        return notificationService.getAllUsersByNotification(notification);
    }

    public void updateNotification(Notification notification, List<User> users) {
        notificationService.updateNotification(notification, users);
    }
    //</editor-fold>

    //<editor-fold desc="REPORT">
    // REPORT SERVICE

    public void addReport(Report report) {
        reportService.addReport(report);
    }

    public void deleteReport(Report report) {
        reportService.deleteReport(report);
    }

    public void updateReport(Report report) {
        reportService.updateReport(report);
    }

    public Report getReportById(int id) {
        return reportService.getReportById(id);
    }

    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }
    //</editor-fold>

    //<editor-fold desc="SITE">
    // SITE SERVICE


    public List<Site> getAllSites() {
        return siteService.getAllSites();
    }

    public Site getSiteByVerantwoordelijke(String naam) {return siteService.getSiteByVerantwoordelijke(naam);}

    public boolean addSite(Site site) {
        return siteService.addSite(site);
    }

    public boolean updateSite(Site site) {
        return siteService.update(site);
    }

    public boolean deleteSite(int siteId) {
        return siteService.deleteSite(siteId);
    }
    //</editor-fold>

    //<editor-fold desc="USER">
    // USER SERVICE

    public boolean userLogin(String email, String password) {
        userService.login(email, password);
        return true;
    }

    public boolean userRegister(String firstName, String lastName, LocalDate birthDate, String email, String password,
                                String adres, String gsmNummer, Rollen rol) {

        User newUser = new User(firstName, lastName, birthDate,email, password, adres, gsmNummer, rol);
        userService.register(newUser);
        logService.logUserCreation(newUser);
        return true;
    }


    public boolean deleteUser(String email) {
        userService.deleteUser(email);

        return true;
    }

    public List<User> getAllActiveUsers() {
        return userService.getAllActiveUsers();
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public boolean editUser(User updatedUser){

        userService.editUser(updatedUser);
        logService.logUserEdit(updatedUser);
        return true;
    }

    public void resetPassword(User user, String password) {
        userService.resetPassword(user, password);
        logService.logPasswordReset(user);
    }

    public void stopMachine(Machine m) {
        machineService.stopMachine(m);
    }

    public void setMachineInMaintenance(Machine m) {
        machineService.setMachineInMaintenance(m);
    }

    public void setMachineInStartable(Machine m) {
        machineService.setMachineInStartable(m);
    }
}
