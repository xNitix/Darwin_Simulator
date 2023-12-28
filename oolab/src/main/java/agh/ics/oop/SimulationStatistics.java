package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.GrassField;

import java.util.ArrayList;

public class SimulationStatistics {
    private final GrassField grassField; // Referencja do obiektu GrassField

    public SimulationStatistics(GrassField grassField) {
        this.grassField = grassField;
    }

    public synchronized int getNumberOfInsistingAnimals() {
        ArrayList<Animal> animals = grassField.getAnimalsObj();
        return animals.size();
    }

    public synchronized double getAvgDaysAlive() {
        ArrayList<Animal> animals = grassField.getAnimalsObj();
        int avg = 0;
        int animalCounter = 0;

        for (Animal animal : animals) {
            int daysAlive = animal.getDayAlive();
            avg = avg + daysAlive;
            animalCounter ++;
        }

        return (double) avg /animalCounter;
    }
}
