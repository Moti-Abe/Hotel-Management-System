package hotel;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CheckinPage {

    private VBox layout;
    private Stage stage;
    private TableView<Booking> bookingTable;

    public CheckinPage(Stage stage) {
        this.stage = stage;
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        bookingTable = new TableView<>();
        bookingTable.setPlaceholder(new Label("No bookings yet. Create one in Booking / Reservation."));
        refreshTable();

        TableColumn<Booking, Integer> idCol = new TableColumn<>("Booking ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));

        TableColumn<Booking, String> guestCol = new TableColumn<>("Guest");
        guestCol.setCellValueFactory(param -> param.getValue().getGuest().nameProperty());

        TableColumn<Booking, Integer> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(param -> param.getValue().getRoom().roomNumberProperty().asObject());

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        bookingTable.getColumns().addAll(idCol, guestCol, roomCol, statusCol);

        Button checkinBtn = new Button("Check-in");
        Button checkoutBtn = new Button("Check-out");
        Button refreshBtn = new Button("Refresh");
        Button backBtn = new Button("Back");

        checkinBtn.setOnAction(e -> updateStatus("Checked In"));
        checkoutBtn.setOnAction(e -> updateStatus("Checked Out"));
        refreshBtn.setOnAction(e -> refreshTable());
        backBtn.setOnAction(e -> backToDashboard());

        layout.getChildren().addAll(bookingTable, checkinBtn, checkoutBtn, refreshBtn, backBtn);
    }

    public VBox getLayout() {
        return layout;
    }

    private void refreshTable() {
        ObservableList<Booking> bookings = BookingDAO.getAllBookings();
        bookingTable.setItems(bookings);
    }

    private void updateStatus(String newStatus) {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a booking first!");
            return;
        }
        BookingDAO.updateBookingStatus(selected.getBookingId(), newStatus);
        refreshTable();
        showInfo("Status updated to " + newStatus + "!");
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