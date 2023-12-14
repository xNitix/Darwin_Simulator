package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Exceptions.PositionAlreadyOccupiedException;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;
import java.util.*;

public class Simulation implements Runnable{

    private List<Animal> animals;
    private List<MoveDirection> moveDirections;

    private WorldMap map;


    public Simulation(List<Vector2d> initialPositions, List<MoveDirection> directions, WorldMap map){
        List<Animal> animals = new ArrayList<>();
        for(Vector2d position : initialPositions){
            Animal currAnimal = new Animal(position);
           try{
               map.place(currAnimal);
               animals.add(currAnimal);
           }catch(PositionAlreadyOccupiedException e){
               System.out.println(e.getMessage());
           }
        }
        this.animals = animals;
        this.moveDirections = directions;
        this.map = map;
    }

    List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public void run(){
        int sizeOfAnimals = this.animals.size();
        int sizeOfDiresctions = this.moveDirections.size();

        for(int i = 0; i < sizeOfDiresctions; i++)
        {
            MoveDirection direction = moveDirections.get(i);
            Animal currAnimal = animals.get(i%sizeOfAnimals);

            map.move(currAnimal,direction);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
