package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class NavbarManager {

    private static Parent navbar;

    public static Parent getNavbar() {
        if (navbar == null) {
            loadNavbar();
        }
        return navbar;
    }

    private static void loadNavbar() {
        try {
            FXMLLoader navbarLoader = new FXMLLoader(NavbarManager.class.getResource("/view/Navbar.fxml"));
            navbar = navbarLoader.load();

            NavbarController controller = navbarLoader.getController();
            controller.updateNavbar();

            navbar.setUserData(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
