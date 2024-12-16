package cz.diplomka.pivovarfe.controller;

import cz.diplomka.pivovarfe.PivovarApplication;
import cz.diplomka.pivovarfe.service.RecipeClient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.Map;

public class RecipeListController {

    @FXML
    private ListView<String> recipeListView;

    private final RecipeClient recipeClient = new RecipeClient();
    private Map<Long, String> recipeMap;

    @FXML
    public void initialize() {
        loadRecipes();

        recipeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void menu() throws IOException {
        switchToMainView();
    }

    private void loadRecipes() {
        recipeClient.getAllRecipesNames(
                recipes -> {
                    this.recipeMap = recipes;
                    recipeListView.getItems().setAll(recipes.values());
                },
                this::showErrorAlert
        );
    }

    private void handleStartRecipe() {
        String selectedRecipeName = recipeListView.getSelectionModel().getSelectedItem();

        if (selectedRecipeName == null) {
            showInfoAlert("Vyberte recept", "Prosím vyberte recept, ktorý chcete spustiť.");
            return;
        }

        Long selectedRecipeId = getRecipeIdByName(selectedRecipeName);

        if (selectedRecipeId != null) {
            showInfoAlert("Spustenie receptu", "Recept " + selectedRecipeName + " bol úspešne spustený.");
        } else {
            showErrorAlert();
        }
    }

    private void handleDeleteRecipe() {
        String selectedRecipeName = recipeListView.getSelectionModel().getSelectedItem();

        if (selectedRecipeName == null) {
            showInfoAlert("Vyberte recept", "Prosím vyberte recept, ktorý chcete vymazať.");
            return;
        }

        Long selectedRecipeId = getRecipeIdByName(selectedRecipeName);

        if (selectedRecipeId == null) {
            showErrorAlert();
            return;
        }

//        recipeClient.deleteRecipe(
//                selectedRecipeId,
//                () -> {
//                    recipeListView.getItems().remove(selectedRecipeName);
//                    recipeMap.remove(selectedRecipeId);
//                    showInfoAlert("Recept vymazaný", "Recept " + selectedRecipeName + " bol úspešne vymazaný.");
//                },
//                this::showErrorAlert
//        );
    }

    private Long getRecipeIdByName(String recipeName) {
        return recipeMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(recipeName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Chyba");
        alert.setHeaderText(null);
        alert.setContentText("Niečo sa pokazilo. Skúste to znova.");
        alert.showAndWait();
    }

    private void switchToMainView() throws IOException {
        PivovarApplication.switchScene("view/main-view.fxml");
    }
}
