package controller;

import domein.user.User;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import repository.UserDaoJpa;

import java.util.Comparator;

public class GebruikersBeheerderController {

@FXML
private MFXTableView<User> userTable;

private ObservableList<User> users = FXCollections.observableArrayList();
private UserDaoJpa userDao = new UserDaoJpa();

// Deze methode haalt de gebruikers uit de database en vult de TableView
private void loadUsersFromDatabase() {
users.setAll(userDao.findAll());
userTable.setItems(users);  // De TableView wordt gevuld met de lijst van gebruikers
}

// De initialize-methode die wordt aangeroepen zodra de FXML is geladen
@FXML
public void initialize() {
loadUsersFromDatabase();  // Roep de methode aan om de gebruikers te laden
setupTable();  // Stel de tabelkolommen in
}

// Configureer de kolommen van de MFXTableView
private void setupTable() {
// Maak MFXTableColumn objecten voor elke kolom
MFXTableColumn<User> firstNameColumn = new MFXTableColumn<>("First Name", true, Comparator.comparing(User::getFirstName));
MFXTableColumn<User> lastNameColumn = new MFXTableColumn<>("Last Name", true, Comparator.comparing(User::getLastName));
MFXTableColumn<User> emailColumn = new MFXTableColumn<>("Email", true, Comparator.comparing(User::getEmail));
MFXTableColumn<User> addressColumn = new MFXTableColumn<>("Address", true, Comparator.comparing(User::getAdres));

// Stel voor elke kolom in welke gegevens de cellen moeten bevatten
firstNameColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getFirstName));
lastNameColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getLastName));
emailColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getEmail));
addressColumn.setRowCellFactory(user -> new MFXTableRowCell<>(User::getAdres));

// Voeg de kolommen toe aan de MFXTableView
userTable.getTableColumns().addAll(firstNameColumn, lastNameColumn, emailColumn, addressColumn);
}
}
