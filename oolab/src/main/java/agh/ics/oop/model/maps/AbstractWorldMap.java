package agh.ics.oop.model.maps;
import agh.ics.oop.model.entity.Animal;
import agh.ics.oop.model.entity.WorldElement;
import agh.ics.oop.model.enums.FieldType;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.utils.*;

import java.util.*;
import java.util.Queue;

public abstract class AbstractWorldMap implements WorldMap {
    protected final Map<Vector2d, FieldType> fieldTypes = Collections.synchronizedMap(new HashMap<>());

    protected final Map<Vector2d, SamePositionAnimals> animals = Collections.synchronizedMap(new HashMap<>());

    protected final Map<Vector2d, WorldElement> grasses = Collections.synchronizedMap(new HashMap<>());

    private final Map<int[],Integer> mapGenotypes = Collections.synchronizedMap(new HashMap<>());

    protected List<Animal> animalsObj = Collections.synchronizedList(new ArrayList<>());

    protected final List<WorldElement> grassesObj = Collections.synchronizedList(new ArrayList<>());

    protected final Vector2d leftDownGrass;

    protected final Vector2d rightUpGrass;

    protected List<MapChangeListener> listeners = new ArrayList<>();
    private final Boolean isSpecialGen;

    public AbstractWorldMap(int width, int height, Boolean isSpecialGen) {
        this.isSpecialGen = isSpecialGen;
        leftDownGrass = new Vector2d(0,0);
        rightUpGrass = new Vector2d(width-1,height-1);
    }

    abstract public void makeGrassMap(int quantity);

    abstract protected void initializeFieldTypes();

    abstract public void move(Animal animal, int energyCost);

    public void addAnimalToMap(Animal animal, Vector2d position){
        if(!animals.containsKey(position)){
            SamePositionAnimals samePositionAnimals = new SamePositionAnimals(position,animal);
            animals.put(position, samePositionAnimals);
        } else {
            animals.get(position).addAnimal(animal);
        }
    }

    public void place(Animal animal) {
        addAnimalToMap(animal, animal.getPosition());
        animalsObj.add(animal);
        addGenotype(animal.getGenoType());
    }

    protected Vector2d generateNewPosition(Vector2d position, MapDirection direction) {
        Vector2d newPosition = position.add(direction.toUnitVector());
        if(isOutOfBounds(newPosition)){
            if(newPosition.getY() < leftDownGrass.getY() || newPosition.getY() > rightUpGrass.getY()){
                return position;
            } else if(newPosition.getX() < leftDownGrass.getX()) {
                return new Vector2d(rightUpGrass.getX(), newPosition.getY());
            } else  {
                return new Vector2d(leftDownGrass.getX(), newPosition.getY());
            }
        } else {
            return newPosition;
        }
    }

    private boolean isOutOfBounds(Vector2d newPosition) {
        return !(newPosition.follows(leftDownGrass) && newPosition.precedes(rightUpGrass));
    }

    public boolean isMapFullOfGrass(){
        return grasses.size() == (rightUpGrass.getX() + 1) * (rightUpGrass.getY() + 1);
    }

    public ArrayList<Vector2d> freeJunglePosition(){
        ArrayList<Vector2d> free = new ArrayList<>();

        for (Map.Entry<Vector2d, FieldType> entry : fieldTypes.entrySet()) {
            Vector2d position = entry.getKey();
            if (entry.getValue() == FieldType.PREFERRED && !grasses.containsKey(position)) {
                free.add(position);
            }
        }
        return free;
    }

    public ArrayList<Vector2d> freeStepPosition(){
        ArrayList<Vector2d> free = new ArrayList<>();

        for (Map.Entry<Vector2d, FieldType> entry : fieldTypes.entrySet()) {
            Vector2d position = entry.getKey();
            if (entry.getValue() == FieldType.UNATTRACTIVE && !grasses.containsKey(position)) {
                free.add(position);
            }
        }
        return free;
    }

    public ArrayList<Vector2d> freePosition(){
        ArrayList<Vector2d> free = new ArrayList<>();

        for (Map.Entry<Vector2d, FieldType> entry : fieldTypes.entrySet()) {
            Vector2d position = entry.getKey();
            if (!grasses.containsKey(position) && !animals.containsKey(position)) {
                free.add(position);
            }
        }
        return free;
    }

    public synchronized WorldElement objectAt(Vector2d position) {
        List<Animal> copyOfAnimals = new ArrayList<>(animalsObj);
        for (Animal animal : copyOfAnimals) {
            if (animal.getPosition().equals(position)) {
                SamePositionAnimals samePositionAnimals = animals.get(animal.getPosition());
                if(samePositionAnimals != null){
                    SamePositionAnimals animalsOnPosition = animals.get(animal.getPosition());
                    return animalsOnPosition.getRandomStrongest();
                }
                return null;
            }
        }

        List<WorldElement> copyOfGrasses = new ArrayList<>(grassesObj);
        for (WorldElement grass : copyOfGrasses) {
            if (grass.getPosition().equals(position)) {
                return grass;
            }
        }
        return null;
    }

