package hotel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class BookingPage {

    private final VBox layout;
    private final Stage stage;
    private ComboBox<Guest> guestBox;
    private ComboBox<Room> roomBox;

    public BookingPage(Stage stage) {
        this.stage = stage;
        layout = new VBox(15);
        layout.setPadding(new Insets(20));

        // Guest Selection
        guestBox = new ComboBox<>();
        guestBox.setPromptText("Select a guest (add in Guest Management if none)");
        refreshGuests();

        // Room Selection
        roomBox = new ComboBox<>();
        roomBox.setPromptText("Select an available room (add in Room Management if none)");
        refreshAvailableRooms();

        // Date Pickers
        DatePicker checkinPicker = new DatePicker(LocalDate.now());
        DatePicker checkoutPicker = new DatePicker(LocalDate.now().plusDays(1));

        // Reserve Button
        Button reserveBtn = new Button("Reserve");
        reserveBtn.setOnAction(e -> {
            Guest guest = guestBox.getValue();
            Room room = roomBox.getValue();
            LocalDate checkin = checkinPicker.getValue();
            LocalDate checkout = checkoutPicker.getValue();

            if (guest == null || room == null || checkin == null || checkout == null || checkin.isAfter(checkout)) {
                showAlert("Fill all fields correctly! Check-in must be before check-out.");
                return;
            }

            BookingDAO bookingDAO = new BookingDAO();
            boolean success = bookingDAO.addBooking(guest.getId(), room.getRoomNumber(), checkin, checkout, "Reserved");

            if (success) {
                showInfo("Booking reserved successfully!");
                guestBox.getSelectionModel().clearSelection();
                roomBox.getSelectionModel().clearSelection();
                checkinPicker.setValue(LocalDate.now());
                checkoutPicker.setValue(LocalDate.now().plusDays(1));
                refreshAvailableRooms(); // Update available rooms
            } else {
                showAlert("Booking failed. Room may be unavailable or invalid data.");
            }
        });

        // Back Button
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> backToDashboard());

        layout.getChildren().addAll(
                new Label("Select Guest:"), guestBox,
                new Label("Select Room:"), roomBox,
                new Label("Check-in:"), checkinPicker,
                new Label("Check-out:"), checkoutPicker,
                reserveBtn, backBtn
        );
    }

    public VBox getLayout() {
        return layout;
    }

    private void refreshGuests() {
        GuestDAO guestDAO = new GuestDAO();
        List<Guest> guestList = guestDAO.getAllGuests();
        guestBox.setItems(FXCollections.observableArrayList(guestList));
    }

    private void refreshAvailableRooms() {
        RoomDAO roomDAO = new RoomDAO();
        List<Room> roomList = roomDAO.getAvailableRooms();
        roomBox.setItems(FXCollections.observableArrayList(roomList));
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