package agh.ics.oop.presenter;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationStatistics;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.*;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class SimulationPresenter implements MapChangeListener {
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
    private GrassField worldMap;
    private int cellSize = 280;
    private int startEnergy;
    private int moveCost;
    private int startAnimalNumber;
    private int energyForGrass;
    private int plantPerDay;
    private int reproduceEnergy;
    private int reproduceEnergyLost;
    private SimulationEngine simulationEngine;
    private int maxMutations;
    private int minMutations;

    private ToggleGroup radioGroup;

    private ToggleGroup radioGroupGen;

    private Boolean isSpecial = false;
    private Boolean isSpecialGen = false;

    private int genNumber;

    Image blackCat = new Image("file:oolab/src/main/resources/koty/kot1.png");
    Image grayCat = new Image("file:oolab/src/main/resources/koty/kot2.png");
    Image redCat = new Image("file:oolab/src/main/resources/koty/kot3.png");
    Image orangekCat = new Image("file:oolab/src/main/resources/koty/kot4.png");
    Image yellowCat = new Image("file:oolab/src/main/resources/koty/kot5.png");
    Image goodPlant = new Image("file:oolab/src/main/resources/koty/grass1.png");
    Image badPlant = new Image("file:oolab/src/main/resources/koty/grass2.png");

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

        for (int y = numRows; y >= 0; y--) {
            for (int x = 0; x <= numCols; x++) {
                Rectangle komorka = new Rectangle(cellSize, cellSize);
                komorka.setStroke(Color.BLACK);
                Vector2d aktualnaPozycja = new Vector2d(x + bounds.leftDown().getX(), y + bounds.leftDown().getY());

                FieldType fieldType = worldMap.getFieldType(aktualnaPozycja);
                if(fieldType == FieldType.PREFERRED){
                    komorka.setFill(Color.GREEN);
                    mapGrid.add(komorka, x, numRows - y);
                } else {
                    komorka.setFill(Color.rgb(215, 255, 190));
                    mapGrid.add(komorka, x, numRows - y);
                }

                // Ustawianie kolorów na podstawie typu obiektu na danej pozycji
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

    @Override
    public void mapChanged(GrassField worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            description.setText(message);
            updateStatistics();
        });
    }

    public void onSimulationStartClicked(ActionEvent actionEvent) {
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
            cellSize=cellSize/width+30;

            if (width <= 0 || height <= 0 || grassQuantity < 0 || startEnergy < 0) {

            } else {
                GrassField map = new GrassField(grassQuantity, width, height, isSpecial, isSpecialGen);
                map.subscribe(this);
                setWorldMap(map);

                List<Vector2d> positions = generateStartPositions();
                Simulation simulation = new Simulation(positions, worldMap, genNumber, startEnergy, moveCost, plantPerDay, energyForGrass, reproduceEnergy, reproduceEnergyLost, minMutations, maxMutations, isSpecialGen);
                statistics = new SimulationStatistics(map,simulation);

                SimulationEngine simulationEngine = new SimulationEngine(Arrays.asList(simulation), 4);
                this.simulationEngine = simulationEngine;
                simulationEngine.runAsyncInThreadPool();
            }
        } catch (NumberFormatException e) {
            // tu tez
        }
    }

    public void onPauseSimulation() {
        if (simulationEngine != null) {
            simulationEngine.stopAllSimulations();
        }
    }

    public void onResumeSimulation() {
        if (simulationEngine != null) {
            simulationEngine.resumeAllSimulations();
        }
    }

    public void initialize() {
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
        genNumberField.setText("5");
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
        double daysAlive = statistics.getAvgDaysAlive();
        int plantCount = statistics.getPlantsCount();
        int freeFieldsCount = statistics.getFreeFieldsCount();
        double liveAnimalsAvgEnergy = statistics.getLiveAnimalsAvgEnergy();
        double liveAnimalsChildAvg = statistics.getAliveAnimalsChildAvg();
        int[] mostFamousGenoType = statistics.getDominantGenoType();

        animalCountLabel.setText("Animal Count: " + animalCount);
        plantCountLabel.setText("Plant Count: " + plantCount);
        daysAliveLabel.setText("Average Dead Animals Life Length: " + daysAlive);
        freeFieldCountLabel.setText("Free Fields Count: " + freeFieldsCount);
        liveAnimalsAvgEnergyLabel.setText("Alive Animals AVG Energy: " + liveAnimalsAvgEnergy);
        liveAnimalsChildAvgLabel.setText("Alive Animals Child AVG: " + liveAnimalsChildAvg);
        mostFamounsGenoTypeLabel.setText("Famous Genotype: " + Arrays.toString(mostFamousGenoType));
        updateChart();
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

}
