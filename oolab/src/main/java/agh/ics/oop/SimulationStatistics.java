package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.GrassField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimulationStatistics {
    private final GrassField grassField; // Referencja do obiektu GrassField

    private final Simulation simulation;

    public SimulationStatistics(GrassField grassField, Simulation simulation) {
        this.grassField = grassField;
        this.simulation = simulation;
    }

    public synchronized int getNumberOfInsistingAnimals() {
        ArrayList<Animal> animals = grassField.getAnimalsObj();
        return animals.size();
    }

    public synchronized double getAvgDaysAlive() {
        List<Animal> animals = simulation.getDeadAnimals();
        int avg = 0;
        int animalCounter = 0;

        for (Animal animal : animals) {
            int daysAlive = animal.getDayAlive();
            avg = avg + daysAlive;
            animalCounter ++;
        }

        return (double) avg /animalCounter;
    }


    public int getAnimalCount() {
        return simulation.getAnimals().size();
    }

    public int getDay(){
        return simulation.getDay();
    }

    public int getPlantsCount() {
        return grassField.getGrassesObj().size();
    }

    public int getFreeFieldsCount() {
        return grassField.freePosition().size();
    }

    public double getLiveAnimalsAvgEnergy(){
        ArrayList<Animal> animals = grassField.getAnimalsObj();
        int sumEnergy = 0;
        for(Animal animal : animals){
            sumEnergy += animal.getCurrentEnergy();
        }
        return  (double) sumEnergy/animals.size();
    }

    public double getAliveAnimalsChildAvg(){
        ArrayList<Animal> animals = grassField.getAnimalsObj();
        int sumChild = 0;
        for(Animal animal : animals){
            sumChild += animal.getChildNumber();
        }
        return  (double) sumChild/animals.size();
    }

    public int[] getDominantGenoType() {
        int[] genotype = new int[0];
        int cnt = 0;

        for (Map.Entry<int[],Integer> entry: grassField.getMapGenotypes().entrySet()) {
            int[] key = entry.getKey();
            Integer value = entry.getValue();
            if (value > cnt) {
                cnt = value;
                genotype = key;
            }
        }
        return genotype;
    }
}
