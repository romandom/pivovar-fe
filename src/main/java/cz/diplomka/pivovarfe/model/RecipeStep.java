package cz.diplomka.pivovarfe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.diplomka.pivovarfe.constant.BrewingVessel;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BrewingVessel getVessel() {
        return vessel;
    }

    public void setVessel(BrewingVessel vessel) {
        this.vessel = vessel;
    }

    public boolean isTransferStep() {
        return isTransferStep;
    }

    public void setTransferStep(boolean transferStep) {
        isTransferStep = transferStep;
    }
}

