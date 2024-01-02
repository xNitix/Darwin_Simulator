package agh.ics.oop;
import agh.ics.oop.model.*;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;

public class SimulationApp extends Application {

    private BorderPane viewRoot;

    public void start(Stage primaryStage) {
        configureStage(primaryStage, viewRoot);
        primaryStage.show();
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
            Label plantCountLabel = new Label();
            Label freeFieldCountLabel = new Label();
            Label mostFamounsGenoTypeLabel = new Label();
            Label liveAnimalsAvgEnergyLabel = new Label();
            Label deadAniamlsDaysAlivedLabel = new Label();
            Label liveAnimalsChildAvgLabel = new Label();

            LineChart<Number, Number> lineChart = new LineChart<>(new NumberAxis(), new NumberAxis());

            controller.setLineChart(lineChart);
            controller.setAnimalCountLabel(animalCountLabel);
            controller.setDeadAniamlsDaysAlivedLabel(deadAniamlsDaysAlivedLabel);
            controller.setPlantCountLabel(plantCountLabel);
            controller.setFreeFieldCountLabel(freeFieldCountLabel);
            controller.setMostFamounsGenoTypeLabel(mostFamounsGenoTypeLabel);
            controller.setLiveAnimalsAvgEnergyLabel(liveAnimalsAvgEnergyLabel);
            controller.setLiveAnimalsChildAvgLabel(liveAnimalsChildAvgLabel);

            Button pauseButton = new Button("Pause");
            pauseButton.setOnAction(e -> controller.onPauseSimulation());

            Button resetButton = new Button("Reset");
            resetButton.setOnAction(e -> controller.onResumeSimulation());

            Button dominantButton = new Button("Dominant Genotype Animals");
            dominantButton.setVisible(false);
            controller.setDominantButton(dominantButton);
            dominantButton.setOnAction(e -> controller.onFollowSimulation());

            HBox buttonContainer = new HBox(pauseButton, resetButton, dominantButton);
            buttonContainer.setSpacing(10); // Odstęp między przyciskami

            VBox legendContainer = new VBox();
            controller.setLegendContainer(legendContainer);
            controller.createLegend();

            // Dodanie statystyk i przycisków do lewego kontenera
            leftContent.getChildren().addAll(animalCountLabel, plantCountLabel, freeFieldCountLabel, mostFamounsGenoTypeLabel, liveAnimalsAvgEnergyLabel, deadAniamlsDaysAlivedLabel, liveAnimalsChildAvgLabel, legendContainer, lineChart, buttonContainer);

            // Dodanie lewego kontenera i mapy do głównego kontenera
            simulationContent.getChildren().addAll(leftContent, mapGrid);

            // Ustawienie właściwości VBox
            leftContent.setAlignment(Pos.TOP_LEFT); // Statystyki i przyciski będą na górze po lewej stronie
            leftContent.setSpacing(15); // Dodanie odstępu między elementami

            simulationTab.setContent(simulationContent);
            tabPane.getTabs().add(simulationTab);
            tabPane.getSelectionModel().select(simulationTab);

            controller.onSimulationStartClicked(event);
        });
        return startButton;
    }



    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        if (viewRoot == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulation.fxml"));
                viewRoot = loader.load();
                SimulationPresenter controller = loader.getController();
                controller.initialize();

                TabPane tabPane = new TabPane();
                Tab parametersTab = new Tab("Parameters");
                VBox parametersContent = new VBox(10);

                parametersContent.getChildren().add(viewRoot);

                Button startButton = getButton(controller, tabPane);
                startButton.setMaxWidth(200);

                HBox buttonContainer = new HBox();
                buttonContainer.getChildren().add(startButton);
                buttonContainer.setAlignment(Pos.CENTER);

                parametersContent.getChildren().addAll(new Label(), buttonContainer);
                parametersTab.setContent(parametersContent);
                tabPane.getTabs().add(parametersTab);

                Scene scene = new Scene(new BorderPane(tabPane), 800, 900);
                primaryStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        primaryStage.setTitle("Simulation App");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }


}
