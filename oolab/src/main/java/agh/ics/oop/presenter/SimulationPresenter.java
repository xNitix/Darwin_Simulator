package agh.ics.oop.presenter;
import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import java.util.Arrays;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    private Label description;
    @FXML
    private GridPane mapGrid;
    private WorldMap worldMap;
    @FXML
    private TextField inputField;
    private static final int CELL_SIZE = 40;
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
                Label element = new Label();
                Vector2d currPosition = new Vector2d(x - 1 + bounds.leftDown().getX(), y - 1 + bounds.leftDown().getY());

                if (worldMap.objectAt(currPosition) != null) {
                    element.setText(worldMap.objectAt(currPosition).toString());
                }

                GridPane.setHalignment(element, HPos.CENTER);
                mapGrid.add(element, x, numRows - y + 1);
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
        String[] args = inputField.getText().split(" ");
        List<MoveDirection> directions = OptionsParser.parse(args);

        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(5,4));
        Simulation simulation = new Simulation(positions, directions, worldMap);

        setWorldMap(worldMap);
        SimulationEngine simulationEngine = new SimulationEngine(Arrays.asList(simulation),4);
        simulationEngine.runAsyncInThreadPool();

    }
}
