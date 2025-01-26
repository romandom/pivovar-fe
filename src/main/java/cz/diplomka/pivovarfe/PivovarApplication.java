package cz.diplomka.pivovarfe;

import cz.diplomka.pivovarfe.constant.ViewPath;
import cz.diplomka.pivovarfe.util.SceneSwitcher;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class PivovarApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        SceneSwitcher.setPrimaryStage(stage);
        stage.setTitle("Brewing Application");
        stage.setWidth(800);
        stage.setHeight(480);
        stage.setFullScreen(true);
        SceneSwitcher.switchScene(ViewPath.MAIN_VIEW);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
