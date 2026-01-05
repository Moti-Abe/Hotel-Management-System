package hotel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        DatabaseSetup.initialize();

        LoginPage loginPage = new LoginPage(stage);
        Scene scene = new Scene(loginPage.getLayout(), 600, 500);
        stage.setTitle("Derartu Hotel Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
