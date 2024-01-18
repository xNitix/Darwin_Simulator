package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.Exceptions.PositionAlreadyOccupiedException;

import java.util.ArrayList;
import java.util.*;

public class Simulation implements Runnable{

    //private List<Animal> animals;

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

    //private int animalId = 0;

    public int getDay() {
        return day;
    }

    private UUID simulationID = UUID.randomUUID();

    private volatile boolean isPaused = false; // Flaga do zatrzymywania/wznawiania symulacji


    public Simulation(List<Vector2d> initialPositions, GrassField map, int genNumber, int startEnergy, int energyCost, int plantPerDay, int plantEnergy, int reproduceEnergyRequired, int reproduceEnergyLost, int minMutations, int maxMutations, Boolean isSpecialGen){
        this.plantEnergy = plantEnergy;
        //List<Animal> animals = new ArrayList<>();
        this.energyCost = energyCost;
        this.plantPerDay = plantPerDay;
        this.reproduceEnergyRequired = reproduceEnergyRequired;
        this.reproduceEnergyLost = reproduceEnergyLost;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genNumber = genNumber;
        for(Vector2d position : initialPositions){
            int[] genes = GenoType.createRandomGenoType(genNumber);
            Animal currAnimal = new Animal(position,genes,startEnergy,map,isSpecialGen);
            map.place(currAnimal);
            //animals.add(currAnimal);
            //animalId++;
        }
        //this.animals = animals;
        this.map = map;
    }

    /*
    public int getNewAnimalId(){
        animalId++;
        return animalId;
    }


     */

    List<Animal> getAnimals() {
        return Collections.unmodifiableList(map.getAnimalsObj());
    }

    public void run(){
        while(true) {
            if (!isPaused) {
                System.out.println(simulationID);
                //System.out.println("tu1");
                //System.out.println(deadAnimals.size());
                //System.out.println("tu2");
                removeDeadAnimals();
                //System.out.println("tu3");
                updateAnimals();
                //System.out.println("tu4");
                eat();
                //System.out.println("tu5");
                animalsReproduce();
                map.makeGrassMap(plantPerDay);
                //System.out.println("tu6");
                day++;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private synchronized void updateAnimals(){
        /*
        int sizeOfAnimals = this.animals.size();
        int iterator = 0;
        while (true) {
            if (!isPaused) {
                Animal currAnimal = animals.get(iterator % sizeOfAnimals);

                map.move(currAnimal);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                iterator++;
            }
        }

         */

        for(Animal animal : map.getAnimalsObj()){
            map.move(animal,-energyCost);
            System.out.println(animal.getCurrentEnergy());
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
        //System.out.println("ala");
        //System.out.println("ala1");
        ArrayList<Animal> animals = new ArrayList<>(map.getAnimalsObj());
        for (Animal animal : animals) {
            //System.out.println("ala2");
            //System.out.println("ala3");
            if (animal.getCurrentEnergy() < 0) {
                animal.setDeathDay(day);
                //System.out.println("ala4");
                map.removeAnimalFromMap(animal);
                //System.out.println("ala5");
                deadAnimals.add(animal);
                //System.out.println("ala6");
                //iterator.remove();
                //System.out.println("ala7");
            }
        }
    }

    private synchronized void animalsReproduce(){
        //System.out.println("sym");
        map.reproduce(genNumber,minMutations,maxMutations,reproduceEnergyLost,reproduceEnergyRequired);
        //map.getAnimalsObj().addAll(newAnimals);
    }

}
