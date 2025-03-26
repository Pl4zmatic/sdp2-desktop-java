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
import service.ServiceController;
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
    @FXML private VBox managerMenu;

    @FXML private Button beheerGebruikerItem;
    @FXML private Button logItem;
    @FXML private Button beheerSiteItem;
    @FXML private Button onderhoudItem;
    @FXML private Button beheerMachineItem;
    @FXML private Button beheerNotificatieItem;
    @FXML private Button onderhoudTechniekerItem;

    @FXML private Button overviewPlantsItem;
    @FXML private Button managePlantsItem;
    @FXML private Button manageMachineItem;
    @FXML private Button manageNotificationItem;

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



    @FXML
    public void initialize() {

        NotificationPoller.getInstance().addObserver(this);
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

                if (overviewPlantsItem != null) {
                    overviewPlantsItem.setOnAction(event -> navToOverviewPlants());
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

                if (quickNavSites != null) quickNavSites.setVisible(false);
                if (quickNavSites != null) quickNavSites.setManaged(false);
                if (quickNavMachines != null) quickNavMachines.setVisible(false);
                if (quickNavMachines != null) quickNavMachines.setManaged(false);
                if (quickNavNotifications != null) quickNavNotifications.setVisible(false);
                if (quickNavNotifications != null) quickNavNotifications.setManaged(false);
            }
        }
    }

    @FXML
    private void quickNavToUsers() {
        SceneSwitcher.switchScene("/view/ManageUsers.fxml");
    }

    @FXML
    private void quickNavToLogs() {
        SceneSwitcher.switchScene("/view/UserLogs.fxml");
    }

    @FXML
    private void quickNavToSites() {
        SceneSwitcher.switchScene("/view/SitesManagement.fxml");
    }

    @FXML
    private void quickNavToMaintenance() {
        SceneSwitcher.switchScene("/view/OnderhoudScherm.fxml");
    }

    @FXML
    private void quickNavToMachines() {
        SceneSwitcher.switchScene("/view/ManageMachines.fxml");
    }

    @FXML
    private void quickNavToNotifications() {
        SceneSwitcher.switchScene("/view/NotificationsManagement.fxml");
    }

    @FXML
    private void navToViewNotifications() {
        SceneSwitcher.switchScene("/view/ShowNotifications.fxml");
    }

    @FXML
    private void navToOverviewPlants() {
        SceneSwitcher.switchScene("/view/SiteSelectionPlantOverview.fxml");
    }

    private void showLogoutMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> {
            handleLougout();
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
            if (administratorMenu != null && verantwoordelijkeMenu != null && techniekerMenu != null && managerMenu != null) {
                switch (userRole) {
                    case ADMINISTRATOR -> {
                        administratorMenu.setManaged(true);
                        administratorMenu.setVisible(true);
                        verantwoordelijkeMenu.setManaged(false);
                        verantwoordelijkeMenu.setVisible(false);
                        techniekerMenu.setManaged(false);
                        techniekerMenu.setVisible(false);
                        managerMenu.setManaged(false);
                        managerMenu.setVisible(false);

                        setActiveMenuItem(beheerGebruikerItem);
                    }
                    case VERANTWOORDELIJKE -> {
                        administratorMenu.setManaged(false);
                        administratorMenu.setVisible(false);
                        verantwoordelijkeMenu.setManaged(true);
                        verantwoordelijkeMenu.setVisible(true);
                        techniekerMenu.setManaged(false);
                        techniekerMenu.setVisible(false);
                        managerMenu.setManaged(false);
                        managerMenu.setVisible(false);

                        setActiveMenuItem(beheerMachineItem);
                    }
                    case TECHNIEKER -> {
                        administratorMenu.setManaged(false);
                        administratorMenu.setVisible(false);
                        verantwoordelijkeMenu.setManaged(false);
                        verantwoordelijkeMenu.setVisible(false);
                        techniekerMenu.setManaged(true);
                        techniekerMenu.setVisible(true);
                        managerMenu.setManaged(false);
                        managerMenu.setVisible(false);

                        setActiveMenuItem(onderhoudTechniekerItem);
                    }
                    case MANAGER -> {
                        administratorMenu.setManaged(false);
                        administratorMenu.setVisible(false);
                        verantwoordelijkeMenu.setManaged(false);
                        verantwoordelijkeMenu.setVisible(false);
                        techniekerMenu.setManaged(false);
                        techniekerMenu.setVisible(false);
                        managerMenu.setManaged(true);
                        managerMenu.setVisible(true);

                        setActiveMenuItem(overviewPlantsItem);
                    }
                }

                if (profileLastName != null && profileFirstName != null) {
                    setTextToUsername(profileLastName, currentUser.getLastName());
                    setTextToUsername(profileFirstName, currentUser.getFirstName());
                }

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
            case "Manage Users" -> "/view/ManageUsers.fxml";
            case "Logs" -> "/view/UserLogs.fxml";
            case "Manage Plants" -> "/view/ManageSites.fxml";
            case "Maintenance" -> "/view/SiteSelection2.fxml";
            case "Manage Machines" -> "/view/SiteSelection.fxml";
            case "Manage Notifications" -> "/view/ManageNotifications.fxml";
            case "Overview Plants" -> "/view/OverviewPlants.fxml";
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
                overviewPlantsItem,
                managePlantsItem,
                manageMachineItem,
                manageNotificationItem
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
            handleLougout();
        });
    }

    private void handleLougout() {
        Session.setNavbarCollapsed(false);
        Session.clear();
        SceneSwitcher.switchScene("/view/LoginPage.fxml");
        NotificationPoller.getInstance().stopPolling();
        NotificationPoller.getInstance().clearObservers();
    }

    private void setTextToUsername(Text text, String fullName) {
        if (text != null) {
            text.setText(fullName);
        }
    }

    @Override
    public void update(boolean hasNotifications) {
        System.out.println(hasNotifications + " poep");
        notificationCircle.setVisible(hasNotifications);
    }
}