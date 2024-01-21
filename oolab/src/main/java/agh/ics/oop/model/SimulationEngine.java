package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationEngine {
    private final List<Simulation> simulations = new ArrayList<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    public SimulationEngine() {

    }

    public void stopSimulation() {
        for (Simulation simulation : simulations) {
            simulation.stopSimulation();
        }
    }

    public void resumeSimulation() {
        for (Simulation simulation : simulations) {
            simulation.resumeSimulation();
        }
    }

    public void addSimulation(Simulation simulation) {
        simulations.add(simulation);
        executorService.submit(simulation);
    }

    public void shotDown(){
        executorService.shutdownNow();
    }


}