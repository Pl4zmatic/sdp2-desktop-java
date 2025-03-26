package domein;

import controller.NavbarManager;
import domein.machine.Maintenance;
import domein.machine.report.Report;
import domein.site.Site;
import domein.user.User;
import javafx.scene.control.Button;

import java.util.HashMap;
import java.util.Map;

public class Session {

    private static Session instance;
    private static User currentUser;  // Houdt de ingelogde gebruiker bij
    private static Button activeNavItem;
    private static boolean navbarCollapsed = false;
    private static final Map<String, Object> attributes = new HashMap<>();
    private static Site currentSite;
    private static Report currentReport;
    private static Maintenance currentMaintenance;

    public static Button getActiveNavItem(){
        return activeNavItem;
    }

    public static void setActiveButton(Button navItem) {
        activeNavItem = navItem;
    }

    // Private constr zodat er geen object van aangemaakt kan worden
    private Session() {}



    // Methode om de huidige gebruiker op te halen
    public static Site getCurrentSite() {
        return currentSite;
    }

    public static void setCurrentSite(Site site) {
        currentSite = site;
    }

    public static void setCurrentReport(Report report) {
        currentReport = report;
    }

    public static Report getCurrentReport() {
        return currentReport;
    }

    // Set ingelogde gebruiker als current user
    public static void setCurrentUser(User user) {
        currentUser = user;
        NavbarManager.reloadNavbar();
    }

    // Methode om de huidige user op te halen
    public static User getCurrentUser() {
        return currentUser;
    }
    // Specifieke methodes voor navbar collapsed state
    public static boolean isNavbarCollapsed() {
        return navbarCollapsed;
    }

    public static void setNavbarCollapsed(boolean collapsed) {
        navbarCollapsed = collapsed;
    }

    public static void toggleNavbarCollapsed() {
        navbarCollapsed = !navbarCollapsed;
    }

    // Generic attribute methods (kept for backward compatibility)
    public static Object getAttribute(String key) {
        return attributes.get(key);
    }

    public static void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public static void removeAttribute(String key) {
        attributes.remove(key);
    }

    public static void setCurrentMaintenance(Maintenance maintenance) {
        currentMaintenance = maintenance;
    }

    public static Maintenance getCurrentMaintenance() {
        return currentMaintenance;
    }

    // Current User op null zetten bij bv. uitloggen
    public static void clear() {
        currentUser = null;
        activeNavItem = null;

        navbarCollapsed = false;
    }
}

