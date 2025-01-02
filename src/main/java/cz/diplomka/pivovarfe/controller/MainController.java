package cz.diplomka.pivovarfe.controller;

import cz.diplomka.pivovarfe.constant.ViewPath;
import cz.diplomka.pivovarfe.util.SceneSwitcher;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainController {
    @FXML
    private void openHardwareControl() throws IOException {
        SceneSwitcher.switchScene(ViewPath.HARDWARE);
    }

    @FXML
    private void openRecipeCreate() throws IOException {
        SceneSwitcher.switchScene(ViewPath.CREATE);
    }

    @FXML
    private void openRecipeList() throws IOException {
        SceneSwitcher.switchScene(ViewPath.RECIPE_LIST);
    }
}