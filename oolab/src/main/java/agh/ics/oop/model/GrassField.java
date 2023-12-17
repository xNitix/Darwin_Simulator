package agh.ics.oop.model;

import java.util.*;

public class GrassField extends AbstractWorldMap{
    private final Map<Vector2d,Grass> grasses = new HashMap<>();
    private Vector2d leftDownGrass;
    private Vector2d rightUpGrass;

    public GrassField(int grassFilesQuantity, int width, int height) {
        leftDownGrass = new Vector2d(0,0);
        rightUpGrass = new Vector2d(width-1,height-1);
        makeGrassMap(grassFilesQuantity); //  metoda generujaca trawe na polach
    }

    private void makeGrassMap(int quantity){
        if(quantity >= 0) {
            int maxGrassPosition = (int) Math.sqrt(quantity * 10);
            int putCounter = 0;
            while (putCounter < quantity) {
                int x = (int) (Math.random() * maxGrassPosition); // <0,1> *
                int y = (int) (Math.random() * maxGrassPosition);
                Grass newGrass = new Grass(new Vector2d(x, y));

                if (!grasses.containsKey(newGrass.getPosition())) {
                    grasses.put(newGrass.getPosition(), newGrass);
                    leftDownGrass = leftDownGrass.lowerLeft(newGrass.getPosition());
                    rightUpGrass = rightUpGrass.upperRight(newGrass.getPosition());
                    putCounter++;
                }
            }
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || grasses.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if(super.objectAt(position) != null){
            return super.objectAt(position);
        }
        return grasses.getOrDefault(position,null);
    }
    @Override
    public Collection<WorldElement> getElements() {
        Collection<WorldElement> allElements = new ArrayList<>(super.getElements());
        allElements.addAll(grasses.values());
        return allElements;
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(leftDownGrass,rightUpGrass);
    }
    @Override
    public UUID getID() {
        return id;
    }
}
