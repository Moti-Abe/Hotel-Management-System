package hotel;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Dashboard {

    private final VBox layout;
    private final Stage stage;
    private final String username;

    public Dashboard(Stage stage, String username) {
        this.stage = stage;
        this.username = username;

        layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #1d2671, #c33764);");

        Label welcomeLabel = new Label("WELCOME TO DERARTU HOTEL, " + username.toUpperCase());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setEffect(new DropShadow(5, Color.BLACK));

        Button guestBtn = createButton("Guest Management");
        Button roomBtn = createButton("Room Management");
        Button bookingBtn = createButton("Booking / Reservation");
        Button checkinBtn = createButton("Check-in / Check-out");

        guestBtn.setOnAction(e -> openGuestPage());
        roomBtn.setOnAction(e -> openRoomPage());
        bookingBtn.setOnAction(e -> openBookingPage());
        checkinBtn.setOnAction(e -> openCheckinPage());

        layout.getChildren().addAll(welcomeLabel, guestBtn, roomBtn, bookingBtn, checkinBtn);

        // Fade animation
        FadeTransition fade = new FadeTransition(Duration.millis(1200), layout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    public VBox getLayout() {
        return layout;
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(300);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        btn.setEffect(new DropShadow(3, Color.BLACK));
        btn.setOnMouseEntered(e -> btn.setOpacity(0.8));
        btn.setOnMouseExited(e -> btn.setOpacity(1));
        return btn;
    }

    private void openGuestPage() {
        GuestPage guestPage = new GuestPage(stage);
        stage.setScene(new Scene(guestPage.getLayout(), 800, 600));
    }

    private void openRoomPage() {
        RoomPage roomPage = new RoomPage(stage);
        stage.setScene(new Scene(roomPage.getLayout(), 800, 600));
    }

    private void openBookingPage() {
        BookingPage bookingPage = new BookingPage(stage);
        stage.setScene(new Scene(bookingPage.getLayout(), 800, 600));
    }

    private void openCheckinPage() {
        CheckinPage checkinPage = new CheckinPage(stage);
        stage.setScene(new Scene(checkinPage.getLayout(), 800, 600));
    }
}
