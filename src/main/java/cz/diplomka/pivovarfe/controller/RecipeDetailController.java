package cz.diplomka.pivovarfe.controller;

import cz.diplomka.pivovarfe.model.RecipeStep;
import cz.diplomka.pivovarfe.service.RecipeClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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

    public RecipeDetailController() {
        this.recipeClient = new RecipeClient();
    }

    public void setRecipeId(Long recipeId) {
        loadRecipeDetails(recipeId);
    }

    private void loadRecipeDetails(Long recipeId) {
        recipeClient.getRecipeDetail(recipeId,
                recipe -> {
                    System.out.println(recipe.getName());
                    createCellsOfTable();
                    recipeNameLabel.setText(recipe.getName());
                    setRecipeStepsTable(recipe.getSteps());
                },
                () -> System.out.println("Cannot load recipe with id: " + recipeId)
        );
        System.out.println("Loading details for recipe ID: " + recipeId);
    }

    private void createCellsOfTable() {
        configureIntegerColumn(stepNumberColumn, RecipeStep::getStepNumber);
        configureDoubleColumn(targetTempColumn, RecipeStep::getTargetTemperature);
        configureIntegerColumn(durationColumn, RecipeStep::getDuration);
        configureStringColumn(vesselColumn, step -> step.getVessel().toString());
        configureBooleanColumn(transferColumn, RecipeStep::isTransferStep);
    }

    private void setRecipeStepsTable(List<RecipeStep> steps) {
        final ObservableList<RecipeStep> observableRecipeSteps = FXCollections.observableArrayList(steps);
        stepsTable.setItems(observableRecipeSteps);
    }
}
