package agh.ics.oop.presenter;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationConfig;
import agh.ics.oop.SimulationStatistics;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.*;
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

    Image blackCat = new Image("file:oolab/src/main/resources/koty/kot1.png");
    Image grayCat = new Image("file:oolab/src/main/resources/koty/kot2.png");
    Image redCat = new Image("file:oolab/src/main/resources/koty/kot3.png");
    Image orangeCat = new Image("file:oolab/src/main/resources/koty/kot4.png");
    Image yellowCat = new Image("file:oolab/src/main/resources/koty/kot5.png");
    Image goodPlant = new Image("file:oolab/src/main/resources/koty/grass1.png");
    Image badPlant = new Image("file:oolab/src/main/resources/koty/grass2.png");

    private GridPane mapGrid;

    private Tab simulationTab;

    private VBox legendContainer;

    private Button trackButton;

    private Button dominantButton;

    private int startEnergy;

    private int startAnimalNumber;

    private int cellSize = 280;

    private AbstractWorldMap worldMap;

    private final SimulationEngine simulationEngine = new SimulationEngine();

    private SimulationStatistics statistics;

    private List<SimulationConfig> configurations;

    private StatisticPresenter statisticPresenter;

    private Boolean isSpecial = false;
    private Boolean isSpecialGen = false;
    private Boolean isCSVActive = false;

    public void initialize() {
        statisticCSVButton.setSelected(false);
        ToggleGroup radioGroup = new ToggleGroup();
        poisonedFruitRadioButton.setToggleGroup(radioGroup);
        forestedEquatorRadioButton.setToggleGroup(radioGroup);
        forestedEquatorRadioButton.setSelected(true);

        radioGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selectedRadioButton = (RadioButton) newValue;
            if (selectedRadioButton == poisonedFruitRadioButton) {
                isSpecial = true;
            } else if (selectedRadioButton == forestedEquatorRadioButton) {
                isSpecial = false;
            }
        });

        ToggleGroup radioGroupGen = new ToggleGroup();
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

    public void onSimulationStartClicked() {
        try {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            int grassQuantity = Integer.parseInt(grassQuantityField.getText());
            startEnergy = Integer.parseInt(startEnergyField.getText());
            int moveCost = Integer.parseInt(moveEnergyCost.getText());
            startAnimalNumber = Integer.parseInt(animalNumber.getText());
            int plantPerDay = Integer.parseInt(plantPerDayField.getText());
            int energyForGrass = Integer.parseInt(energyEatField.getText());
            int reproduceEnergy = Integer.parseInt(reproduceEnergyField.getText());
            int reproduceEnergyLost = Integer.parseInt(reproduceEnergyLostField.getText());
            int minMutations = Integer.parseInt(minMutationsField.getText());
            int maxMutations = Integer.parseInt(maxMutationsField.getText());
            int genNumber = Integer.parseInt(genNumberField.getText());
            cellSize = (int) 430/Math.max(width,height);

            inputValidation(width,height,grassQuantity,startEnergy,moveCost,startAnimalNumber,plantPerDay,energyForGrass,reproduceEnergy,reproduceEnergyLost,minMutations,maxMutations,genNumber);

            AbstractWorldMap map;
            if(isSpecial){
                map = new PoisonGrassField(grassQuantity, width, height, isSpecialGen);
            }else{
                map = new GrassField(grassQuantity, width, height, isSpecialGen);
            }
            map.subscribe(this);
            setWorldMap(map);

            List<Vector2d> positions = generateStartPositions();
            Simulation simulation = new Simulation(positions, worldMap, genNumber, startEnergy, moveCost, plantPerDay, energyForGrass, reproduceEnergy, reproduceEnergyLost, minMutations, maxMutations, isSpecialGen);
            statistics = new SimulationStatistics(map,simulation);
            statisticPresenter.setStatistics(statistics);
            statisticPresenter.setCSVActive(isCSVActive);
            if(isCSVActive){
                try {
                    statisticPresenter.newFile();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            simulationEngine.addSimulation(simulation);
            simulationTab.setOnClosed(event -> {
                simulationEngine.shotDown();
            });

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Arguments cannot be empty");
        }
    }

    private void inputValidation(int width,int height,int grassQuantity,int startEnergy,int moveCost,int startAnimalNumber,int plantPerDay,int energyForGrass,int reproduceEnergy,int reproduceEnergyLost,int minMutations,int maxMutations,int genNumber) {
        if (width < 0 ) throw new IllegalArgumentException("Width must be grater than 0 ");

        if (height < 0) throw new IllegalArgumentException("Height must be grater than 0");

        if (grassQuantity < 0) throw new IllegalArgumentException("Start grassQuantity must be grater or equals than 0");

        if (startEnergy < 0) throw new IllegalArgumentException("Start energy must be grater than 0");

        if (moveCost < 0) throw new IllegalArgumentException("Move energy must be grater than 0");

        if (startAnimalNumber < 0) throw new IllegalArgumentException("Animal number must be grater or equals than 0");

        if (plantPerDay < 0) throw new IllegalArgumentException("Plant per day number must be grater or equals than 0");

        if (energyForGrass < 0) throw new IllegalArgumentException("Energy from grass must be grater or equals than 0");

        if (reproduceEnergy < 0) throw new IllegalArgumentException("Reproduce energy must be grater than or equals 0");

        if (reproduceEnergyLost < 0) throw new IllegalArgumentException("Cost of reproduce must be grater than or equals 0");

        if (minMutations < 0) throw new IllegalArgumentException("Minimum number of mutations must be greater than or equals 0");

        if (maxMutations < 0) throw new IllegalArgumentException("Maximum number of mutations must be greater than or equals 0");

        if (genNumber < 0) throw new IllegalArgumentException("Number of genes must be greater than or equals 0");
    }

    private void clearGrid() {
        mapGrid.setGridLinesVisible(false);
        mapGrid.getChildren().clear();
        mapGrid.setGridLinesVisible(true);
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @Override
    public void mapChanged(AbstractWorldMap worldMap) {
        Platform.runLater(() -> {
            drawMap();
            statisticPresenter.updateStatistics();
        });
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

        Vector2d trackPosition = null;
        if(statistics.getSelectedAnimal() != null && statistics.getSelectedAnimal().getCurrentEnergy() > -1)
        {
            trackPosition = statistics.getSelectedAnimal().getPosition();
        }
        for (int y = numRows; y >= 0; y--) {
            for (int x = 0; x <= numCols; x++) {
                Rectangle mapCell = new Rectangle(cellSize, cellSize);
                mapCell.setStroke(Color.BLACK);
                Vector2d currentPosition = new Vector2d(x + bounds.leftDown().getX(), y + bounds.leftDown().getY());

                if(trackPosition != null){
                    if(currentPosition.equals(trackPosition)){
                        mapCell.setStroke(Color.RED);
                        mapCell.setStrokeWidth(3);
                    }
                }

                FieldType fieldType = worldMap.getFieldType(currentPosition);
                if(fieldType == FieldType.PREFERRED){
                    mapCell.setFill(Color.GREEN);
                    mapGrid.add(mapCell, x, numRows - y);
                } else {
                    mapCell.setFill(Color.rgb(215, 255, 190));
                    mapGrid.add(mapCell, x, numRows - y);
                }
                Object object = worldMap.objectAt(currentPosition);
                if (object instanceof Grass) {
                    ImageView imageView = new ImageView(goodPlant);
                    imageView.setFitWidth(cellSize);
                    imageView.setFitHeight(cellSize);
                    mapGrid.add(imageView, x, numRows - y);
                } else if(object instanceof BadGrass) {
                    ImageView imageView = new ImageView(badPlant);
                    imageView.setFitWidth(cellSize);
                    imageView.setFitHeight(cellSize);
                    mapGrid.add(imageView, x, numRows - y);
                } else if (object instanceof Animal) {
                    int energy = ((Animal) object).getCurrentEnergy();
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
                        ImageView imageView = new ImageView(orangeCat);
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
                GridPane.setHalignment(mapCell, HPos.CENTER);
            }
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
        legendData.put(orangeCat, "20% - 40% energy");
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


    public void onPauseSimulation() {
        simulationEngine.stopSimulation();
        dominantButton.setVisible(true);
        trackButton.setVisible(true);
    }

    public void onResumeSimulation() {
        simulationEngine.resumeSimulation();
        dominantButton.setVisible(false);
        trackButton.setVisible(false);
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

    public void onRadioButtonClickedCSV() {
        if (statisticCSVButton.isSelected()) {
            isCSVActive = true;
        }
    }

    @FXML
    public void onDominantGenotype() {
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

    public void onTrackAnimalButton() {
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
            statisticPresenter.updateSelectedAnimalStats(selectedAnimal);
        });
    }

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
            System.out.println("anulowno");
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

    public void setStatisticPresenter(StatisticPresenter statisticPresenter) { this.statisticPresenter = statisticPresenter;}

    public void setSimulationTab(Tab simulationTab) {
        this.simulationTab = simulationTab;
    }

    public void setMapGrid(GridPane mapGrid) {
        Platform.runLater(() -> {
            this.mapGrid = mapGrid;
        });
    }

    public void setLegendContainer(VBox legendContainer) {
        this.legendContainer = legendContainer;
    }

    public void setWorldMap(AbstractWorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void setDominantButton(Button dominantButton) { this.dominantButton = dominantButton; }

    public void setTrackButton(Button trackButton) { this.trackButton = trackButton; }
}
