package agh.ics.oop.model;
import java.util.*;
import static agh.ics.oop.model.MapDirection.randomDirection;
public class PoisonGrassField extends AbstractWorldMap{

    public PoisonGrassField(int grassFilesQuantity, int width, int height, Boolean isSpecialGen) {
        super(width, height, isSpecialGen);
        initializeFieldTypes();
        makeGrassMap(grassFilesQuantity);
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
            boolean isNewFieldPrefered = false;
            if(grassProbability <= 0.8 && !preferredPositions.isEmpty() && !unattractivePositions.isEmpty()) {
                newPosition = preferredPositions.remove(0);
                isNewFieldPrefered = true;
            } else if(grassProbability > 0.8 && !preferredPositions.isEmpty() && !unattractivePositions.isEmpty()) {
                newPosition = unattractivePositions.remove(0);
            } else if(!unattractivePositions.isEmpty()) {
                newPosition = unattractivePositions.remove(0);
            } else {
                newPosition = preferredPositions.remove(0);
                isNewFieldPrefered = true;
            }

            if(isNewFieldPrefered){
                Random random = new Random();
                double chance = random.nextDouble();

                if(chance < 0.25) {
                    BadGrass newGrass = new BadGrass(newPosition);
                    grasses.put(newPosition, newGrass);
                    grassesObj.add(newGrass);
                } else {
                    Grass newGrass = new Grass(newPosition);
                    grasses.put(newPosition, newGrass);
                    grassesObj.add(newGrass);
                }
            } else {
                Grass newGrass = new Grass(newPosition);
                grasses.put(newPosition, newGrass);
                grassesObj.add(newGrass);
            }
            addedGrass++;
        }
    }

    protected void initializeFieldTypes() {
        Random random = new Random(); // co wywołanie?
        double targetPreferredFieldsRatio = 0.2;

        int totalFields = (rightUpGrass.getX() - leftDownGrass.getX() + 1) * (rightUpGrass.getY() - leftDownGrass.getY() + 1);
        int targetPreferredFields = (int) (totalFields * targetPreferredFieldsRatio);

        int sideLength = (int) Math.sqrt(targetPreferredFields);
        int startX = leftDownGrass.getX() + random.nextInt(rightUpGrass.getX() - sideLength + 1);
        int startY = leftDownGrass.getY() + random.nextInt(rightUpGrass.getY() - sideLength + 1);

        for (int x = startX; x < startX + sideLength; x++) {
            for (int y = startY; y < startY + sideLength; y++) {
                Vector2d position = new Vector2d(x, y);
                fieldTypes.put(position, FieldType.PREFERRED);
            }
        }

        for (int x = leftDownGrass.getX(); x <= rightUpGrass.getX(); x++) {
            for (int y = leftDownGrass.getY(); y <= rightUpGrass.getY(); y++) {
                Vector2d position = new Vector2d(x, y);
                if (!fieldTypes.containsKey(position)) {
                    fieldTypes.put(position, FieldType.UNATTRACTIVE);
                }
            }
        }
    }

    public void move(Animal animal, int energyCost) {
        MapDirection newDirection= animal.getNewDirection();
        Vector2d oldPosition = animal.getPosition();
        Vector2d newPosition = super.generateNewPosition(oldPosition,newDirection);

        if (grasses.get(newPosition) != null && grasses.get(newPosition) instanceof BadGrass) {
            if (dashBadGrass()) {
                Vector2d newPosition2;
                do {
                    newPosition2 = randomDirection().toUnitVector().add(oldPosition);
                    if (newPosition2.equals(newPosition)) {
                        newPosition2 = randomDirection().toUnitVector().add(oldPosition);
                    }
                } while (newPosition2.equals(newPosition));
                newPosition = newPosition2;
            }
        }

        animal.move(energyCost, newPosition);
        if (!oldPosition.equals(animal.getPosition())) {
            SamePositionAnimals oldPositionAnimals = animals.get(oldPosition);
            if (oldPositionAnimals != null) {
                oldPositionAnimals.removeAnimal(animal);
                if (oldPositionAnimals.isEmpty()) {
                    animals.remove(oldPosition);
                }
            }
            SamePositionAnimals newPositionAnimals = animals.getOrDefault(animal.getPosition(), new SamePositionAnimals(animal.getPosition(), animal));
            newPositionAnimals.addAnimal(animal);
            animals.put(animal.getPosition(), newPositionAnimals);
        }
    }

    private boolean dashBadGrass() {
        Random random = new Random(); // co wywołanie?
        double chance = random.nextDouble();
        return chance < 0.2;
    }

}
