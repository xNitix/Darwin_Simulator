package agh.ics.oop;
import java.util.Properties;

public class SimulationConfig { // record?
    private final String title;
    private final String normalNextGenButton;

    private final String madnessNextGenButton;

    private final String animalNumber;

    private final String genNumberField;

    private final String startEnergyField;

    private final String moveEnergyCost;

    private final String reproduceEnergyField;

    private final String reproduceEnergyLostField;

    private final String minMutationsField;

    private final String maxMutationsField;

    private final String forestedEquatorRadioButton;

    private final String poisonedFruitRadioButton;
    private final String widthField;

    private final String heightField;

    private final String grassQuantityField;

    private final String energyEatField;

    private final String plantPerDayField;

    private final String statisticCSVButton;

    public SimulationConfig(Properties properties) {
        this.title = properties.getProperty("title");
        this.normalNextGenButton = properties.getProperty("normalNextGenButton");
        this.madnessNextGenButton = properties.getProperty("madnessNextGenButton");
        this.animalNumber = properties.getProperty("animalNumber");
        this.genNumberField = properties.getProperty("genNumberField");
        this.startEnergyField = properties.getProperty("startEnergyField");
        this.moveEnergyCost = properties.getProperty("moveEnergyCost");
        this.reproduceEnergyField = properties.getProperty("reproduceEnergyField");
        this.reproduceEnergyLostField = properties.getProperty("reproduceEnergyLostField");
        this.minMutationsField = properties.getProperty("minMutationsField");
        this.maxMutationsField = properties.getProperty("maxMutationsField");
        this.forestedEquatorRadioButton = properties.getProperty("forestedEquatorRadioButton");
        this.poisonedFruitRadioButton = properties.getProperty("poisonedFruitRadioButton");
        this.widthField = properties.getProperty("widthField");
        this.heightField = properties.getProperty("heightField");
        this.grassQuantityField = properties.getProperty("grassQuantityField");
        this.energyEatField = properties.getProperty("energyEatField");
        this.plantPerDayField = properties.getProperty("plantPerDayField");
        this.statisticCSVButton = properties.getProperty("statisticCSVButton");
    }

    public String getTitle() {
        return title;
    }

    public String getNormalNextGenButton() {
        return normalNextGenButton;
    }

    public String getMadnessNextGenButton() {
        return madnessNextGenButton;
    }

    public String getAnimalNumber() {
        return animalNumber;
    }

    public String getGenNumberField() {
        return genNumberField;
    }

    public String getStartEnergyField() {
        return startEnergyField;
    }

    public String getMoveEnergyCost() {
        return moveEnergyCost;
    }

    public String getReproduceEnergyField() {
        return reproduceEnergyField;
    }

    public String getReproduceEnergyLostField() {
        return reproduceEnergyLostField;
    }

    public String getMinMutationsField() {
        return minMutationsField;
    }

    public String getMaxMutationsField() {return maxMutationsField;}

    public String getForestedEquatorRadioButton() {
        return forestedEquatorRadioButton;
    }

    public String getPoisonedFruitRadioButton() {
        return poisonedFruitRadioButton;
    }

    public String getWidthField() {
        return widthField;
    }

    public String getHeightField() {
        return heightField;
    }

    public String getGrassQuantityField() {
        return grassQuantityField;
    }

    public String getEnergyEatField() {
        return energyEatField;
    }

    public String getPlantPerDayField() {
        return plantPerDayField;
    }

    public String getStatisticCSVButton() {
        return statisticCSVButton;
    }

}
