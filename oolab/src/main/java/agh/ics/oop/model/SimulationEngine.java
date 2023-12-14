package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> threads = new ArrayList<>();

    private final ExecutorService executorService;
    public SimulationEngine(List<Simulation> simulations,int n) {
        this.simulations = simulations;
        this.executorService = Executors.newFixedThreadPool(n);

    }
    public void runSync() {
        for (Simulation simulation : simulations) {
            System.out.println("Start simulation ID:");
            simulation.run();
            System.out.println("Simulation completed! \n");
        }
    }

    public void runAsync() {
        for(Simulation simulation : simulations){
            Thread thread = new Thread(simulation);
            thread.start();
            threads.add(thread);
        }
    }
    public void awaitSimulationsEnd() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Wątek został przerwany: " + e.getMessage());
            }
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("nie zakonczono watkow");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    public void runAsyncInThreadPool() {
        for (Simulation simulation : simulations) {
            executorService.submit(simulation);
        }
    }


}