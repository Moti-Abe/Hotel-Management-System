package hotel;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class GuestPage {

    private final VBox layout;
    private final Stage stage;
    private final TableView<Guest> guestTable;

    public GuestPage(Stage stage) {
        this.stage = stage;
        layout = new VBox(15);
        layout.setPadding(new javafx.geometry.Insets(20));

        guestTable = new TableView<>();
        TableColumn<Guest, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Guest, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Guest, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        guestTable.getColumns().addAll(idCol, nameCol, phoneCol);
        refreshTable();

        Button addBtn = new Button("Add Guest");
        Button editBtn = new Button("Edit Guest");
        Button deleteBtn = new Button("Delete Guest");
        Button backBtn = new Button("Back");

        addBtn.setOnAction(e -> addGuest());
        editBtn.setOnAction(e -> editGuest());
        deleteBtn.setOnAction(e -> deleteGuest());
        backBtn.setOnAction(e -> backToDashboard());

        layout.getChildren().addAll(guestTable, addBtn, editBtn, deleteBtn, backBtn);
    }

    public VBox getLayout() {
        return layout;
    }

    private void refreshTable() {
        ObservableList<Guest> guests = GuestDAO.getAllGuests();
        guestTable.setItems(guests);
    }

    private void addGuest() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Add Guest");
        dialog.setContentText("Enter Name,Phone (e.g., John,12345):");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            String[] parts = input.split(",");
            if (parts.length == 2) {
                GuestDAO.addGuest(parts[0].trim(), parts[1].trim());
                refreshTable();
            } else showAlert("Invalid input!");
        });
    }

    private void editGuest() {
        Guest selected = guestTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a guest!"); return; }
        TextInputDialog dialog = new TextInputDialog(selected.getName() + "," + selected.getPhone());
        dialog.setHeaderText("Edit Guest");
        dialog.setContentText("Enter Name,Phone:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            String[] parts = input.split(",");
            if (parts.length == 2) {
                GuestDAO.updateGuest(selected.getId(), parts[0].trim(), parts[1].trim());
                refreshTable();
            } else showAlert("Invalid input!");
        });
    }

    private void deleteGuest() {
        Guest selected = guestTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a guest!"); return; }
        GuestDAO.deleteGuest(selected.getId());
        refreshTable();
    }

    private void backToDashboard() {
        Dashboard dashboard = new Dashboard(stage, "Admin");
        stage.setScene(new Scene(dashboard.getLayout(), 800, 600));
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
