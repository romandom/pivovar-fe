package cz.diplomka.pivovarfe.controller;

import cz.diplomka.pivovarfe.constant.BrewingVessel;
import cz.diplomka.pivovarfe.model.Recipe;
import cz.diplomka.pivovarfe.model.RecipeStep;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class CreateRecipeController {

    @FXML private TextField recipeNameField;
    @FXML private TextField stepNumberField;
    @FXML private TextField targetTempField;
    @FXML private TextField durationField;
    @FXML private ComboBox<BrewingVessel> vesselComboBox;
    @FXML private CheckBox transferCheckBox;
    @FXML private TableView<RecipeStep> stepsTable;
    @FXML private TableColumn<RecipeStep, Integer> stepNumberColumn;
    @FXML private TableColumn<RecipeStep, Double> targetTempColumn;
    @FXML private TableColumn<RecipeStep, Integer> durationColumn;
    @FXML private TableColumn<RecipeStep, String> vesselColumn;
    @FXML private TableColumn<RecipeStep, Boolean> transferColumn;

    private final ObservableList<RecipeStep> recipeSteps;

    public CreateRecipeController() {
        this.recipeSteps = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Initialize ComboBox with BrewingVessel values
        vesselComboBox.getItems().setAll(BrewingVessel.values());

        // Initialize Table columns
        stepNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStepNumber()).asObject());
        targetTempColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTargetTemperature()).asObject());
        durationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDuration()).asObject());
        vesselColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVessel().toString()));
        transferColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isTransferStep()).asObject());

        // Bind data to table
        stepsTable.setItems(recipeSteps);
    }

    @FXML
    private void addStep() {
        try {
            var stepNumber = Integer.parseInt(stepNumberField.getText());
            var targetTemperature = Double.parseDouble(targetTempField.getText());
            var duration = Integer.parseInt(durationField.getText());
            var vessel = vesselComboBox.getValue();
            var isTransferStep = transferCheckBox.isSelected();

            var step = new RecipeStep(stepNumber, targetTemperature, duration, vessel, isTransferStep);
            recipeSteps.add(step);
            clearInputFields();
        } catch (NumberFormatException ex) {
            var alert = new Alert(AlertType.ERROR, "Please fill out all fields with valid data.");
            alert.show();
        }
    }

    @FXML
    private void createRecipe() {
        String recipeName = recipeNameField.getText();
        if (recipeName == null || recipeName.isEmpty()) {
            var alert = new Alert(AlertType.ERROR, "Recipe name is required.");
            alert.show();
            return;
        }

        // Create the Recipe object and set steps
        var recipe = new Recipe();
        recipe.setName(recipeName);
        recipe.setSteps(recipeSteps);

        // Handle creating the recipe (e.g., save to database or show confirmation)
        System.out.println("Recipe created: " + recipe.getName());
    }

    private void clearInputFields() {
        recipeNameField.clear();
        stepNumberField.clear();
        targetTempField.clear();
        durationField.clear();
        vesselComboBox.getSelectionModel().clearSelection();
        transferCheckBox.setSelected(false);
    }
}