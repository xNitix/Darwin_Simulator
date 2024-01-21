package agh.ics.oop.presenter;

import agh.ics.oop.SimulationStatistics;
import agh.ics.oop.model.Animal;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatisticPresenter {
    private GridPane mapGrid;

    private SimulationStatistics statistics;

    private Label plantCountLabel;

    private Label freeFieldCountLabel;

    private Label mostFamounsGenoTypeLabel;

    private Label liveAnimalsAvgEnergyLabel;

    private Label liveAnimalsChildAvgLabel;

    private Label animalCountLabel;

    private Label daysAliveLabel;

    private LineChart<Number, Number> lineChart;

    private final Map<Integer, Integer> animalCountData = new HashMap<>();

    private final Map<Integer, Integer> plantCountData = new HashMap<>();

    private VBox mapAndTrack;

    private boolean isCSVActive;

    String folderPath = "oolab/src/main/resources/StatisticsCSV";

    String filePath;


    public StatisticPresenter() {
    }

    public void updateStatistics() {
        int animalCount = statistics.getNumberOfInsistingAnimals();
        double daysAlive = (double) Math.round(statistics.getAvgDaysAlive()* 100) /100;
        int plantCount = statistics.getPlantsCount();
        int freeFieldsCount = statistics.getFreeFieldsCount();
        double liveAnimalsAvgEnergy = (double) Math.round(statistics.getLiveAnimalsAvgEnergy() * 100) /100;
        double liveAnimalsChildAvg = (double) Math.round(statistics.getAliveAnimalsChildAvg()* 100)/100;
        int[] mostFamousGenoType = statistics.getDominantGenoType();

        animalCountLabel.setText("Animal Count: " + animalCount);
        plantCountLabel.setText("Plant Count: " + plantCount);
        freeFieldCountLabel.setText("Free Fields Count: " + freeFieldsCount);
        mostFamounsGenoTypeLabel.setText("Famous Genotype: " + Arrays.toString(mostFamousGenoType));
        liveAnimalsAvgEnergyLabel.setText("Alive Animals AVG Energy: " + liveAnimalsAvgEnergy);
        daysAliveLabel.setText("Average Dead Animals Life Length: " + daysAlive);
        liveAnimalsChildAvgLabel.setText("Alive Animals Child AVG: " + liveAnimalsChildAvg);
        updateChart();
        updateSelectedAnimalStats(statistics.getSelectedAnimal());
        if(isCSVActive){
            String[] row = {String.valueOf(statistics.getDay()), String.valueOf(animalCount), String.valueOf(plantCount), String.valueOf(freeFieldsCount),
                    Arrays.toString(mostFamousGenoType), String.valueOf(liveAnimalsAvgEnergy), String.valueOf(daysAlive),String.valueOf(liveAnimalsChildAvg)};
            try {
                appendToCSV(filePath,row);
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void updateChart() {
        int animalCount = statistics.getAnimalCount();
        int plantCount = statistics.getPlantsCount();
        int days = statistics.getDay();

        // Dodanie aktualnych danych do map
        animalCountData.put(days, animalCount);
        plantCountData.put(days, plantCount);

        XYChart.Series<Number, Number> animalCountSeries = lineChart.getData().get(0);
        XYChart.Series<Number, Number> plantCountSeries = lineChart.getData().get(1);

        // Aktualizacja danych w istniejących seriach
        animalCountSeries.getData().add(new XYChart.Data<>(days, animalCount));
        plantCountSeries.getData().add(new XYChart.Data<>(days, plantCount));

    }

    public void updateSelectedAnimalStats(Animal selectedAnimal) {
        if (selectedAnimal != null) {
            VBox animalStatsBox = new VBox();
            animalStatsBox.setSpacing(2);
            animalStatsBox.setStyle("-fx-background-color: white;-fx-border-color: black; -fx-border-width: 3px;-fx-padding: 3;");
            Label animalInfoLabel = new Label("Selected Animal Info:");
            animalInfoLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
            Label idLabel = new Label("ID: " + selectedAnimal.getId());
            idLabel.setStyle("-fx-font-weight: bold;");
            Label positionLabel = new Label("Position: " + selectedAnimal.getPosition());
            positionLabel.setStyle("-fx-font-weight: bold;");
            Label genoType = new Label("Genotype: " + Arrays.toString(selectedAnimal.getGenoType()));
            Label activeGen = new Label("Active gen: " + selectedAnimal.getGenoType()[selectedAnimal.getWhichGenIsActive()]);
            Label energyLabel = new Label("Energy: " + selectedAnimal.getCurrentEnergy());
            Label grassEatenCounter = new Label("Eaten grass counter: " + selectedAnimal.getGrassEatenCounter());
            Label childCount = new Label("Child counter: " + selectedAnimal.getChildNumber());
            Label lifeStatus = new Label();
            if(selectedAnimal.getDeathDay() == -1){
                lifeStatus.setText("Days Alive: " + selectedAnimal.getDayAlive());
            } else {
                lifeStatus.setText("Death day: " + selectedAnimal.getDeathDay());
            }
            Label descendants = new Label("Number of descendants: " + selectedAnimal.getDescendantsNumber());

            animalStatsBox.getChildren().addAll(animalInfoLabel, idLabel, positionLabel, genoType, activeGen, energyLabel, grassEatenCounter, childCount, lifeStatus, descendants);
            mapAndTrack.getChildren().clear();
            mapAndTrack.getChildren().addAll(mapGrid, animalStatsBox);
        }
    }
    protected void newFile() throws IOException {
        String[] headers = {"Day", "Animals Count", "Plants Count", "Free fields count", "Famous Genotype", "AVG energy for living animals", "AVG dead animals life length", "AVG child count"};
        String fileName = generateFileName();
        this.filePath = Paths.get(folderPath, fileName).toString();
        writeCSV(filePath, headers);
    }

    private static String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "statistics_" + dateFormat.format(new Date()) + ".csv";
    }

    // dodanie naglowkow do pliku
    private static void writeCSV(String filePath, String[] headers) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
        writer.println(String.join(";", headers));
        writer.flush();
    }

    private static void appendToCSV(String filePath, String[] data) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
        writer.println(String.join(";", data));
        writer.flush();
    }

    public void setLineChart(LineChart<Number, Number> lineChart) {
        this.lineChart = lineChart;

        lineChart.setPrefWidth(400);
        lineChart.setPrefHeight(300);

        // Inicjalizacja serii danych dla ilości zwierząt
        XYChart.Series<Number, Number> animalCountSeries = new XYChart.Series<>();
        animalCountSeries.setName("Animal Count");

        // Inicjalizacja serii danych dla ilości roslin
        XYChart.Series<Number, Number> plantCountSeries = new XYChart.Series<>();
        plantCountSeries.setName("Plant Count");

        lineChart.getData().addAll(animalCountSeries, plantCountSeries);

        lineChart.getXAxis().setLabel("Day");
        lineChart.getYAxis().setLabel("Count");
    }

    public void setPlantCountLabel(Label plantCountLabel) {
        this.plantCountLabel = plantCountLabel;
    }

    public void setFreeFieldCountLabel(Label freeFieldCountLabel) {
        this.freeFieldCountLabel = freeFieldCountLabel;
    }


    public void setMostFamousGenoTypeLabel(Label mostFamounsGenoTypeLabel) {
        this.mostFamounsGenoTypeLabel = mostFamounsGenoTypeLabel;
    }


    public void setLiveAnimalsAvgEnergyLabel(Label liveAnimalsAvgEnergy) {
        this.liveAnimalsAvgEnergyLabel = liveAnimalsAvgEnergy;
    }


    public void setLiveAnimalsChildAvgLabel(Label liveAnimalsChildAvg) {
        this.liveAnimalsChildAvgLabel = liveAnimalsChildAvg;
    }

    public void setAnimalCountLabel(Label animalCountLabel) {
        this.animalCountLabel = animalCountLabel;
    }

    public void setDeadAnimalsDaysAliveLabel(Label daysAliveLabel) {
        this.daysAliveLabel = daysAliveLabel;
    }

    public void setMapAndTrack(VBox mapAndTrack) {
        this.mapAndTrack = mapAndTrack;
    }

    public void setMapGrid(GridPane mapGrid) {
        this.mapGrid = mapGrid;
    }

    public void setStatistics(SimulationStatistics statistics) {
        this.statistics = statistics;
    }

    public void setCSVActive(boolean CSVActive) {
        isCSVActive = CSVActive;
    }
}
