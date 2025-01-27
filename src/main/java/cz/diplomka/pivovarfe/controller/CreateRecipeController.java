package cz.diplomka.pivovarfe.controller;

import cz.diplomka.pivovarfe.constant.BrewingProcess;
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
    private TableColumn<RecipeStep, String> processColumn;
    @FXML
    private Slider temperatureSlider;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Button temperaturePlusButton;
    @FXML
    private Button temperatureMinusButton;
    @FXML
    private Slider durationSlider;
    @FXML
    private Label durationLabel;
    @FXML
    private Button durationPlusButton;
    @FXML
    private Button durationMinusButton;
    @FXML
    private ToggleGroup kettleGroup;
    @FXML
    private ToggleButton rmutovaciaButton;
    @FXML
    private ToggleButton vystieraciaButton;
    @FXML
    private ToggleGroup processGroup;
    @FXML
    private ToggleButton rmutovaciButton;
    @FXML
    private ToggleButton chmelovarButton;
    @FXML
    private ToggleGroup decoctionGroup;
    @FXML
    private ToggleButton anoDecoctionButton;
    @FXML
    private ToggleButton nieDecoctionButton;

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
        createCellsOfTable();

        setupSliderAndLabel(temperatureSlider, temperatureLabel, 20.0, 100.0, 20.0);
        setupButtons(temperatureSlider, temperaturePlusButton, temperatureMinusButton, 1.0);

        setupSliderAndLabel(durationSlider, durationLabel, 0.0, 120.0, 0.0);
        setupButtons(durationSlider, durationPlusButton, durationMinusButton, 1.0);

        setupToggleGroup(kettleGroup, rmutovaciaButton);
        setupToggleGroup(processGroup, rmutovaciButton);
        setupToggleGroup(decoctionGroup, nieDecoctionButton);


        stepsTable.setItems(recipeSteps);
    }

    @FXML
    private void addStep() {
        try {
            var targetTemperature = Double.parseDouble(temperatureLabel.getText());
            var duration = Integer.parseInt(durationLabel.getText());
            var vessel = getSelectedKettle();
            var isTransferStep = getSelectedDecoction();
            var process = getSelectedProcess();

            var step = new RecipeStep(stepNumber, targetTemperature, duration, vessel, isTransferStep, process);
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
        durationSlider.setValue(0.0);
        temperatureSlider.setValue(20.0);
        kettleGroup.selectToggle(rmutovaciaButton);
        decoctionGroup.selectToggle(nieDecoctionButton);
        processGroup.selectToggle(rmutovaciButton);
    }

    private void createCellsOfTable() {
        configureIntegerColumn(stepNumberColumn, RecipeStep::getStepNumber);
        configureDoubleColumn(targetTempColumn, RecipeStep::getTargetTemperature);
        configureIntegerColumn(durationColumn, RecipeStep::getDuration);
        configureStringColumn(vesselColumn, step -> step.getVessel().toString());
        configureBooleanColumn(transferColumn, RecipeStep::isDecoctionStep);
        configureStringColumn(processColumn, step -> step.getProcess().toString());
    }

    private void setupSliderAndLabel(Slider slider, Label label, double min, double max, double initialValue) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(initialValue);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> label.setText(String.format("%.0f", newValue.doubleValue())));
    }

    private void setupButtons(Slider slider, Button plusButton, Button minusButton, double step) {
        plusButton.setOnAction(event -> {
            double currentValue = slider.getValue();
            if (currentValue < slider.getMax()) {
                slider.setValue(currentValue + step);
            }
        });

        minusButton.setOnAction(event -> {
            double currentValue = slider.getValue();
            if (currentValue > slider.getMin()) {
                slider.setValue(currentValue - step);
            }
        });
    }

    private void setupToggleGroup(ToggleGroup toggleGroup, ToggleButton toggleButton) {
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton selectedButton = (ToggleButton) newValue;
                System.out.println("Selected: " + selectedButton.getText());
            } else {
                System.out.println("No selected.");
            }
        });

        toggleGroup.selectToggle(toggleButton);
    }

    private BrewingVessel getSelectedKettle() {
        ToggleButton selectedButton = (ToggleButton) kettleGroup.getSelectedToggle();
        if (selectedButton == rmutovaciaButton) {
            return BrewingVessel.MAIN_KETTLE;
        } else if (selectedButton == vystieraciaButton) {
            return BrewingVessel.DECOCTION_KETTLE;
        }
        return BrewingVessel.MAIN_KETTLE;
    }

    private BrewingProcess getSelectedProcess() {
        ToggleButton selectedButton = (ToggleButton) processGroup.getSelectedToggle();
        if (selectedButton == rmutovaciButton) {
            return BrewingProcess.MASHING;
        } else if (selectedButton == chmelovarButton) {
            return BrewingProcess.HOPPING;
        }
        return BrewingProcess.MASHING;
    }

    private boolean getSelectedDecoction() {
        ToggleButton selectedButton = (ToggleButton) decoctionGroup.getSelectedToggle();
        if (selectedButton == anoDecoctionButton) {
            return true;
        } else if (selectedButton == nieDecoctionButton) {
            return false;
        }
        return false;
    }
}
