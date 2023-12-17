package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.Exceptions.PositionAlreadyOccupiedException;

import java.util.ArrayList;
import java.util.*;

public class Simulation implements Runnable{

    private List<Animal> animals;

    private WorldMap map;

    private volatile boolean isPaused = false; // Flaga do zatrzymywania/wznawiania symulacji


    public Simulation(List<Vector2d> initialPositions, WorldMap map, int genNumber){
        List<Animal> animals = new ArrayList<>();
        for(Vector2d position : initialPositions){
            int[] genes = GenoType.createRandomGenoType(genNumber);
            Animal currAnimal = new Animal(position,genes);
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
    }
    public void stopSimulation() {
        isPaused = true;
    }

    public void resumeSimulation() {
        isPaused = false;
    }

}
