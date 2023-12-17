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
    private Spinner genNumber;
    @FXML
    private Label description;
    @FXML
    private GridPane mapGrid;
    private WorldMap worldMap;
    @FXML
    private TextField inputField;
    private static final int CELL_SIZE = 40;

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
        int numRows = bounds.rightUp().getY() - bounds.leftDown().getY() + 1;
        int numCols = bounds.rightUp().getX() - bounds.leftDown().getX() + 1;

        for (int i = 0; i <= numCols; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));
        }
        for (int i = 0; i <= numRows; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
        }

        Label leftUp = new Label("y/x");
        mapGrid.add(leftUp, 0, 0);
        GridPane.setHalignment(leftUp, HPos.CENTER);

        for (int x = 1; x <= numCols; x++) {
            Label columnDescription = new Label(String.valueOf(x - 1 + bounds.leftDown().getX()));
            mapGrid.add(columnDescription, x, 0);
            GridPane.setHalignment(columnDescription, HPos.CENTER);
        }

        for (int y = numRows; y > 0; y--) {
            Label rowsDescription = new Label(String.valueOf(y - 1 + bounds.leftDown().getY()));
            mapGrid.add(rowsDescription, 0, numRows - y + 1);
            GridPane.setHalignment(rowsDescription, HPos.CENTER);
        }

        for (int y = numRows; y > 0; y--) {
            for (int x = 1; x <= numCols; x++) {
                Rectangle komorka = new Rectangle(CELL_SIZE, CELL_SIZE);
                komorka.setStroke(Color.BLACK);
                Vector2d aktualnaPozycja = new Vector2d(x - 1 + bounds.leftDown().getX(), y - 1 + bounds.leftDown().getY());

                // Ustawianie kolorów na podstawie typu obiektu na danej pozycji
                Object obiekt = worldMap.objectAt(aktualnaPozycja);
                if (obiekt instanceof Grass) {
                    komorka.setFill(Color.RED);
                } else if (obiekt instanceof Animal) {
                    komorka.setFill(Color.YELLOW);
                } else {
                    komorka.setFill(Color.GREEN); // Domyślny kolor dla innych obiektów
                }

                GridPane.setHalignment(komorka, HPos.CENTER);
                mapGrid.add(komorka, x, numRows - y + 1);
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
        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(5,4));
        Simulation simulation = new Simulation(positions, worldMap, (Integer) genNumber.getValue());

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
