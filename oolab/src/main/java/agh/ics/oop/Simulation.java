package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.*;

public class Simulation implements Runnable{
    public List<Animal> getDeadAnimals() {
        return deadAnimals;
    }

    private List<Animal> deadAnimals = new ArrayList<>();

    private GrassField map;

    private final int energyCost;

    private final int plantPerDay;

    private final int plantEnergy;

    private final int reproduceEnergyRequired;

    private final int reproduceEnergyLost;

    private int maxMutations;
    private int minMutations;

    private int genNumber;

    private int day=0;


    public int getDay() {
        return day;
    }

    private UUID simulationID = UUID.randomUUID();

    private volatile boolean isPaused = false; // Flaga do zatrzymywania/wznawiania symulacji


    public Simulation(List<Vector2d> initialPositions, GrassField map, int genNumber, int startEnergy, int energyCost, int plantPerDay, int plantEnergy, int reproduceEnergyRequired, int reproduceEnergyLost, int minMutations, int maxMutations, Boolean isSpecialGen){
        this.plantEnergy = plantEnergy;
        this.energyCost = energyCost;
        this.plantPerDay = plantPerDay;
        this.reproduceEnergyRequired = reproduceEnergyRequired;
        this.reproduceEnergyLost = reproduceEnergyLost;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genNumber = genNumber;
        for(Vector2d position : initialPositions){
            int[] genes = GenoType.createRandomGenoType(genNumber);
            Animal currAnimal = new Animal(position,genes,startEnergy,isSpecialGen);
            map.place(currAnimal);
        }
        this.map = map;
    }

    List<Animal> getAnimals() {
        return Collections.unmodifiableList(map.getAnimalsObj());
    }

    public void run(){
        while(true) {
            if (!isPaused) {
                removeDeadAnimals();
                updateAnimals();
                eat();
                animalsReproduce();
                map.makeGrassMap(plantPerDay);
                day++;
                map.updateDrawMap();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private synchronized void updateAnimals(){
        for(Animal animal : map.getAnimalsObj()){
            map.move(animal,-energyCost);
        }

    }

    private synchronized void eat(){
        map.eatGrassByAnimals(plantEnergy);
    }

    public void stopSimulation() {
        isPaused = true;
    }

    public void resumeSimulation() {
        isPaused = false;
    }

    private synchronized void removeDeadAnimals() {
        ArrayList<Animal> animals = new ArrayList<>(map.getAnimalsObj());
        for (Animal animal : animals) {
            if (animal.getCurrentEnergy() < 0) {
                animal.setDeathDay(day);
                map.removeAnimalFromMap(animal);
                deadAnimals.add(animal);
            }
        }
    }

    private synchronized void animalsReproduce(){
        map.reproduce(genNumber,minMutations,maxMutations,reproduceEnergyLost,reproduceEnergyRequired);
    }

}
