package agh.ics.oop.model;

import agh.ics.oop.model.Exceptions.PositionAlreadyOccupiedException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, Animal> animals = new HashMap<>();

    protected List<MapChangeListener> listeners = new ArrayList<>();

    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);

    protected final UUID id = UUID.randomUUID();

    public void move(Animal animal, MoveDirection direction) {
        Vector2d oldPosition = animal.getPosition();
        animal.move(direction, this);
        if (!oldPosition.equals(animal.getPosition())) {
            animals.remove(oldPosition);
            animals.put(animal.getPosition(), animal);
            mapChanged("animal moved from : " + oldPosition + " to : " + animal.getPosition());
        }else{
            mapChanged("animal in position: " + oldPosition + " changed its orientation to: " + animal.getCurrentOrientation());
        }
    }

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

    public void place(Animal animal) throws PositionAlreadyOccupiedException {
        if (canMoveTo(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
            mapChanged("animal placed : " + animal.getPosition());
        } else {
            throw new PositionAlreadyOccupiedException(animal.getPosition());
        }

    }

    public boolean canMoveTo(Vector2d position) {
        return !animals.containsKey(position);
    }

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey((position));
    }

    public WorldElement objectAt(Vector2d position) {
        
        return this.animals.get(position);
    }

    public Collection<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
    }

    @Override
    public String toString() {
        Boundary boundary = getCurrentBounds();
        return mapVisualizer.draw(boundary.leftDown(),boundary.rightUp());
    }

}
