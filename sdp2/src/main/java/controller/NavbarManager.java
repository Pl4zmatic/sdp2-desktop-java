package controller;

import domein.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class NavbarManager {

    private static Parent navbar;
    private static boolean lastCollapsedState = false;

    public static Parent getNavbar() {
        boolean currentCollapsedState = Session.isNavbarCollapsed();

        if (navbar == null || lastCollapsedState != currentCollapsedState) {
            loadNavbar();
        }

        return navbar;
    }

    private static void loadNavbar() {
        System.out.println("DE NAVBAR IS COLLAPSED: " + Session.isNavbarCollapsed());
        try {
            boolean isCollapsed = Session.isNavbarCollapsed();
            lastCollapsedState = isCollapsed;

            String fxmlPath = isCollapsed ? "/view/CollapsedNavbar.fxml" : "/view/Navbar.fxml";

            FXMLLoader navbarLoader = new FXMLLoader(NavbarManager.class.getResource(fxmlPath));
            navbar = navbarLoader.load();

            NavbarController controller = navbarLoader.getController();

            // Only call updateNavbar if the controller is properly initialized
            try {
                controller.updateNavbar();
            } catch (Exception e) {
                System.err.println("Error updating navbar: " + e.getMessage());
                e.printStackTrace();
            }

            navbar.setUserData(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadNavbar() {
        navbar = null;
        getNavbar();
    }
}

