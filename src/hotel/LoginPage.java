package hotel;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginPage {

    private final VBox layout;
    private final Stage stage;
    private final UserDAO userDAO = new UserDAO(); // Connects to SQLite DB

    public LoginPage(Stage stage) {
        this.stage = stage;

        // Layout
        layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #6a11cb, #2575fc);");

        // Logo
        ImageView logo = new ImageView(new Image("https://via.placeholder.com/100x100.png?text=Logo"));
        logo.setFitWidth(100);
        logo.setFitHeight(100);

        // Title
        Label titleLabel = new Label("DERARTU HOTEL ONLINE MANAGEMENT SYSTEM");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setEffect(new DropShadow(5, Color.BLACK));
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(400);
        titleLabel.setAlignment(Pos.CENTER);

        // Username
        TextField usernameField = new TextField();
        usernameField.setPromptText("Email / Username");
        usernameField.setMaxWidth(300);
        usernameField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 8;");

        // Password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        passwordField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 8;");

        // Error message
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        errorLabel.setVisible(false);

        // Login Button
        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(300);
        loginBtn.setPrefHeight(40);
        loginBtn.setStyle(
                "-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        loginBtn.setEffect(new DropShadow(5, Color.BLACK));

        loginBtn.setOnMouseEntered(e -> loginBtn.setOpacity(0.8));
        loginBtn.setOnMouseExited(e -> loginBtn.setOpacity(1));

        // Login action with database validation
        loginBtn.setOnAction(e -> {

            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter username and password!");
                errorLabel.setVisible(true);
                return;
            }

            boolean success = userDAO.login(username, password);

            if (success) {
                errorLabel.setVisible(false);

                System.out.println("Login successful!");

                openDashboard(username); // pass username to dashboard
            } else {
                errorLabel.setText("Invalid username or password!");
                errorLabel.setVisible(true);
            }
        });

        // Add components
        layout.getChildren().addAll(
                logo,
                titleLabel,
                usernameField,
                passwordField,
                loginBtn,
                errorLabel
        );

        // Fade-in animation
        FadeTransition fade = new FadeTransition(Duration.millis(1500), layout);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        // Slide-in transition
        TranslateTransition slide = new TranslateTransition(Duration.millis(800), layout);
        slide.setFromY(-50);
        slide.setToY(0);
        slide.play();
    }

    public VBox getLayout() {
        return layout;
    }

    private void openDashboard(String username) {
        Dashboard dashboard = new Dashboard(stage, username);
        stage.setScene(new Scene(dashboard.getLayout(), 800, 600));
    }
}
