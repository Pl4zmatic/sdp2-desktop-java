package controller;

import domein.Session;
import domein.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import service.NotificationService;
import domein.polling.NotificationPoller;
import utils.Observer;
import utils.Rollen;

import java.io.IOException;


public class NavbarController implements Observer {

    @FXML private VBox rootLayout;
    @FXML private VBox collapsedNavbar;
    @FXML private Button collapseButton;
    @FXML private Button expandButton;

    @FXML private VBox quickNavAdmin;
    @FXML private VBox quickNavManager;

    @FXML private Button quickNavUsers;
    @FXML private Button quickNavLogs;
    @FXML private Button quickNavSites;
    @FXML private Button quickNavMaintenance;
    @FXML private Button quickNavMachines;
    @FXML private Button quickNavNotifications;
    @FXML private Button quickNavProfile;

    @FXML private VBox administratorMenu;
    @FXML private VBox verantwoordelijkeMenu;
    @FXML private VBox techniekerMenu;

    @FXML private Button beheerGebruikerItem;
    @FXML private Button logItem;
    @FXML private Button beheerSiteItem;
    @FXML private Button onderhoudItem;
    @FXML private Button beheerMachineItem;
    @FXML private Button beheerNotificatieItem;
    @FXML private Button onderhoudTechniekerItem;

    @FXML private Text profileLastName;
    @FXML private Text profileFirstName;
    @FXML private Button profileIcon;

    @FXML private ContextMenu logoutMenu;
    @FXML private MenuItem logoutItem;

    @FXML private Circle notificationCircle;
    @FXML private Label amountOfNotificationsLabel;
    @FXML private Button notificationBellButton;
    @FXML private ImageView notificationBellImageView;

    private Parent expandedNavbar;
    private Parent collapsedNavbarView;

    private NotificationService notificationService;

