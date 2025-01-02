package cz.diplomka.pivovarfe.controller;

import cz.diplomka.pivovarfe.constant.ViewPath;
import cz.diplomka.pivovarfe.service.RecipeClient;
import cz.diplomka.pivovarfe.util.SceneSwitcher;
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
        SceneSwitcher.switchScene(ViewPath.MAIN_VIEW);
    }

    @FXML
    private void openRecipe() throws IOException {
        switchToRecipeDetailView();
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

    private void switchToRecipeDetailView() throws IOException {
        String selectedRecipeName = recipeListView.getSelectionModel().getSelectedItem();

        if (selectedRecipeName == null) {
            showInfoAlert("Vyberte recept", "Prosím vyberte recept, ktorý chcete otvoriť.");
            return;
        }

        final Long selectedRecipeId = getRecipeIdByName(selectedRecipeName);

        if (selectedRecipeId == null) {
            showErrorAlert();
            return;
        }

        switchToRecipeDetailView(selectedRecipeId);
    }

    private void switchToRecipeDetailView(Long selectedRecipeId) throws IOException {
        SceneSwitcher.switchScene(ViewPath.RECIPE_DETAIL, controller -> {
            if (controller instanceof RecipeDetailController) {
                ((RecipeDetailController) controller).setRecipeId(selectedRecipeId);
            }
        });
    }

}
