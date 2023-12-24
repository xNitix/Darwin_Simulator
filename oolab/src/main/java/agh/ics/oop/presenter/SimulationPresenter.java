package agh.ics.oop.presenter;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SimulationPresenter implements MapChangeListener {
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
    private Spinner genNumber;
    @FXML
    private Label description;
    @FXML
    private GridPane mapGrid;
    private WorldMap worldMap;
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
    public void setWorldMap(WorldMap worldMap) {
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

                // Ustawianie kolorów na podstawie typu obiektu na danej pozycji
                Object obiekt = worldMap.objectAt(aktualnaPozycja);
                if (obiekt instanceof Grass) {
                    komorka.setFill(Color.RED);
                } else if (obiekt instanceof Animal) {
                    int energy = ((Animal) obiekt).getCurrentEnergy();
                    //System.out.println((double)energy/startEnergy*100);
                    if((double)energy/startEnergy*100 > 80){
                        komorka.setFill(Color.BLACK);
                    }else if((double)energy/startEnergy*100 <= 80 && (double)energy/startEnergy*100 > 60){
                        komorka.setFill(Color.GRAY);
                    }else if((double)energy/startEnergy*100 <= 60 && (double)energy/startEnergy*100 > 40){
                        komorka.setFill(Color.rgb(150, 75, 0));
                    }else if((double)energy/startEnergy*100 <= 40 && (double)energy/startEnergy*100 > 20){
                        komorka.setFill(Color.rgb(255, 140, 0));
                    }else if((double)energy/startEnergy*100 <= 20 && (double)energy/startEnergy*100 >= 0){
                        komorka.setFill(Color.rgb(255, 255, 0));
                    }
                } else {
                    FieldType fieldType = worldMap.getFieldType(aktualnaPozycja);
                    if(fieldType == FieldType.PREFERRED){
                        komorka.setFill(Color.rgb(0, 100, 0));
                    } else {
                        komorka.setFill(Color.rgb(100, 50, 0));
                    }
                }

                GridPane.setHalignment(komorka, HPos.CENTER);
                mapGrid.add(komorka, x, numRows - y);
            }
        }

    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            description.setText(message);
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
            cellSize=cellSize/width;

            if (width <= 0 || height <= 0 || grassQuantity < 0 || startEnergy < 0) {
                // obsluzyc wyjatki
            } else {
                GrassField map = new GrassField(grassQuantity, width, height);
                map.subscribe(this);
                setWorldMap(map);
            }
        } catch (NumberFormatException e) {
            // tu tez
        }


        List<Vector2d> positions = generateStartPositions();
        Simulation simulation = new Simulation(positions, worldMap, (Integer) genNumber.getValue(), startEnergy, moveCost, plantPerDay, energyForGrass, reproduceEnergy, reproduceEnergyLost, minMutations, maxMutations);

        setWorldMap(worldMap);
        SimulationEngine simulationEngine = new SimulationEngine(Arrays.asList(simulation),4);
        this.simulationEngine = simulationEngine;
        simulationEngine.runAsyncInThreadPool();

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
        // Ustawienie wartości domyślnej dla Spinnera genNumber
        genNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 5)); // zakres od 0 do oo, wartość domyślna 5
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

}
