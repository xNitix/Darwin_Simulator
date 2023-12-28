package agh.ics.oop;
import agh.ics.oop.model.*;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;

public class SimulationApp extends Application {
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane root = loader.load();
            SimulationPresenter controller = loader.getController();

            // Inicjalizacja SimulationPresenter
            controller.initialize(); // Możesz dodać swoją logikę inicjalizacji, jeśli taka jest potrzebna

            TabPane tabPane = new TabPane();
            Tab parametersTab = new Tab("Parameters");
            VBox parametersContent = new VBox();

            Button startButton = getButton(controller, tabPane);

            parametersContent.getChildren().addAll(root, startButton);
            parametersTab.setContent(parametersContent);
            tabPane.getTabs().add(parametersTab);

            Scene scene = new Scene(new BorderPane(tabPane), 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Simulation App");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Button getButton(SimulationPresenter controller, TabPane tabPane) {
        Button startButton = new Button("Start Simulation");
        startButton.setOnAction(event -> {
            // Tworzenie zakładki i zawartości symulacji
            Tab simulationTab = new Tab("Simulation");

            // Utworzenie kontenera HBox do przechowywania statystyk, przycisków i mapy
            HBox simulationContent = new HBox();

            // Utworzenie kontenera VBox do przechowywania statystyk i przycisków
            VBox leftContent = new VBox();

            GridPane mapGrid = new GridPane();
            controller.setMapGrid(mapGrid);

            Label animalCountLabel = new Label();
            Label daysAliveLabel = new Label();

            controller.setAnimalCountLabel(animalCountLabel);
            controller.setDaysAliveLabel(daysAliveLabel);

            // Dodanie przycisków
            Button pauseButton = new Button("Pause");
            pauseButton.setOnAction(e -> controller.onPauseSimulation());

            Button resetButton = new Button("Reset");
            resetButton.setOnAction(e -> controller.onResumeSimulation());

            // Dodanie statystyk i przycisków do lewego kontenera
            leftContent.getChildren().addAll(animalCountLabel, daysAliveLabel, pauseButton, resetButton);

            // Dodanie lewego kontenera i mapy do głównego kontenera
            simulationContent.getChildren().addAll(leftContent, mapGrid);

            // Ustawienie właściwości HBox
            HBox.setHgrow(mapGrid, Priority.ALWAYS); // Mapa będzie się rozciągać, aby wypełnić dostępną przestrzeń

            // Ustawienie właściwości VBox
            leftContent.setAlignment(Pos.TOP_LEFT); // Statystyki i przyciski będą na górze po lewej stronie
            leftContent.setSpacing(10); // Dodanie odstępu między elementami

            simulationTab.setContent(simulationContent);
            tabPane.getTabs().add(simulationTab);
            tabPane.getSelectionModel().select(simulationTab);

            controller.onSimulationStartClicked(event);
        });
        return startButton;
    }



    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }


}
