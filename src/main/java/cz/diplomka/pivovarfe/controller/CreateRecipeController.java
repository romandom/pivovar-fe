package cz.diplomka.pivovarfe.controller;

import cz.diplomka.pivovarfe.constant.BrewingVessel;
import cz.diplomka.pivovarfe.constant.ViewPath;
import cz.diplomka.pivovarfe.model.Recipe;
import cz.diplomka.pivovarfe.model.RecipeStep;
import cz.diplomka.pivovarfe.service.RecipeClient;
import cz.diplomka.pivovarfe.util.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

import static cz.diplomka.pivovarfe.util.TableColumnFactory.*;
import static cz.diplomka.pivovarfe.util.TableColumnFactory.configureBooleanColumn;

public class CreateRecipeController {

    @FXML
    private TextField recipeNameField;
    @FXML
    private TextField targetTempField;
    @FXML
    private TextField durationField;
    @FXML
    private ComboBox<BrewingVessel> vesselComboBox;
    @FXML
    private CheckBox transferCheckBox;
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

    private final ObservableList<RecipeStep> recipeSteps;
    private final RecipeClient recipeClient;

    private int stepNumber;

    public CreateRecipeController() {
        this.recipeSteps = FXCollections.observableArrayList();
        this.recipeClient = new RecipeClient();
        this.stepNumber = 1;
    }

    @FXML
    public void initialize() {
        vesselComboBox.getItems().setAll(BrewingVessel.values());
        createCellsOfTable();
        stepsTable.setItems(recipeSteps);
    }

    @FXML
    private void addStep() {
        try {
            var targetTemperature = Double.parseDouble(targetTempField.getText());
            var duration = Integer.parseInt(durationField.getText());
            var vessel = vesselComboBox.getValue();
            var isTransferStep = transferCheckBox.isSelected();

            var step = new RecipeStep(stepNumber, targetTemperature, duration, vessel, isTransferStep);
            recipeSteps.add(step);

            stepNumber++;
            clearInputFields();
        } catch (NumberFormatException ex) {
            var alert = new Alert(AlertType.ERROR, "Please fill out all fields with valid data.");
            alert.show();
        }
    }

    @FXML
    private void menu() throws IOException {
        SceneSwitcher.switchScene(ViewPath.MAIN_VIEW);
    }

    @FXML
    private void createRecipe() {
        var recipeName = recipeNameField.getText();
        if (recipeName == null || recipeName.isEmpty()) {
            var alert = new Alert(AlertType.ERROR, "Recipe name is required.");
            alert.show();
            return;
        }

        var recipe = new Recipe();
        recipe.setName(recipeName);
        recipe.setSteps(recipeSteps);

        recipeClient.createRecipe(recipe,
                () -> {
                    var alert = new Alert(AlertType.INFORMATION, "Recipe created successfully!");
                    alert.show();
                    clearInputFields();
                    try {
                        SceneSwitcher.switchScene(ViewPath.MAIN_VIEW);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    var alert = new Alert(AlertType.ERROR, "Failed to create the recipe.");
                    alert.show();
                }
        );
    }

    private void clearInputFields() {
        targetTempField.clear();
        durationField.clear();
        vesselComboBox.getSelectionModel().clearSelection();
        transferCheckBox.setSelected(false);
    }

    private void createCellsOfTable() {
        configureIntegerColumn(stepNumberColumn, RecipeStep::getStepNumber);
        configureDoubleColumn(targetTempColumn, RecipeStep::getTargetTemperature);
        configureIntegerColumn(durationColumn, RecipeStep::getDuration);
        configureStringColumn(vesselColumn, step -> step.getVessel().toString());
        configureBooleanColumn(transferColumn, RecipeStep::isDecoctionStep);
    }
}