    public void eatGrassByAnimals(int energy) {
        List<Vector2d> grassPositions = new ArrayList<>(grasses.keySet());

        for (Vector2d position : grassPositions) {
            SamePositionAnimals samePositionAnimals = animals.get(position);
            WorldElement grassAtPosition = grasses.get(position);

            if (samePositionAnimals != null && grassAtPosition != null && !samePositionAnimals.getAnimals().isEmpty()) {
                List<Animal> animalsAtPosition = samePositionAnimals.getAnimals();
                if (animalsAtPosition.size() >= 1) {
                    Animal strongestAnimal = samePositionAnimals.getRandomStrongest();
                    grasses.remove(position);
                    grassesObj.remove(grassAtPosition);
                    strongestAnimal.animalEat(energy,grassAtPosition);
                }
            }
        }
    }
    public void removeAnimalFromMap(Animal animalToRemove) {
        Vector2d position = animalToRemove.getPosition();
        SamePositionAnimals samePositionAnimals = animals.get(position);
        if (samePositionAnimals != null) {
            samePositionAnimals.removeAnimal(animalToRemove);
            if(samePositionAnimals.isEmpty()){
                animals.remove(position);
            }
            removeDeadAnimalsFromList(animalToRemove);
        }
    }

    public void removeDeadAnimalsFromList(Animal animalToRemove) {
        animalsObj.remove(animalToRemove);
    }

    public void reproduce(int genNumber, int minMutations, int maxMutations, int reproduceCost, int energyRequired) {
        List<Vector2d> animalsPositions = new ArrayList<>(animals.keySet());

        for (Vector2d position : animalsPositions) {
            SamePositionAnimals samePositionAnimals = animals.get(position);

            if (samePositionAnimals != null && !samePositionAnimals.getAnimals().isEmpty()) {
                List<Animal> animalsAtPosition = samePositionAnimals.getAnimals();

                if (animalsAtPosition.size() > 1) {
                    animalsAtPosition = samePositionAnimals.findTwoStrongestAnimals();
                    Animal animal1 = animalsAtPosition.get(0);
                    Animal animal2 = animalsAtPosition.get(1);
                    if(animal1.getCurrentEnergy() >= energyRequired && animal2.getCurrentEnergy() >= energyRequired) {
                        int[] childGenType = GenoType.combineGenoType(genNumber, animal1, animal2, minMutations, maxMutations);
                        addGenotype(childGenType);
                        animal1.animalNewChild();
                        animal2.animalNewChild();
                        animal1.animalReproduceEnergyLost(reproduceCost);
                        animal2.animalReproduceEnergyLost(reproduceCost);
                        Animal child = new Animal(animal1.getPosition(), childGenType, 2 * reproduceCost, isSpecialGen, animal1, animal2 );
                        place(child);
                        descendantsUpdate(child);
                    }
                }
            }
        }
    }

    private void descendantsUpdate(Animal child) {
        List<Animal> visitedAnimals = new ArrayList<>();
        Queue<Animal> queue = new LinkedList<>();
        queue.add(child.getParent1());
        queue.add(child.getParent2());
        child.getParent1().addNewDescendant();
        child.getParent2().addNewDescendant();
        visitedAnimals.add(child.getParent1());
        visitedAnimals.add(child.getParent2());

        while(!queue.isEmpty()){
            Animal animal = queue.poll();

            if(animal.getParent1()!=null && !visitedAnimals.contains(animal.getParent1())){
                queue.add(animal.getParent1());
                animal.getParent1().addNewDescendant();
                visitedAnimals.add(animal.getParent1());
            }

            if(animal.getParent2()!=null && !visitedAnimals.contains(animal.getParent2())){
                queue.add(animal.getParent2());
                animal.getParent2().addNewDescendant();
                visitedAnimals.add(animal.getParent2());
            }
        }

    }

    private void addGenotype(int[] newGenotype) {
        for (Map.Entry<int[],Integer> entry: mapGenotypes.entrySet()) {
            int[] key = entry.getKey();
            Integer value = entry.getValue();

            if (Arrays.equals(key, newGenotype)) {
                mapGenotypes.put(key,value+1);
                mapGenotypes.remove(key,value);
                return;
            }
        }
        mapGenotypes.put(newGenotype,1);
    }

    public void subscribe(MapChangeListener listener)
    {
        listeners.add(listener);
    }

    public void unSubscribe(MapChangeListener listener)
    {
        listeners.remove(listener);
    }

    protected void mapChanged(){
        for(MapChangeListener listener : listeners){
            listener.mapChanged(this);
        }
    }

    public void updateDrawMap(){
        mapChanged();
    }

    // getters

    public Boundary getCurrentBounds() {
        return new Boundary(leftDownGrass,rightUpGrass);
    }

    public List<Animal> getAnimalsObj() {
        return animalsObj;
    }

    public Map<int[], Integer> getMapGenotypes() {
        return mapGenotypes;
    }

    public List<WorldElement> getGrassesObj() {
        return grassesObj;
    }

    public FieldType getFieldType(Vector2d positionToCheck){
        return fieldTypes.get(positionToCheck);
    }


}
