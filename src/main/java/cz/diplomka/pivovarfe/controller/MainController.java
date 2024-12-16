package cz.diplomka.pivovarfe.controller;

import cz.diplomka.pivovarfe.PivovarApplication;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainController {
    @FXML
    private void openHardwareControl() throws IOException {
        PivovarApplication.switchScene("view/hardware.fxml");
    }

    @FXML
    private void openRecipeCreate() throws IOException {
        PivovarApplication.switchScene("view/create-view.fxml");
    }

    @FXML
    private void openRecipeList() throws IOException {
        PivovarApplication.switchScene("view/recipe-list-view.fxml");
    }
}