package cz.diplomka.pivovarfe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PivovarApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        stage.setTitle("Brewing Application");
        switchScene("view/main-view.fxml");
        stage.show();
    }

    public static void switchScene(String fxmlPath) throws IOException {
        var loader = new FXMLLoader(PivovarApplication.class.getResource(fxmlPath));
        var scene = new Scene(loader.load(), 800, 480);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}