package domein;

import domein.user.User;
import javafx.scene.control.Button;

public class Session {
    private static User currentUser;  // Houdt de ingelogde gebruiker bij
    private static Button activeNavItem;

    public static Button getActiveNavItem(){
        return activeNavItem;
    }

    public static void setActiveButton(Button navItem) {
        activeNavItem = navItem;
    }

    // Private constr zodat er geen object van aangemaakt kan worden
    private Session() {}

    // Methode om de huidige gebruiker op te halen
    public static User getCurrentUser() {
        return currentUser;
    }

    // Set ingelogde gebruiker als current user
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Current User op null zetten bij bv. uitloggen
    public static void clear() {
        currentUser = null;
    }
}
