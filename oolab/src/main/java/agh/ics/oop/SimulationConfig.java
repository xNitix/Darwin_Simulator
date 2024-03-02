package agh.ics.oop;

import java.util.Properties;

public record SimulationConfig(
        String title,
        String normalNextGenButton,
        String madnessNextGenButton,
        String animalNumber,
        String genNumberField,
        String startEnergyField,
        String moveEnergyCost,
        String reproduceEnergyField,
        String reproduceEnergyLostField,
        String minMutationsField,
        String maxMutationsField,
        String forestedEquatorRadioButton,
        String poisonedFruitRadioButton,
        String widthField,
        String heightField,
        String grassQuantityField,
        String energyEatField,
        String plantPerDayField,
        String statisticCSVButton
) {
    public SimulationConfig(Properties properties) {
        this(
                properties.getProperty("title"),
                properties.getProperty("normalNextGenButton"),
                properties.getProperty("madnessNextGenButton"),
                properties.getProperty("animalNumber"),
                properties.getProperty("genNumberField"),
                properties.getProperty("startEnergyField"),
                properties.getProperty("moveEnergyCost"),
                properties.getProperty("reproduceEnergyField"),
                properties.getProperty("reproduceEnergyLostField"),
                properties.getProperty("minMutationsField"),
                properties.getProperty("maxMutationsField"),
                properties.getProperty("forestedEquatorRadioButton"),
                properties.getProperty("poisonedFruitRadioButton"),
                properties.getProperty("widthField"),
                properties.getProperty("heightField"),
                properties.getProperty("grassQuantityField"),
                properties.getProperty("energyEatField"),
                properties.getProperty("plantPerDayField"),
                properties.getProperty("statisticCSVButton")
        );
    }
}
