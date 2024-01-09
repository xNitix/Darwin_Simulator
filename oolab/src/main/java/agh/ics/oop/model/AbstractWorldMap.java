package agh.ics.oop.model;

import agh.ics.oop.model.Exceptions.PositionAlreadyOccupiedException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap  {
/*
    protected final Map<Vector2d, SamePositionAnimals> animals = new HashMap<>();

    protected List<MapChangeListener> listeners = new ArrayList<>();

    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);

    protected final UUID id = UUID.randomUUID();

    public synchronized ArrayList<Animal> getAnimalsObj() {
        return animalsObj;
    }

    public ArrayList<Animal> animalsObj = new ArrayList<>();


    public void subscribe(MapChangeListener listener)
    {
        listeners.add(listener);
    }

    public void unSubscribe(MapChangeListener listener)
    {
        listeners.remove(listener);
    }

    protected void mapChanged(String message){
        for(MapChangeListener listener : listeners){
            listener.mapChanged(this,message);
        }
    }

    public synchronized void place(Animal animal) {
        //if (canMoveTo(animal.getPosition())) {
        addAnimalToMap(animal, animal.getPosition());
        mapChanged("animal placed : " + animal.getPosition());
        animalsObj.add(animal);
        //} else {
        //    throw new PositionAlreadyOccupiedException(animal.getPosition());
        //}

    }

    public synchronized void addAnimalToMap(Animal animal, Vector2d position){
        if(!animals.containsKey(position)){
            SamePositionAnimals samePositionAnimals = new SamePositionAnimals(position,animal,this);
            animals.put(position, samePositionAnimals);
        } else {
            animals.get(position).addAnimal(animal);
        }
    }

    /*
    public boolean canMoveTo(Vector2d position) {
        return !animals.containsKey(position);
    }



    public boolean isOccupied(Vector2d position) {
        return animals.containsKey((position));
    }



    public Collection<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>();
        for (SamePositionAnimals samePositionAnimals : animals.values()) {
            elements.addAll(samePositionAnimals.getAnimals());
        }
        return elements;
    }



    @Override
    public String toString() {
        Boundary boundary = getCurrentBounds();
        return mapVisualizer.draw(boundary.leftDown(),boundary.rightUp());
    }
*/
}
