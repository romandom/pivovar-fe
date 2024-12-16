package cz.diplomka.pivovarfe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.diplomka.pivovarfe.constant.BrewingVessel;
import lombok.Data;

@Data
public class RecipeStep {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("stepNumber")
    private int stepNumber;

    @JsonProperty("targetTemperature")
    private double targetTemperature;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("vessel")
    private BrewingVessel vessel;

    @JsonProperty("isTransferStep")
    private boolean isTransferStep;

    public RecipeStep(int stepNumber, double targetTemperature, int duration, BrewingVessel vessel, boolean isTransferStep) {
        this.stepNumber = stepNumber;
        this.targetTemperature = targetTemperature;
        this.duration = duration;
        this.vessel = vessel;
        this.isTransferStep = isTransferStep;
    }
}