    @FXML
    public void initialize() {
        notificationService = NotificationService.getInstance();

        if (rootLayout != null) {
            expandedNavbar = rootLayout;

            boolean isCollapsed = Session.isNavbarCollapsed();
            if (isCollapsed) {
                javafx.application.Platform.runLater(this::collapseNavbar);
            } else {
                if (collapseButton != null) {
                    collapseButton.setOnAction(event -> collapseNavbar());
                }

                updateNavbar();

                for (Button button : getAllMenuButtons()) {
                    if (button != null) {
                        button.setOnAction(event -> {
                            try {
                                handleNavigation(button);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
        }

        if (collapsedNavbar != null) {
            if (expandButton != null) {
                expandButton.setOnAction(event -> expandNavbar());
            }

            updateQuickNavVisibility();

            if (quickNavProfile != null) {
                quickNavProfile.setOnAction(event -> {
                    try {
                        showLogoutMenu();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            // Set up quick nav buttons
            if (quickNavUsers != null) {
                quickNavUsers.setOnAction(event -> quickNavToUsers());
            }
            if (quickNavLogs != null) {
                quickNavLogs.setOnAction(event -> quickNavToLogs());
            }
            if (quickNavSites != null) {
                quickNavSites.setOnAction(event -> quickNavToSites());
            }
            if (quickNavMaintenance != null) {
                quickNavMaintenance.setOnAction(event -> quickNavToMaintenance());
            }
            if (quickNavMachines != null) {
                quickNavMachines.setOnAction(event -> quickNavToMachines());
            }
            if (quickNavNotifications != null) {
                quickNavNotifications.setOnAction(event -> quickNavToNotifications());
            }

            if(notificationBellButton != null) {
                notificationBellButton.setOnAction(event -> navToViewNotifications());
            }
        }


    }

    private void updateQuickNavVisibility() {
        if (quickNavAdmin == null || quickNavManager == null) return;

        User currentUser = Session.getCurrentUser();
        if (currentUser == null) return;

        Rollen userRole = currentUser.getRol();

        switch (userRole) {
            case ADMINISTRATOR -> {
                quickNavAdmin.setVisible(true);
                quickNavAdmin.setManaged(true);
                quickNavManager.setVisible(false);
                quickNavManager.setManaged(false);
            }
            case VERANTWOORDELIJKE, MANAGER -> {
                quickNavAdmin.setVisible(false);
                quickNavAdmin.setManaged(false);
                quickNavManager.setVisible(true);
                quickNavManager.setManaged(true);
            }
            case TECHNIEKER -> {
                quickNavAdmin.setVisible(false);
                quickNavAdmin.setManaged(false);
                quickNavManager.setVisible(true);
                quickNavManager.setManaged(true);

                // Hide all buttons except maintenance for technician
                if (quickNavSites != null) quickNavSites.setVisible(false);
                if (quickNavSites != null) quickNavSites.setManaged(false);
                if (quickNavMachines != null) quickNavMachines.setVisible(false);
                if (quickNavMachines != null) quickNavMachines.setManaged(false);
                if (quickNavNotifications != null) quickNavNotifications.setVisible(false);
                if (quickNavNotifications != null) quickNavNotifications.setManaged(false);
            }
        }
    }

    // Quick navigation methods
    @FXML
    private void quickNavToUsers() {
        try {
            SceneSwitcher.switchScene("/view/ManageUsers.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void quickNavToLogs() {
        try {
            SceneSwitcher.switchScene("/view/UserLogs.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void quickNavToSites() {
        try {
            SceneSwitcher.switchScene("/view/SitesManagement.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void quickNavToMaintenance() {
        try {
            SceneSwitcher.switchScene("/view/OnderhoudScherm.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void quickNavToMachines() {
        try {
            SceneSwitcher.switchScene("/view/ManageMachines.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void quickNavToNotifications() {
        try {
            SceneSwitcher.switchScene("/view/NotificationsManagement.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navToViewNotifications() {
        try {
            SceneSwitcher.switchScene("/view/ShowNotifications.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLogoutMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> {
            try {
                handleLougout();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        menu.getItems().add(logoutItem);

        menu.show(quickNavProfile, javafx.geometry.Side.RIGHT, 0, 0);
    }

    private void collapseNavbar() {
        try {
            Session.setNavbarCollapsed(true);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CollapsedNavbar.fxml"));
            collapsedNavbarView = loader.load();

            BorderPane mainLayout = (BorderPane) rootLayout.getParent();
            if (mainLayout != null) {
                mainLayout.setLeft(collapsedNavbarView);

                mainLayout.layout();
            } else {
                System.err.println("Could not find parent BorderPane for navbar");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void expandNavbar() {
        try {
            Session.setNavbarCollapsed(false);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Navbar.fxml"));
            Parent expandedNavbarView = loader.load();

            BorderPane mainLayout = (BorderPane) collapsedNavbar.getParent();
            if (mainLayout != null) {
                mainLayout.setLeft(expandedNavbarView);

                NavbarController controller = loader.getController();
                controller.updateNavbar();

                mainLayout.layout();
            } else {
                System.err.println("Could not find parent BorderPane for navbar");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateNavbar() {
        User currentUser = Session.getCurrentUser();

        if (currentUser == null) {
            return;
        }

        Rollen userRole = currentUser.getRol();

        boolean isCollapsedView = (collapsedNavbar != null && rootLayout == null);

        if (isCollapsedView) {
            updateQuickNavVisibility();

            if (quickNavProfile != null) {
                Tooltip tooltip = new Tooltip(currentUser.getFirstName() + " " + currentUser.getLastName());
                Tooltip.install(quickNavProfile, tooltip);
            }
        } else {
            if (administratorMenu != null && verantwoordelijkeMenu != null && techniekerMenu != null) {
                switch (userRole) {
                    case ADMINISTRATOR -> {
                        administratorMenu.setManaged(true);
                        administratorMenu.setVisible(true);
                        verantwoordelijkeMenu.setManaged(false);
                        verantwoordelijkeMenu.setVisible(false);
                        techniekerMenu.setManaged(false);
                        techniekerMenu.setVisible(false);

                        setActiveMenuItem(beheerGebruikerItem);
                    }
                    case VERANTWOORDELIJKE -> {
                        administratorMenu.setManaged(false);
                        administratorMenu.setVisible(false);
                        verantwoordelijkeMenu.setManaged(true);
                        verantwoordelijkeMenu.setVisible(true);
                        techniekerMenu.setManaged(false);
                        techniekerMenu.setVisible(false);

                        setActiveMenuItem(beheerMachineItem);
                    }
                    case TECHNIEKER -> {
                        administratorMenu.setManaged(false);
                        administratorMenu.setVisible(false);
                        verantwoordelijkeMenu.setManaged(false);
                        verantwoordelijkeMenu.setVisible(false);
                        techniekerMenu.setManaged(true);
                        techniekerMenu.setVisible(true);

                        setActiveMenuItem(onderhoudTechniekerItem);
                    }
                    case MANAGER -> {
                        administratorMenu.setManaged(false);
                        administratorMenu.setVisible(false);
                        verantwoordelijkeMenu.setManaged(true);
                        verantwoordelijkeMenu.setVisible(true);
                        techniekerMenu.setManaged(false);
                        techniekerMenu.setVisible(false);

                        setActiveMenuItem(beheerMachineItem);
                    }
                }

                // Set profile text if available
                if (profileLastName != null && profileFirstName != null) {
                    setTextToUsername(profileLastName, currentUser.getLastName());
                    setTextToUsername(profileFirstName, currentUser.getFirstName());
                }

                // Setup profile menu if available
                if (profileIcon != null && logoutMenu != null && logoutItem != null) {
                    setupProfileMenu();
                }
            }
        }
    }

    private void setActiveMenuItem(Button button) {
        if (button == null) return;

        for (Button menuButton : getAllMenuButtons()) {
            if (menuButton != null) {
            }
        }

        Session.setActiveButton(button);
    }

    private void handleNavigation(Button clickedButton) throws IOException {
        setActiveMenuItem(clickedButton);

        String fxmlPath = switch (clickedButton.getText()) {
            case "Beheer Gebruikers" -> "/view/ManageUsers.fxml";
            case "Logs" -> "/view/UserLogs.fxml";
            case "Beheer Sites" -> "/view/ManageSites.fxml";
            case "Onderhoud" -> "/view/OnderhoudScherm.fxml";
            case "Beheer Machines" -> "/view/ManageMachines.fxml";
            case "Beheer Notificaties" -> "/view/ManageNotifications.fxml";
            case "Logout" -> "/view/Logout.fxml";
            default -> null;
        };

        if (fxmlPath != null) {
            SceneSwitcher.switchScene(fxmlPath);
        }
    }

    private Button[] getAllMenuButtons() {
        return new Button[]{
                beheerGebruikerItem,
                logItem,
                beheerSiteItem,
                onderhoudItem,
                beheerMachineItem,
                beheerNotificatieItem,
                onderhoudTechniekerItem,
        };
    }

    private void setupProfileMenu() {
        if (profileLastName == null || profileFirstName == null ||
                logoutMenu == null || logoutItem == null || profileIcon == null) {
            return;
        }

        setTextToUsername(profileLastName, Session.getCurrentUser().getLastName());
        setTextToUsername(profileFirstName, Session.getCurrentUser().getFirstName());

        logoutMenu.getStyleClass().add("logoutMenu");
        logoutItem.getStyleClass().add("logoutItem");

        profileIcon.setOnMouseClicked(e -> {
            if(e.getButton().equals(MouseButton.PRIMARY)) {
                logoutMenu.show(profileIcon, e.getScreenX(), e.getScreenY());
            }
        });

        logoutItem.setOnAction(e -> {
            try {
                handleLougout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void handleLougout() throws IOException {
        Session.setNavbarCollapsed(false);
        Session.clear();
        SceneSwitcher.switchScene("/view/LoginPage.fxml");
        NotificationPoller.getInstance().stopPolling();
    }

    private void setTextToUsername(Text text, String fullName) {
        if (text != null) {
            text.setText(fullName);
        }
    }






    @Override
    public void update(boolean hasNotifications) {
        System.out.println(hasNotifications);

        notificationCircle.setVisible(hasNotifications);


//            Image image = new Image(getClass().getResourceAsStream("/images/User_fill_notif_3x.png"));
//            ImageView imageView = new ImageView(image);
//            imageView.setFitHeight(40);
//            imageView.setFitWidth(40);
//            profileIcon.setGraphic(imageView);
        }
//        } else {
//            Image image = new Image(getClass().getResourceAsStream("/images/User_fill.png"));
//            profileIcon.setGraphic(new ImageView(image));
//        }

    }




