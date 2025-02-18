package cz.diplomka.pivovarfe.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.diplomka.pivovarfe.constant.BrewingProcess;
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

    @JsonProperty("isDecoctionStep")
    private boolean isDecoctionStep;

    @JsonProperty("process")
    private BrewingProcess process;


    public RecipeStep(int stepNumber, double targetTemperature, int duration, BrewingVessel vessel, boolean isDecoctionStep, BrewingProcess process) {
        this.stepNumber = stepNumber;
        this.targetTemperature = targetTemperature;
        this.duration = duration;
        this.vessel = vessel;
        this.isDecoctionStep = isDecoctionStep;
        this.process = process;
    }

    @JsonCreator
    public RecipeStep(
            @JsonProperty("id") Long id,
            @JsonProperty("stepNumber") int stepNumber,
            @JsonProperty("targetTemperature") double targetTemperature,
            @JsonProperty("duration") int duration,
            @JsonProperty("vessel") BrewingVessel vessel,
            @JsonProperty("decoctionStep") boolean isDecoctionStep,
            @JsonProperty("process") BrewingProcess process) {
        this.id = id;
        this.stepNumber = stepNumber;
        this.targetTemperature = targetTemperature;
        this.duration = duration;
        this.vessel = vessel;
        this.isDecoctionStep = isDecoctionStep;
        this.process = process;
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

    public boolean isDecoctionStep() {
        return isDecoctionStep;
    }

    public void setTransferStep(boolean decoctionStep) {
        isDecoctionStep = decoctionStep;
    }

    public void setDecoctionStep(boolean decoctionStep) {
        isDecoctionStep = decoctionStep;
    }

    public BrewingProcess getProcess() {
        return process;
    }

    public void setProcess(BrewingProcess process) {
        this.process = process;
    }
}

