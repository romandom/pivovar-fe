package cz.diplomka.pivovarfe.model;

import cz.diplomka.pivovarfe.constant.BrewingVessel;
import lombok.Data;

@Data
public class RecipeStep {
    private Long id;

    private int stepNumber;

    private double targetTemperature;

    private int duration; // Duration in seconds

    private Recipe recipe;

    private BrewingVessel vessel;

    private boolean isTransferStep;

    public RecipeStep(int stepNumber, double targetTemperature, int duration, BrewingVessel vessel, boolean isTransferStep) {
        this.stepNumber = stepNumber;
        this.targetTemperature = targetTemperature;
        this.duration = duration;
        this.vessel = vessel;
        this.isTransferStep = isTransferStep;
    }
}

