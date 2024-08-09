package agh.ics.oop.simulation;

import agh.ics.oop.model.maps.AbstractWorldMap;
import agh.ics.oop.model.entity.Animal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SimulationStatistics {
    private final AbstractWorldMap grassField;

    private final Simulation simulation;

    private Animal selectedAnimal = null;

    public SimulationStatistics(AbstractWorldMap grassField, Simulation simulation) {
        this.grassField = grassField;
        this.simulation = simulation;
    }

    public int getNumberOfInsistingAnimals() {
        List<Animal> animals = grassField.getAnimalsObj();
        return animals.size();
    }

    public double getAvgDaysAlive() {
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

    public double getLiveAnimalsAvgEnergy(){
        List<Animal> animals = grassField.getAnimalsObj();
        int sumEnergy = 0;
        for(Animal animal : animals){
            sumEnergy += animal.getCurrentEnergy();
        }
        return  (double) sumEnergy/animals.size();
    }

    public double getAliveAnimalsChildAvg(){
        List<Animal> animals = grassField.getAnimalsObj();
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

    public List<Animal> findDominantGenotypeAnimals() {
        List<Animal> result = new ArrayList<>();
        int[] dominantGenoType = getDominantGenoType();
        List<Animal> animals = getDeadAndAliveAnimals();
        for(Animal animal : animals){
            if(Arrays.equals(animal.getGenoType(), dominantGenoType)){
                result.add(animal);
            }
        }
        return result;
    }

    public List<Animal> getDeadAndAliveAnimals(){
        List<Animal> animals = new ArrayList<>(grassField.getAnimalsObj());
        animals.addAll(simulation.getDeadAnimals());
        return animals;
    }

    public Animal findSelectedAnimal(int selectedID) {
        List<Animal> animals = getDeadAndAliveAnimals();
        for (Animal animal : animals) {
            if (animal.getId() == selectedID) {
                selectedAnimal = animal;
                return animal;
            }
        }
        return null;
    }

    public Animal getSelectedAnimal() {
        return selectedAnimal;
    }

    public int getDay(){ return simulation.getDay(); }

    public int getAnimalCount() {
        return simulation.getAnimals().size();
    }

    public int getPlantsCount() {
        return grassField.getGrassesObj().size();
    }

    public int getFreeFieldsCount() {
        return grassField.freePosition().size();
    }

}
