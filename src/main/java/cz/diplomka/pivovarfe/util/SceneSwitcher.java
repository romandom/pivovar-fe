package cz.diplomka.pivovarfe.util;

import cz.diplomka.pivovarfe.constant.ViewPath;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class SceneSwitcher {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlPath) throws IOException {
        switchScene(fxmlPath, null);
    }

    public static void switchScene(String fxmlPath, Consumer<Object> controllerInitializer) throws IOException {
        if (primaryStage == null) {
            throw new IllegalStateException("Primary stage is not set. Call setPrimaryStage first.");
        }

        boolean isFullScreen = primaryStage.isFullScreen();

        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load(), 800, 480);

        if (controllerInitializer != null) {
            controllerInitializer.accept(loader.getController());
        }

        primaryStage.setScene(scene);

        primaryStage.setFullScreen(isFullScreen);
    }


    public static void switchScene(ViewPath viewPath) throws IOException {
        switchScene(viewPath.getPath());
    }

    public static void switchScene(ViewPath viewPath, Consumer<Object> controllerInitializer) throws IOException {
        switchScene(viewPath.getPath(), controllerInitializer);
    }
}

