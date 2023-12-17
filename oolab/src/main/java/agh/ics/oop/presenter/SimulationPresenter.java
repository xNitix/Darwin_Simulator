package agh.ics.oop.presenter;
import agh.ics.oop.OptionsParser;
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

import java.util.Arrays;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    private TextField moveEnergyCost;
    @FXML
    private TextField energyEat;
    @FXML
    private TextField plantPerDay;
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

    private SimulationEngine simulationEngine;
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
                    komorka.setFill(Color.BLUE);
                } else if (obiekt instanceof Animal) {
                    int energy = ((Animal) obiekt).getCurrentEnergy();
                    System.out.println((double)energy/startEnergy*100);
                    if((double)energy/startEnergy*100 > 80){
                        komorka.setFill(Color.BLACK);
                    }else if((double)energy/startEnergy*100 <= 80 && (double)energy/startEnergy*100 > 60){
                        komorka.setFill(Color.GRAY);
                    }else if((double)energy/startEnergy*100 <= 60 && (double)energy/startEnergy*100 > 40){
                        komorka.setFill(Color.RED);
                    }else if((double)energy/startEnergy*100 <= 40 && (double)energy/startEnergy*100 > 20){
                        komorka.setFill(Color.ORANGE);
                    }else if((double)energy/startEnergy*100 <= 20 && (double)energy/startEnergy*100 >= 0){
                        komorka.setFill(Color.YELLOW);
                    }
                } else {
                    komorka.setFill(Color.GREEN); // Domyślny kolor dla innych obiektów
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


        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(5,4));
        Simulation simulation = new Simulation(positions, worldMap, (Integer) genNumber.getValue(), startEnergy, moveCost);

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

}
