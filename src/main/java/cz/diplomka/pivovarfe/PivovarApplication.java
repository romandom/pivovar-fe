package cz.diplomka.pivovarfe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

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

        // Pridanie softvérovej klávesnice
        attachSoftKeyboardListeners(scene);

        primaryStage.setScene(scene);
    }

    public static void switchScene(String fxmlPath, Consumer<Object> controllerInitializer) throws IOException {
        FXMLLoader loader = new FXMLLoader(PivovarApplication.class.getResource(fxmlPath));
        Scene scene = new Scene(loader.load(), 800, 480);

        if (controllerInitializer != null) {
            controllerInitializer.accept(loader.getController());
        }

        attachSoftKeyboardListeners(scene);

        primaryStage.setScene(scene);
    }

    public static void attachSoftKeyboardListeners(Scene scene) {
        scene.getRoot().lookupAll(".text-field").forEach(node -> {
            if (node instanceof TextField) {
                node.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    try {
                        if (isNowFocused) {
                            // Spustenie softvérovej klávesnice
                            try {
                                new ProcessBuilder("matchbox-keyboard").start();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            // Zatvorenie softvérovej klávesnice
                            try {
                                new ProcessBuilder("pkill", "matchbox-keyboard").start();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }


    public static void main(String[] args) {
        launch();
    }
}
