package agh.ics.oop.model;

import java.util.*;

public class GrassField extends AbstractWorldMap{
    private final Map<Vector2d,Grass> grasses = new HashMap<>();
    private Vector2d leftDownGrass = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
    private Vector2d rightUpGrass = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
    private Vector2d leftDownDynamic;
    private Vector2d rightUpDynamic;
    public GrassField(int grassFilesQuantity) {
        makeGrassMap(grassFilesQuantity); //  metoda generujaca trawe na polach
    }

    private void makeGrassMap(int quantity){

        //dla testow

        /*
        if(quantity == -777)
        {
            Grass newGrass0 = new Grass(new Vector2d(0,0));
            Grass newGrass1 = new Grass(new Vector2d(2,3));
            Grass newGrass2 = new Grass(new Vector2d(5,5));
            Grass newGrass3 = new Grass(new Vector2d(2,1));
            grasses.put(newGrass0.getPosition(),newGrass0);
            leftDownGrass = leftDownGrass.lowerLeft(newGrass0.getPosition());
            rightUpGrass = rightUpGrass.upperRight(newGrass0.getPosition());
            grasses.put(newGrass2.getPosition(),newGrass2);
            leftDownGrass = leftDownGrass.lowerLeft(newGrass2.getPosition());
            rightUpGrass = rightUpGrass.upperRight(newGrass2.getPosition());
            grasses.put(newGrass1.getPosition(),newGrass0);
            grasses.put(newGrass3.getPosition(),newGrass0);
        }
        */


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
    private void generateDynamicSize()
    {
        leftDownDynamic = leftDownGrass;
        rightUpDynamic = rightUpGrass;

        for(Map.Entry<Vector2d,Animal> entry : animals.entrySet()){
            Vector2d animPosition = entry.getKey();
            leftDownDynamic = leftDownDynamic.lowerLeft(animPosition);
            rightUpDynamic = rightUpDynamic.upperRight(animPosition);
        }
    }
    @Override
    public Collection<WorldElement> getElements() {
        Collection<WorldElement> allElements = new ArrayList<>(super.getElements());
        allElements.addAll(grasses.values());
        return allElements;
    }

    @Override
    public Boundary getCurrentBounds() {
        generateDynamicSize();
        return new Boundary(leftDownDynamic,rightUpDynamic);
    }

    @Override
    public UUID getID() {
        return id;
    }
}
