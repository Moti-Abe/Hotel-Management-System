package hotel;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class RoomPage {

    private final VBox layout;
    private final Stage stage;
    private final TableView<Room> roomTable;

    public RoomPage(Stage stage) {
        this.stage = stage;
        layout = new VBox(15);
        layout.setPadding(new javafx.geometry.Insets(20));

        roomTable = new TableView<>();
        roomTable.setPlaceholder(new Label("No rooms added yet. Click 'Add Room' to create one."));

        TableColumn<Room, Integer> idCol = new TableColumn<>("Room No");
        idCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        TableColumn<Room, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Room, Boolean> statusCol = new TableColumn<>("Available");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("available"));

        roomTable.getColumns().addAll(idCol, typeCol, statusCol);
        refreshTable();

        Button addBtn = new Button("Add Room");
        Button deleteBtn = new Button("Delete Room"); // New for UX
        Button toggleBtn = new Button("Toggle Availability");
        Button backBtn = new Button("Back");

        addBtn.setOnAction(e -> addRoom());
        deleteBtn.setOnAction(e -> deleteRoom());
        toggleBtn.setOnAction(e -> toggleRoom());
        backBtn.setOnAction(e -> backToDashboard());

        layout.getChildren().addAll(roomTable, addBtn, deleteBtn, toggleBtn, backBtn);
    }

    public VBox getLayout() {
        return layout;
    }

    private void refreshTable() {
        ObservableList<Room> rooms = RoomDAO.getAllRooms();
        roomTable.setItems(rooms);
    }

    private void addRoom() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Room");
        dialog.setHeaderText("Add Room");
        dialog.setContentText("Enter Room Number,Type (e.g., 101,Single):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String input = result.get().trim();
            if (input.isEmpty()) {
                showAlert("Input cannot be empty!");
                return;
            }

            String[] parts = input.split(",");
            if (parts.length != 2) {
                showAlert("Invalid format! Use: RoomNumber,Type");
                return;
            }

            try {
                int roomNum = Integer.parseInt(parts[0].trim());
                String type = parts[1].trim();

                if (type.isEmpty()) {
                    showAlert("Room type cannot be empty!");
                    return;
                }

                boolean success = RoomDAO.addRoom(roomNum, type, true);

                if (success) {
                    refreshTable();
                    showInfo("Room " + roomNum + " added successfully!");
                } else {
                    showAlert("Failed to add room. Room number may already exist or invalid input.");
                }

            } catch (NumberFormatException e) {
                showAlert("Room number must be a valid integer!");
            }
        }
    }

    private void deleteRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a room to delete!");
            return;
        }
        RoomDAO.deleteRoom(selected.getRoomNumber());
        refreshTable();
        showInfo("Room deleted successfully!");
    }

    private void toggleRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a room!");
            return;
        }
        boolean newAvailable = !selected.isAvailable();
        boolean success = RoomDAO.updateRoomAvailability(selected.getRoomNumber(), newAvailable);
        if (success) {
            refreshTable();
            showInfo("Availability toggled to " + (newAvailable ? "Available" : "Unavailable"));
        } else {
            showAlert("Failed to toggle availability.");
        }
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

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}