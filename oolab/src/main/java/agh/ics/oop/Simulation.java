package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.Exceptions.PositionAlreadyOccupiedException;

import java.util.ArrayList;
import java.util.*;

public class Simulation implements Runnable{

    private List<Animal> animals;
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

    private volatile boolean isPaused = false; // Flaga do zatrzymywania/wznawiania symulacji


    public Simulation(List<Vector2d> initialPositions, GrassField map, int genNumber, int startEnergy, int energyCost, int plantPerDay, int plantEnergy, int reproduceEnergyRequired, int reproduceEnergyLost, int minMutations, int maxMutations){
        this.plantEnergy = plantEnergy;
        List<Animal> animals = new ArrayList<>();
        this.energyCost = energyCost;
        this.plantPerDay = plantPerDay;
        this.reproduceEnergyRequired = reproduceEnergyRequired;
        this.reproduceEnergyLost = reproduceEnergyLost;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genNumber = genNumber;
        for(Vector2d position : initialPositions){
            int[] genes = GenoType.createRandomGenoType(genNumber);
            Animal currAnimal = new Animal(position,genes,startEnergy,map);
            map.place(currAnimal);
            animals.add(currAnimal);

        }
        this.animals = animals;
        this.map = map;
    }


    List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public void run(){
        while(true) {
            if (!isPaused) {
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
                try {
                    Thread.sleep(1000); // Opóźnienie 1 sekunda między kolejnymi aktualizacjami
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private void updateAnimals(){
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

        for(Animal animal : animals){
            map.move(animal,-energyCost);
            System.out.println(animal.getCurrentEnergy());
        }

    }

    private void eat(){
        map.eatGrassByAnimals(plantEnergy);
    }

    public void stopSimulation() {
        isPaused = true;
    }

    public void resumeSimulation() {
        isPaused = false;
    }

    private void removeDeadAnimals() {
        //System.out.println("ala");
        Iterator<Animal> iterator = animals.iterator();
        //System.out.println("ala1");
        while (iterator.hasNext()) {
            //System.out.println("ala2");
            Animal animal = iterator.next();
            //System.out.println("ala3");
            if (animal.getCurrentEnergy() < 0) {
                //System.out.println("ala4");
                map.removeAnimalFromMap(animal);
                //System.out.println("ala5");
                deadAnimals.add(animal);
                //System.out.println("ala6");
                iterator.remove();
                //System.out.println("ala7");
            }
        }
    }

    private void animalsReproduce(){
        //System.out.println("sym");
        List<Animal> newAnimals = map.reproduce(genNumber,minMutations,maxMutations,reproduceEnergyLost,reproduceEnergyRequired);
        animals.addAll(newAnimals);
    }



}
