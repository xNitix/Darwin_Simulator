package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public class GrassField {
    private final Map<Vector2d, FieldType> fieldTypes = new HashMap<>();

    protected final Map<Vector2d, SamePositionAnimals> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = Collections.synchronizedMap(new HashMap<>());

    public ArrayList<Animal> animalsObj = new ArrayList<>();
    private final List<Grass> grassesObj = Collections.synchronizedList(new ArrayList<>());

    public Map<Vector2d, Grass> getGrasses() {
        return grasses;
    }
    private final Vector2d leftDownGrass;
    private final Vector2d rightUpGrass;

    protected List<MapChangeListener> listeners = new ArrayList<>();

    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);

    protected final UUID id = UUID.randomUUID();

    public synchronized ArrayList<Animal> getAnimalsObj() {
        return animalsObj;
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

     */

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey((position)) || grasses.containsKey(position);
    }



    public Collection<WorldElement> getElements() {
        List<WorldElement> elements = new ArrayList<>();
        for (SamePositionAnimals samePositionAnimals : animals.values()) {
            elements.addAll(samePositionAnimals.getAnimals());
        }

        Collection<WorldElement> allElements = new ArrayList<>(elements);
        allElements.addAll(grasses.values());
        return allElements;

    }



    @Override
    public String toString() {
        Boundary boundary = getCurrentBounds();
        return mapVisualizer.draw(boundary.leftDown(),boundary.rightUp());
    }

    public GrassField(int grassFilesQuantity, int width, int height) {
        leftDownGrass = new Vector2d(0,0);
        rightUpGrass = new Vector2d(width-1,height-1);
        initializeFieldTypes();
        makeGrassMap(grassFilesQuantity);
    }

    private void initializeFieldTypes() {
        Random random = new Random();
        double targetPreferredFieldsRatio = 0.2;
        int totalFields = (rightUpGrass.getX() - leftDownGrass.getX() + 1) * (rightUpGrass.getY() - leftDownGrass.getY() + 1);
        int targetPreferredFields = (int) (totalFields * targetPreferredFieldsRatio);

        List<Vector2d> preferredFields = new ArrayList<>();

        for (int x = leftDownGrass.getX(); x <= rightUpGrass.getX(); x++) {
            for (int y = leftDownGrass.getY(); y <= rightUpGrass.getY(); y++) {
                if(random.nextDouble() < getPreferredProbability(y)){
                    preferredFields.add(new Vector2d(x,y));
                }
            }
        }

        while (preferredFields.size() > targetPreferredFields) {
            int indexToRemove = random.nextInt(preferredFields.size());
            preferredFields.remove(indexToRemove);
        }

        for (int x = leftDownGrass.getX(); x <= rightUpGrass.getX(); x++) {
            for (int y = leftDownGrass.getY(); y <= rightUpGrass.getY(); y++) {
                int added = 0;
                for (Vector2d preferredField : preferredFields) {
                    if (new Vector2d(x, y).equals(preferredField)) {
                        fieldTypes.put(preferredField, FieldType.PREFERRED);
                        added++;
                    }
                }
                if(added == 0){
                    fieldTypes.put(new Vector2d(x,y),FieldType.UNATTRACTIVE);
                }

            }
        }
    }

    private double getPreferredProbability(int row) {
        double middleRow = rightUpGrass.getX() / 2.0;
        double distanceFromMiddle = Math.abs(middleRow - row);
        double normalizedDistance = distanceFromMiddle / middleRow;
        double standardDeviation = 0.2;
        return Math.exp(-Math.pow(normalizedDistance, 2) / (2 * Math.pow(standardDeviation, 2)));
    }


    public void makeGrassMap(int quantity){
        int addedGrass = 0;
        List<Vector2d> preferredPositions = freeJunglePosition();
        Collections.shuffle(preferredPositions);
        List<Vector2d> unattractivePositions = freeStepPosition();
        Collections.shuffle(unattractivePositions);

        while(addedGrass < quantity && !isMapFullOfGrass() && (!preferredPositions.isEmpty() || !unattractivePositions.isEmpty())) {
            double grassProbability = Math.random();

            Vector2d newPosition;
            if(grassProbability <= 0.8 && !preferredPositions.isEmpty() && !unattractivePositions.isEmpty()) {
                newPosition = preferredPositions.remove(0);
            } else if(grassProbability > 0.8 && !preferredPositions.isEmpty() && !unattractivePositions.isEmpty()) {
                newPosition = unattractivePositions.remove(0);
            } else if(!unattractivePositions.isEmpty()) {
                newPosition = unattractivePositions.remove(0);
            } else {
                newPosition = preferredPositions.remove(0);
            }

            Grass newGrass = new Grass(newPosition);
            grasses.put(newPosition, newGrass);
            grassesObj.add(newGrass);
            addedGrass++;
        }
    }


    public Boundary getCurrentBounds() {
        return new Boundary(leftDownGrass,rightUpGrass);
    }

    public UUID getID() {
        return id;
    }

    public synchronized void move(Animal animal, int energyCost) {

       // System.out.println(Arrays.toString(animal.getGenoType()));
        MapDirection newDirection= animal.getNewDirection();
        Vector2d oldPosition = animal.getPosition();
        Vector2d newPosition = generateNewPosition(oldPosition,newDirection);

        animal.move(energyCost, newPosition);
        if (!oldPosition.equals(animal.getPosition())) {
            SamePositionAnimals oldPositionAnimals = animals.get(oldPosition);
            if (oldPositionAnimals != null) {
                oldPositionAnimals.removeAnimal(animal);
                if (oldPositionAnimals.isEmpty()) {
                    animals.remove(oldPosition);
                }
            }

            SamePositionAnimals newPositionAnimals = animals.getOrDefault(animal.getPosition(), new SamePositionAnimals(animal.getPosition(), animal, this));
            newPositionAnimals.addAnimal(animal);
            animals.put(animal.getPosition(), newPositionAnimals);
            mapChanged("animal moved from : " + oldPosition + " to : " + animal.getPosition());
        }else{
            mapChanged("animal in position: " + oldPosition + " changed its orientation to: " + animal.getCurrentOrientation());
        }
    }

    private Vector2d generateNewPosition(Vector2d position, MapDirection direction) {
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

    public FieldType getFieldType(Vector2d positionToCheck){
        return fieldTypes.get(positionToCheck);
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

    public WorldElement objectAt(Vector2d position) {
        List<Animal> copyOfAnimals = new ArrayList<>(animalsObj);
        for (Animal animal : copyOfAnimals) {
            if (animal.getPosition().equals(position)) {
//              returns strongest animal

                SamePositionAnimals samePositionAnimals = animals.get(animal.getPosition());
                if(samePositionAnimals != null){
                    List <Animal> strongestAnimals = animals.get(animal.getPosition()).findStrongestAnimals();
                    Random random = new Random();
                    int randomIdx = random.nextInt(strongestAnimals.size());
                    return strongestAnimals.get(randomIdx);
                }
                return null;
            }
        }

        List<Grass> copyOfGrasses = new ArrayList<>(grassesObj);
        for (Grass grass : copyOfGrasses) {
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
            Grass grassAtPosition = grasses.get(position);

            if (samePositionAnimals != null && grassAtPosition != null && !samePositionAnimals.getAnimals().isEmpty()) {
                List<Animal> animalsAtPosition = samePositionAnimals.getAnimals();
                if (animalsAtPosition.size() >= 1) {
                    Animal strongestAnimal = samePositionAnimals.getRandomStrongest();
                    grasses.remove(position);
                    grassesObj.remove(grassAtPosition);
                    strongestAnimal.animalEat(energy);
                }
            }
        }
        mapChanged("zjedzono rosline");
    }

    public synchronized void removeAnimalFromMap(Animal animalToRemove) {
        Vector2d position = animalToRemove.getPosition();
        SamePositionAnimals samePositionAnimals = animals.get(position);
        if (samePositionAnimals != null) {
            samePositionAnimals.removeAnimal(animalToRemove);
            animals.remove(position);
            removeDeadAnimalsFromList();
        }
    }

    public synchronized void removeDeadAnimalsFromList() {
        animalsObj.removeIf(animal -> animal.getCurrentEnergy() < 0);
    }

    public synchronized List<Animal> reproduce(int genNumber, int minMutations, int maxMutations, int reproduceCost, int energyRequired) {
        //System.out.println("map");
        List<Vector2d> animalsPositions = new ArrayList<>(animals.keySet());
        List<Animal> childs = new ArrayList<>();

        for (Vector2d position : animalsPositions) {
            //System.out.println("for");
            SamePositionAnimals samePositionAnimals = animals.get(position);

            if (samePositionAnimals != null && !samePositionAnimals.getAnimals().isEmpty()) {
                //System.out.println("if1");
                List<Animal> animalsAtPosition = samePositionAnimals.getAnimals();
                //System.out.println(animalsAtPosition.size() + "fff");

                if (animalsAtPosition.size() > 1) {

                    //System.out.println("if3");
                    animalsAtPosition = samePositionAnimals.findTwoStrongestAnimals();
                    //System.out.println("if6");
                    Animal animal1 = animalsAtPosition.get(0);
                    Animal animal2 = animalsAtPosition.get(1);
                    //System.out.println("if7");
                    if(animal1.getCurrentEnergy() >= energyRequired && animal2.getCurrentEnergy() >= energyRequired) {
                        //System.out.println("if4");
                        int[] childGenType = GenoType.combineGenoType(genNumber, animal1, animal2, minMutations, maxMutations);
                        //System.out.println("if5");
                        animal1.animalEat(-reproduceCost);
                        animal2.animalEat(-reproduceCost);
                        //System.out.println("if10");
                        Animal child = new Animal(animal1.getPosition(), childGenType, 2 * reproduceCost, this);
                        //System.out.println("if11");
                        place(child);
                        //System.out.println("if12");
                        System.out.println(animalsObj.size() + "przed rozmnozeniem");
                        animalsObj.add(child);
                        System.out.println(animalsObj.size() + "po rozmnozeniu");
                        childs.add(child);
                        //System.out.println("if13");
                    }
                }
            }
        }
        return childs;
    }

}
