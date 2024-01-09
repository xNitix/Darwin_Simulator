package agh.ics.oop.model;

import java.util.*;

public class SamePositionAnimals implements WorldElement {
    private final Vector2d position;

    public synchronized List<Animal> getAnimals() {
        return animals;
    }

    public int size(){
        return animals.size();
    }

    private final List<Animal> animals = Collections.synchronizedList(new ArrayList<>());

    private final GrassField map;

    public SamePositionAnimals(Vector2d position, Animal animal, GrassField map) {
        this.position = position;
        animals.add(animal);
        this.map = map;
    }

    public synchronized void addAnimal(Animal animal){
        int flag = 0;
        for(Animal animalc : animals){
            if (animalc.equals(animal)) {
                flag = 1;
                break;
            }
        }
        if(flag == 0){
            animals.add(animal);
        }

    }

    public synchronized void removeAnimal(Animal animalToRemove) {
        animals.remove(animalToRemove);
    }

    public boolean isEmpty() {
        return animals.isEmpty();
    }

    @Override
    public boolean isAt(Vector2d position) {
        return false;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public synchronized ArrayList<Animal> findStrongestAnimals() {
        ArrayList<Animal> shuffledAnimals = new ArrayList<>(animals);
        Collections.shuffle(shuffledAnimals);

        shuffledAnimals.sort(
                Comparator.<Animal>comparingInt(animal -> animal.getCurrentEnergy())
                        .thenComparingInt(animal -> animal.getDayAlive())
                        .thenComparingInt(animal -> animal.getChildNumber())
                        .reversed() // Odwrócenie kolejności dla energii, by była od największej
        );

        return shuffledAnimals;
    }

    public ArrayList<Animal> findTwoStrongestAnimals() {
        ArrayList<Animal> result = new ArrayList<>();
        if(!animals.isEmpty() && animals.size() > 1) {
            ArrayList<Animal> strongestAnimals = findStrongestAnimals();
            result.add(strongestAnimals.get(0));
            result.add(strongestAnimals.get(1));
            return result;
        }
        return null;
    }

    public Animal getRandomStrongest(){
        if(!animals.isEmpty()) {
            ArrayList<Animal> strongestAnimals = findStrongestAnimals();
            return strongestAnimals.get(0);
        }
        return null;
    }

}
