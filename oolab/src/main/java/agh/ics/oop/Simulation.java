package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.Exceptions.PositionAlreadyOccupiedException;

import java.util.ArrayList;
import java.util.*;

public class Simulation implements Runnable{

    private List<Animal> animals;

    private WorldMap map;

    private final int energyCost;

    private volatile boolean isPaused = false; // Flaga do zatrzymywania/wznawiania symulacji


    public Simulation(List<Vector2d> initialPositions, WorldMap map, int genNumber, int startEnergy, int energyCost){
        List<Animal> animals = new ArrayList<>();
        this.energyCost = energyCost;
        for(Vector2d position : initialPositions){
            int[] genes = GenoType.createRandomGenoType(genNumber);
            Animal currAnimal = new Animal(position,genes,startEnergy);
           try{
               map.place(currAnimal);
               animals.add(currAnimal);

           }catch(PositionAlreadyOccupiedException e){
               System.out.println(e.getMessage());
           }
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
                updateAnimals();
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
        }

    }
    public void stopSimulation() {
        isPaused = true;
    }

    public void resumeSimulation() {
        isPaused = false;
    }

}
