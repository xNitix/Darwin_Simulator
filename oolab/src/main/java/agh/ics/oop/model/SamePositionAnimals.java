package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SamePositionAnimals implements WorldElement {
    private final Vector2d position;

    public List<Animal> getAnimals() {
        return animals;
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

    public ArrayList<Animal> findStrongestAnimals() {
        ArrayList<Animal> strongestAnimals = new ArrayList<>();
        int mostEnergy = 0;
        int mostDays = 0;
        int mostChild = 0;

        for (Animal animal : animals) {
            int energy = animal.getCurrentEnergy();
            int daysAlive = animal.getDayAlive();
            int childNumber = animal.getChildNumber();

            if (strongestAnimals.isEmpty()) {
                strongestAnimals.add(animal);
                mostEnergy = energy;
                mostDays = daysAlive;
                mostChild = childNumber;
            } else {
                if (energy > mostEnergy) {
                    strongestAnimals.clear();
                    strongestAnimals.add(animal);
                    mostEnergy = energy;
                    mostDays = daysAlive;
                    mostChild = childNumber;
                } else if (energy == mostEnergy) {
                    if (daysAlive > mostDays) {
                        strongestAnimals.clear();
                        strongestAnimals.add(animal);
                        mostDays = daysAlive;
                        mostChild = childNumber;
                    } else if (daysAlive == mostDays) {
                        if (childNumber > mostChild) {
                            strongestAnimals.clear();
                            strongestAnimals.add(animal);
                            mostChild = childNumber;
                        } else if (childNumber == mostChild) {
                            strongestAnimals.add(animal);
                        }
                    }
                }
            }
        }

        return strongestAnimals;
    }

    public ArrayList<Animal> findTwoStrongestAnimals() {
        ArrayList<Animal> result = new ArrayList<>();
        Random random = new Random();

        while (result.size() < 2 && !animals.isEmpty()) {
            ArrayList<Animal> strongestAnimals = this.findStrongestAnimals();

            if (strongestAnimals.isEmpty()) {
                break;
            }

            if (strongestAnimals.size() > 2) {
                int index1 = random.nextInt(strongestAnimals.size());
                int index2;
                do {
                    index2 = random.nextInt(strongestAnimals.size());
                } while (index2 == index1);

                result.add(strongestAnimals.get(index1));
                result.add(strongestAnimals.get(index2));
            } else {
                result.addAll(strongestAnimals);
            }

        }

        return result;
    }

    public Animal getRandomStrongest(){
        Random random = new Random();
        if(!animals.isEmpty()) {
            ArrayList<Animal> strongestAnimals = this.findStrongestAnimals();

            if (strongestAnimals.isEmpty()) {
                return null;
            }
            int index1 = random.nextInt(strongestAnimals.size());

            return strongestAnimals.get(index1);

        }
        return null;
    }

}
