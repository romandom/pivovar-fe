package cz.diplomka.pivovarfe.controller;

import cz.diplomka.pivovarfe.constant.ViewPath;
import cz.diplomka.pivovarfe.model.RecipeStep;
import cz.diplomka.pivovarfe.service.RecipeClient;
import cz.diplomka.pivovarfe.util.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.List;

import static cz.diplomka.pivovarfe.util.TableColumnFactory.*;

public class RecipeDetailController {

    @FXML
    private TableView<RecipeStep> stepsTable;
    @FXML
    private TableColumn<RecipeStep, Integer> stepNumberColumn;
    @FXML
    private TableColumn<RecipeStep, Double> targetTempColumn;
    @FXML
    private TableColumn<RecipeStep, Integer> durationColumn;
    @FXML
    private TableColumn<RecipeStep, String> vesselColumn;
    @FXML
    private TableColumn<RecipeStep, Boolean> transferColumn;
    @FXML
    private Label recipeNameLabel;

    private final RecipeClient recipeClient;

    private Long recipeId;

    public RecipeDetailController() {
        this.recipeClient = new RecipeClient();
    }

    public void setRecipeId(Long recipeId) {
        loadRecipeDetails(recipeId);
    }

    private void loadRecipeDetails(Long recipeId) {
        this.recipeId = recipeId;
        recipeClient.getRecipeDetail(recipeId,
                recipe -> {
                    createCellsOfTable();
                    recipeNameLabel.setText(recipe.getName());
                    setRecipeStepsTable(recipe.getSteps());
                },
                () -> System.out.println("Cannot load recipe with id: " + recipeId)
        );
        System.out.println("Loading details for recipe ID: " + recipeId);
    }

    @FXML
    public void openRecipes() throws IOException {
        SceneSwitcher.switchScene(ViewPath.RECIPE_LIST);
    }

    @FXML
    public void startRecipe() throws IOException {
        switchToRecipeStartView();
    }

    private void createCellsOfTable() {
        configureIntegerColumn(stepNumberColumn, RecipeStep::getStepNumber);
        configureDoubleColumn(targetTempColumn, RecipeStep::getTargetTemperature);
        configureIntegerColumn(durationColumn, RecipeStep::getDuration);
        configureStringColumn(vesselColumn, step -> step.getVessel().toString());
        configureBooleanColumn(transferColumn, RecipeStep::isDecoctionStep);
    }

    private void setRecipeStepsTable(List<RecipeStep> steps) {
        final ObservableList<RecipeStep> observableRecipeSteps = FXCollections.observableArrayList(steps);
        stepsTable.setItems(observableRecipeSteps);
    }

    private void switchToRecipeStartView() throws IOException {
        SceneSwitcher.switchScene(ViewPath.START, controller -> {
            if (controller instanceof StartController) {
                ((StartController) controller).setRecipeId(recipeId);
            }
        });
    }
}
