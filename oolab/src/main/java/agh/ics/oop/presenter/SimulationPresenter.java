package agh.ics.oop.presenter;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationConfig;
import agh.ics.oop.SimulationStatistics;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    private RadioButton statisticCSVButton;
    @FXML
    private RadioButton madnessNextGenButton;
    @FXML
    private RadioButton normalNextGenButton;
    @FXML
    private RadioButton forestedEquatorRadioButton;
    @FXML
    private RadioButton poisonedFruitRadioButton;
    @FXML
    private TextField maxMutationsField;
    @FXML
    private TextField minMutationsField;
    @FXML
    private TextField reproduceEnergyLostField;
    @FXML
    private TextField reproduceEnergyField;
    @FXML
    private TextField moveEnergyCost;
    @FXML
    private TextField energyEatField;
    @FXML
    private TextField plantPerDayField;
    @FXML
    private TextField startEnergyField;
    @FXML
    private TextField animalNumber;
    @FXML
    private TextField grassQuantityField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField widthField;
    @FXML
    private TextField genNumberField;
    @FXML
    private Label description;

    public void setMapGrid(GridPane mapGrid) {
        Platform.runLater(() -> {
            this.mapGrid = mapGrid;
        });
    }

    @FXML
    private GridPane mapGrid;

    @FXML
    private VBox mapAndTrack;
    private GrassField worldMap;
    private int cellSize = 280;
    private int startEnergy;
    private int moveCost;
    private int startAnimalNumber;
    private int energyForGrass;
    private int plantPerDay;
    private int reproduceEnergy;
    private int reproduceEnergyLost;
    private SimulationEngine simulationEngine = new SimulationEngine();
    private int maxMutations;
    private int minMutations;

    private ToggleGroup radioGroup;

    private ToggleGroup radioGroupGen;

    private Boolean isSpecial = false;
    private Boolean isSpecialGen = false;

    private Boolean isCSVActive = false;

    private int genNumber;

    Image blackCat = new Image("file:oolab/src/main/resources/koty/kot1.png");
    Image grayCat = new Image("file:oolab/src/main/resources/koty/kot2.png");
    Image redCat = new Image("file:oolab/src/main/resources/koty/kot3.png");
    Image orangekCat = new Image("file:oolab/src/main/resources/koty/kot4.png");
    Image yellowCat = new Image("file:oolab/src/main/resources/koty/kot5.png");
    Image goodPlant = new Image("file:oolab/src/main/resources/koty/grass1.png");
    Image badPlant = new Image("file:oolab/src/main/resources/koty/grass2.png");

    String folderPath = "oolab/src/main/resources/StatisticsCSV";

    String filePath;

    public void setStatisticsGrid(GridPane statisticsGrid) {
        this.statisticsGrid = statisticsGrid;
    }

    @FXML
    private GridPane statisticsGrid;

    public void setPlantCountLabel(Label plantCountLabel) {
        this.plantCountLabel = plantCountLabel;
    }

    @FXML
    private Label plantCountLabel;

    public void setFreeFieldCountLabel(Label freeFieldCountLabel) {
        this.freeFieldCountLabel = freeFieldCountLabel;
    }

    @FXML
    private Label freeFieldCountLabel;

    public void setMostFamounsGenoTypeLabel(Label mostFamounsGenoTypeLabel) {
        this.mostFamounsGenoTypeLabel = mostFamounsGenoTypeLabel;
    }

    @FXML
    private Label mostFamounsGenoTypeLabel;

    public void setLiveAnimalsAvgEnergyLabel(Label liveAnimalsAvgEnergy) {
        this.liveAnimalsAvgEnergyLabel = liveAnimalsAvgEnergy;
    }

    @FXML
    private Label liveAnimalsAvgEnergyLabel;

    public void setLiveAnimalsChildAvgLabel(Label liveAnimalsChildAvg) {
        this.liveAnimalsChildAvgLabel = liveAnimalsChildAvg;
    }

    @FXML
    private Label liveAnimalsChildAvgLabel;

    public void setAnimalCountLabel(Label animalCountLabel) {
        this.animalCountLabel = animalCountLabel;
    }

    @FXML
    private Label animalCountLabel;

    public void setDeadAniamlsDaysAlivedLabel(Label daysAliveLabel) {
        this.daysAliveLabel = daysAliveLabel;
    }

    @FXML
    private Label daysAliveLabel;
    private SimulationStatistics statistics; // Referencja do obiektu SimulationStatistics

    @FXML
    private LineChart<Number, Number> lineChart;
    private Map<Integer, Integer> animalCountData = new HashMap<>();
    private Map<Integer, Integer> plantCountData = new HashMap<>();

    public void setLegendContainer(VBox legendContainer) {
        this.legendContainer = legendContainer;
    }

    @FXML
    private VBox legendContainer;

    @FXML
    private Button dominantButton;

    public void setLineChart(LineChart<Number, Number> lineChart) {
        this.lineChart = lineChart;

        lineChart.setPrefWidth(400); //szerokość
        lineChart.setPrefHeight(300); //wysokość

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



    public void setWorldMap(GrassField worldMap) {
        this.worldMap = worldMap;
    }
    private void clearGrid() {
        mapGrid.setGridLinesVisible(false);
        mapGrid.getChildren().clear();
        mapGrid.setGridLinesVisible(true);
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void drawMap(){
        clearGrid();

        Boundary bounds = worldMap.getCurrentBounds();
        int numRows = bounds.rightUp().getY() - bounds.leftDown().getY() ;
        int numCols = bounds.rightUp().getX() - bounds.leftDown().getX() ;

        for (int i = 0; i <= numCols; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        }
        for (int i = 0; i <= numRows; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        }

        //Label leftUp = new Label("y/x");
        //mapGrid.add(leftUp, 0, 0);
        //GridPane.setHalignment(leftUp, HPos.CENTER);

        //for (int x = 1; x <= numCols; x++) {
        //    Label columnDescription = new Label(String.valueOf(x - 1 + bounds.leftDown().getX()));
        //    mapGrid.add(columnDescription, x, 0);
        //    GridPane.setHalignment(columnDescription, HPos.CENTER);
        //}

        //for (int y = numRows; y > 0; y--) {
        //    Label rowsDescription = new Label(String.valueOf(y - 1 + bounds.leftDown().getY()));
        //    mapGrid.add(rowsDescription, 0, numRows - y + 1);
        //    GridPane.setHalignment(rowsDescription, HPos.CENTER);
        //}
        Vector2d trackPosition = null;
        if(statistics.getSelectedAnimal() != null && statistics.getSelectedAnimal().getCurrentEnergy() > -1)
        {
            trackPosition = statistics.getSelectedAnimal().getPosition();
        }
        for (int y = numRows; y >= 0; y--) {
            for (int x = 0; x <= numCols; x++) {
                Rectangle komorka = new Rectangle(cellSize, cellSize);
                komorka.setStroke(Color.BLACK);
                Vector2d aktualnaPozycja = new Vector2d(x + bounds.leftDown().getX(), y + bounds.leftDown().getY());

                if(trackPosition != null){
                    if(aktualnaPozycja.equals(trackPosition)){
                        komorka.setStroke(Color.RED);
                        komorka.setStrokeWidth(3);
                    }
                }

                FieldType fieldType = worldMap.getFieldType(aktualnaPozycja);
                if(fieldType == FieldType.PREFERRED){
                    komorka.setFill(Color.GREEN);
                    mapGrid.add(komorka, x, numRows - y);
                } else {
                    komorka.setFill(Color.rgb(215, 255, 190));
                    mapGrid.add(komorka, x, numRows - y);
                }

                Object obiekt = worldMap.objectAt(aktualnaPozycja);
                if (obiekt instanceof Grass) {
                    ImageView imageView = new ImageView(goodPlant);
                    imageView.setFitWidth(cellSize);
                    imageView.setFitHeight(cellSize);
                    mapGrid.add(imageView, x, numRows - y);
                } else if(obiekt instanceof BadGrass) {
                    ImageView imageView = new ImageView(badPlant);
                    imageView.setFitWidth(cellSize);
                    imageView.setFitHeight(cellSize);
                    mapGrid.add(imageView, x, numRows - y);
                } else if (obiekt instanceof Animal) {
                    int energy = ((Animal) obiekt).getCurrentEnergy();
                    //System.out.println((double)energy/startEnergy*100);
                    if((double)energy/startEnergy*100 > 80){
                        ImageView imageView = new ImageView(blackCat);
                        imageView.setFitWidth(cellSize);
                        imageView.setFitHeight(cellSize);
                        mapGrid.add(imageView, x, numRows - y);
                    }else if((double)energy/startEnergy*100 <= 80 && (double)energy/startEnergy*100 > 60){
                        ImageView imageView = new ImageView(grayCat);
                        imageView.setFitWidth(cellSize);
                        imageView.setFitHeight(cellSize);
                        mapGrid.add(imageView, x, numRows - y);
                    }else if((double)energy/startEnergy*100 <= 60 && (double)energy/startEnergy*100 > 40){
                        ImageView imageView = new ImageView(redCat);
                        imageView.setFitWidth(cellSize);
                        imageView.setFitHeight(cellSize);
                        mapGrid.add(imageView, x, numRows - y);
                    }else if((double)energy/startEnergy*100 <= 40 && (double)energy/startEnergy*100 > 20){
                        ImageView imageView = new ImageView(orangekCat);
                        imageView.setFitWidth(cellSize);
                        imageView.setFitHeight(cellSize);
                        mapGrid.add(imageView, x, numRows - y);
                    }else if((double)energy/startEnergy*100 <= 20){
                        ImageView imageView = new ImageView(yellowCat);
                        imageView.setFitWidth(cellSize);
                        imageView.setFitHeight(cellSize);
                        mapGrid.add(imageView, x, numRows - y);
                    }
                }
                GridPane.setHalignment(komorka, HPos.CENTER);
            }
        }

    }

    private int simulationDay= -1;
    @Override
    public void mapChanged(GrassField worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            description.setText(message);
            if(statistics.getDay() != simulationDay){
                updateStatistics();
                simulationDay = statistics.getDay();
            }
        });
    }

    public void onSimulationStartClicked() {
        try {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            int grassQuantity = Integer.parseInt(grassQuantityField.getText());
            startEnergy = Integer.parseInt(startEnergyField.getText());
            moveCost = Integer.parseInt(moveEnergyCost.getText());
            startAnimalNumber = Integer.parseInt(animalNumber.getText());
            plantPerDay = Integer.parseInt(plantPerDayField.getText());
            energyForGrass = Integer.parseInt(energyEatField.getText());
            reproduceEnergy = Integer.parseInt(reproduceEnergyField.getText());
            reproduceEnergyLost = Integer.parseInt(reproduceEnergyLostField.getText());
            minMutations = Integer.parseInt(minMutationsField.getText());
            maxMutations = Integer.parseInt(maxMutationsField.getText());
            genNumber = Integer.parseInt(genNumberField.getText());
            cellSize=  (int) 430/Math.max(width,height);

            if (width <= 0 || height <= 0 || grassQuantity < 0 || startEnergy < 0) {

            } else {
                GrassField map = new GrassField(grassQuantity, width, height, isSpecial, isSpecialGen);
                map.subscribe(this);
                setWorldMap(map);

                List<Vector2d> positions = generateStartPositions();
                Simulation simulation = new Simulation(positions, worldMap, genNumber, startEnergy, moveCost, plantPerDay, energyForGrass, reproduceEnergy, reproduceEnergyLost, minMutations, maxMutations, isSpecialGen);
                statistics = new SimulationStatistics(map,simulation);

                simulationEngine.addSimulation(simulation);

            }
            if(isCSVActive){
                newFile();
            }
        } catch (NumberFormatException e) {
            // tu tez
        }
    }

    public void onPauseSimulation() {
        if (simulationEngine != null) {
            simulationEngine.stopAllSimulations();
            dominantButton.setVisible(true);
        }
    }

    public void onResumeSimulation() {
        if (simulationEngine != null) {
            simulationEngine.resumeAllSimulations();
            dominantButton.setVisible(false);
        }
    }

    public void initialize() {
        statisticCSVButton.setSelected(false);
        radioGroup = new ToggleGroup();
        poisonedFruitRadioButton.setToggleGroup(radioGroup);
        forestedEquatorRadioButton.setToggleGroup(radioGroup);
        // Ustawienie domyślnie zaznaczonego przycisku
        forestedEquatorRadioButton.setSelected(true);

        radioGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selectedRadioButton = (RadioButton) newValue;
            if (selectedRadioButton == poisonedFruitRadioButton) {
                isSpecial = true;
            } else if (selectedRadioButton == forestedEquatorRadioButton) {
                isSpecial = false;
            }
        });

        radioGroupGen = new ToggleGroup();
        normalNextGenButton.setToggleGroup(radioGroupGen);
        madnessNextGenButton.setToggleGroup(radioGroupGen);
        normalNextGenButton.setSelected(true);

        radioGroupGen.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selectedRadioButton = (RadioButton) newValue;
            if (selectedRadioButton == madnessNextGenButton) {
                isSpecialGen = true;
            } else if (selectedRadioButton == normalNextGenButton) {
                isSpecialGen = false;
            }
        });

        animalNumber.setText("3");
        genNumberField.setText("3");
        startEnergyField.setText("15");
        widthField.setText("5");
        heightField.setText("5");
        grassQuantityField.setText("10");
        energyEatField.setText("5");
        moveEnergyCost.setText("1");
        plantPerDayField.setText("2");
        reproduceEnergyField.setText("5");
        reproduceEnergyLostField.setText("4");
        minMutationsField.setText("4");
        maxMutationsField.setText("7");
    }

    @FXML
    private void onRadioButtonClicked() {
        if (poisonedFruitRadioButton.isSelected()) {
            isSpecial = true;
        } else if (forestedEquatorRadioButton.isSelected()) {
            isSpecial = false;
        }
    }

    @FXML
    private void onRadioButtonClickedGen() {
        if (madnessNextGenButton.isSelected()) {
            isSpecialGen = true;
        } else if (normalNextGenButton.isSelected()) {
            isSpecialGen = false;
        }
    }

    private List<Vector2d> generateStartPositions(){
        List<Vector2d> positions = new ArrayList<>();
        Random random = new Random();

        Boundary bounds = worldMap.getCurrentBounds();
        int width = bounds.rightUp().getX() - bounds.leftDown().getX() + 1;
        int height = bounds.rightUp().getY() - bounds.leftDown().getY() + 1;

        for (int i = 0; i < startAnimalNumber; i++) {
            int x = random.nextInt(width) + bounds.leftDown().getX();
            int y = random.nextInt(height) + bounds.leftDown().getY();
            positions.add(new Vector2d(x, y));
        }


        return positions;

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
            appendToCSV(filePath,row);
        }
    }
    public SimulationStatistics getSimulationStatistics() {
        return statistics;
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

    public void createLegend() {
        Rectangle jungleRectangle = new Rectangle(30, 30);
        jungleRectangle.setFill(Color.GREEN);
        Label jungleLabel = new Label("Jungle");
        HBox jungleEntryBox = new HBox(jungleRectangle, jungleLabel);
        jungleEntryBox.setSpacing(5);
        legendContainer.getChildren().add(jungleEntryBox);

        Rectangle stepRectangle = new Rectangle(30, 30);
        stepRectangle.setFill(Color.rgb(215, 255, 190));
        Label stepLabel = new Label("Step");
        HBox stepEntryBox = new HBox(stepRectangle, stepLabel);
        stepEntryBox.setSpacing(5);
        legendContainer.getChildren().add(stepEntryBox);

        Map<Image, String> legendData = new LinkedHashMap<>();
        legendData.put(blackCat, "Above 80% energy");
        legendData.put(grayCat, "60% - 80% energy");
        legendData.put(redCat, "40% - 60% energy");
        legendData.put(orangekCat, "20% - 40% energy");
        legendData.put(yellowCat, "Below 20% energy");
        legendData.put(goodPlant, "Plant");
        if(isSpecial){
            legendData.put(badPlant, "Poisonous plant");
        }

        for (Map.Entry<Image, String> entry : legendData.entrySet()) {
            ImageView imageView = new ImageView(entry.getKey());
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            Label label = new Label(entry.getValue());
            HBox entryBox = new HBox(imageView, label);
            entryBox.setSpacing(5);
            legendContainer.getChildren().add(entryBox);
        }

    }

    @FXML
    public void onFollowSimulation() {
        List<Animal> dominantGenotypeAnimals = statistics.findDominantGenotypeAnimals();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dominant Genotype Animals");
        alert.setHeaderText(null);

        VBox animalsInfo = new VBox();
        for (Animal animal : dominantGenotypeAnimals) {
            HBox animalDetails = new HBox();

            Label idLabel = new Label("ID: " + animal.getId());
            Label positionLabel = new Label("  Position: " + animal.getPosition());
            Label energyLabel = new Label("  Energy: " + animal.getCurrentEnergy());
            Label isDeadLabel = new Label(animal.getCurrentEnergy() < 0 ? " (Dead)" : " (Alive)");
            animalDetails.getChildren().addAll(idLabel, positionLabel, energyLabel, isDeadLabel);

            animalsInfo.getChildren().add(animalDetails);
        }

        alert.getDialogPane().setContent(animalsInfo);
        alert.showAndWait();
    }

    public void setDominantButton(Button dominantButton) {
        this.dominantButton = dominantButton;
    }

    public void ontrackAnimalButton() {
        List<Animal> animals = statistics.getDeadAndAliveAnimals();

        List<String> choices = animals.stream()
                .sorted(Comparator.comparingInt(Animal::getId))
                .map(animal -> "ID: " + animal.getId() + ", Position: " + animal.getPosition() + ", Energy: " + animal.getCurrentEnergy())
                .collect(Collectors.toList());

        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, choices);
        dialog.setTitle("Select Animal");
        dialog.setHeaderText("Choose an animal:");
        dialog.setContentText("Animal:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedAnimalInfo -> {
            int selectedAnimalID = Integer.parseInt(selectedAnimalInfo.split(":")[1].split(",")[0].trim()); // Pobranie ID wybranego zwierzęcia

            Animal selectedAnimal = statistics.findSelectedAnimal(selectedAnimalID);
            updateSelectedAnimalStats(selectedAnimal);

        });
    }

    public void setMapAndTrack(VBox mapAndTrack) {
        this.mapAndTrack = mapAndTrack;
    }

    public synchronized void updateSelectedAnimalStats(Animal selectedAnimal) {
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
            Label activeGen = new Label("Active gen: " + selectedAnimal.getGenoType()[selectedAnimal.getWhichGen()]);
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

    public void onRadioButtonClickedCSV() {
        if (statisticCSVButton.isSelected()) {
            isCSVActive = true;
        }
    }

    private void newFile(){
        String[] headers = {"Day", "Animals Count", "Plants Count", "Free fields count", "Famous Genotype", "AVG energy for living animals", "AVG dead animals life length", "AVG child count"};
        String fileName = generateFileName();
        this.filePath = Paths.get(folderPath, fileName).toString();
        writeCSV(filePath, headers);
    }

    private static String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "statistics_" + dateFormat.format(new Date()) + ".csv";
    }

    // Method to write CSV file with headers
    private static void writeCSV(String filePath, String[] headers) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            writer.println(String.join(";", headers));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to append data to an existing CSV file
    private static void appendToCSV(String filePath, String[] data) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
            writer.println(String.join(";", data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<SimulationConfig> configurations;

    public void showConfigurationsAlert(ActionEvent actionEvent) {
        loadConfigurationsFromProperties();

        List<String> configurationTitles = new ArrayList<>();
        for (SimulationConfig config : configurations) {
            configurationTitles.add(config.getTitle());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, configurationTitles);
        dialog.setTitle("Select Configuration");
        dialog.setHeaderText("Choose a configuration:");
        dialog.setContentText("Configuration:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedConfigTitle -> {
            SimulationConfig selectedConfig = findConfigByTitle(selectedConfigTitle);
            updateSelectedConfigStats(selectedConfig);
        });
    }

    private SimulationConfig findConfigByTitle(String title) {
        return configurations.stream()
                .filter(config -> config.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    private void updateSelectedConfigStats(SimulationConfig selectedConfig) {
        animalNumber.setText(selectedConfig.getAnimalNumber());
        genNumberField.setText(selectedConfig.getGenNumberField());
        startEnergyField.setText(selectedConfig.getStartEnergyField());
        widthField.setText(selectedConfig.getWidthField());
        heightField.setText(selectedConfig.getHeightField());
        grassQuantityField.setText(selectedConfig.getGrassQuantityField());
        energyEatField.setText(selectedConfig.getEnergyEatField());
        moveEnergyCost.setText(selectedConfig.getMoveEnergyCost());
        plantPerDayField.setText(selectedConfig.getPlantPerDayField());
        reproduceEnergyField.setText(selectedConfig.getReproduceEnergyField());
        reproduceEnergyLostField.setText(selectedConfig.getReproduceEnergyLostField());
        minMutationsField.setText(selectedConfig.getMinMutationsField());
        maxMutationsField.setText(selectedConfig.getMinMutationsField());
        isSpecial = (Boolean.parseBoolean(selectedConfig.getPoisonedFruitRadioButton()));
        isSpecialGen = (Boolean.parseBoolean(selectedConfig.getMadnessNextGenButton()));
        forestedEquatorRadioButton.setSelected(Boolean.parseBoolean(selectedConfig.getForestedEquatorRadioButton()));
        poisonedFruitRadioButton.setSelected(Boolean.parseBoolean(selectedConfig.getPoisonedFruitRadioButton()));
        madnessNextGenButton.setSelected(Boolean.parseBoolean(selectedConfig.getMadnessNextGenButton()));
        normalNextGenButton.setSelected(Boolean.parseBoolean(selectedConfig.getNormalNextGenButton()));
        statisticCSVButton.setSelected(Boolean.parseBoolean(selectedConfig.getStatisticCSVButton()));
        isCSVActive = Boolean.parseBoolean(selectedConfig.getStatisticCSVButton());
    }

    public void saveConfiguration() {
        Properties properties = new Properties();
        String configurationsFolderPath = "oolab/src/main/resources/Configurations";

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New property file");
        dialog.setHeaderText("Enter new configuration title:");
        dialog.setContentText("Title: ");
        Optional<String> result = dialog.showAndWait();

        String newConfigFileName;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        if (result.isEmpty()) {
            properties.setProperty("title", dateFormat.format(new Date() + ".properties"));
            newConfigFileName = dateFormat.format(new Date()) + ".properties";
        } else {
            properties.setProperty("title", result.get() + ".properties");
            newConfigFileName = result.get() + ".properties";
        }

        properties.setProperty("animalNumber", animalNumber.getText());
        properties.setProperty("genNumberField", genNumberField.getText());
        properties.setProperty("startEnergyField",startEnergyField.getText());
        properties.setProperty("widthField",  widthField.getText());
        properties.setProperty("heightField", heightField.getText());
        properties.setProperty("grassQuantityField", grassQuantityField.getText());
        properties.setProperty("energyEatField",  energyEatField.getText());
        properties.setProperty("moveEnergyCost",   moveEnergyCost.getText());
        properties.setProperty("plantPerDayField", plantPerDayField.getText());
        properties.setProperty("reproduceEnergyField",  reproduceEnergyField.getText());
        properties.setProperty("reproduceEnergyLostField", reproduceEnergyLostField.getText());
        properties.setProperty("minMutationsField", minMutationsField.getText());
        properties.setProperty("maxMutationsField", maxMutationsField.getText());
        properties.setProperty("poisonedFruitRadioButton", isSpecial ? "true" : "false");
        properties.setProperty("madnessNextGenButton", isSpecialGen ? "true" : "false");
        properties.setProperty("forestedEquatorRadioButton", isSpecial ? "false" : "true");
        properties.setProperty("normalNextGenButton", isSpecialGen ? "false" : "true");
        properties.setProperty("statisticCSVButton", isCSVActive ? "true" : "false");

        try (FileWriter fileWriter = new FileWriter(configurationsFolderPath + File.separator + newConfigFileName)) {
            properties.store(fileWriter, "New configuration");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfigurationsFromProperties() {
        configurations = new ArrayList<>();

        File folder = new File("oolab/src/main/resources/Configurations");
        File[] configFiles = folder.listFiles();

        if (configFiles != null) {
            for (File configFile : configFiles) {
                if (configFile.isFile() && configFile.getName().endsWith(".properties")) {
                    try (InputStream input = new FileInputStream(configFile)) {
                        Properties properties = new Properties();
                        properties.load(input);
                        SimulationConfig config = new SimulationConfig(properties);
                        configurations.add(config);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
