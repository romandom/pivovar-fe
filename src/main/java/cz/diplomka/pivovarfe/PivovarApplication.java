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
        System.out.println(ViewPath.MAIN_VIEW.getPath());
        SceneSwitcher.switchScene(ViewPath.MAIN_VIEW);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
