package agh.ics.oop.model;
import java.util.Random;

public class Animal implements WorldElement{
    private static int nextId = 0; // zmienna wspoldzielona (static)
    private final int id;
    private final int[] genoType;
    public int whichGenIsActive;
    private Vector2d position;
    private MapDirection currentOrientation;
    private int currentEnergy;
    private Animal parent1 = null;
    private Animal parent2 = null;
    private int dayAlive = 0;
    public int grassEatenCounter = 0;
    private int childNumber = 0;
    private int descendantsNumber = 0;
    public int deathDay = -1;
    private final Random random = new Random();
    private double randomFactor = 1.0;

    public Animal(Vector2d position, int[] genoType, int currentEnergy, Boolean madness)
    {
        this.id = nextId;
        nextId++;
        Random random = new Random();
        this.position = position;
        this.currentOrientation = MapDirection.NORTH;
        this.genoType = genoType;
        this.currentEnergy = currentEnergy;
        this.whichGenIsActive = random.nextInt(genoType.length-1);
        if(madness){
            addSomeMadness();
        }
    }
    public Animal(Vector2d position, int[] genoType, int currentEnergy, Boolean madness, Animal parent1, Animal parent2)
    {
        this(position,genoType,currentEnergy,madness);
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    protected MapDirection getNewDirection(){
        int newDirectionId = (currentOrientation.directionToId() + genoType[whichGenIsActive % genoType.length])%8;
        currentOrientation = currentOrientation.idToDirection(newDirectionId);
        return currentOrientation;
    }

    public void move(int energyCost,Vector2d newPosition ){
        if(this.position == newPosition){
            currentOrientation = currentOrientation.getReverse(currentOrientation.directionToId());
        } else {
            this.position = newPosition;
        }
        currentEnergy += energyCost;
        dayAlive++;

        if (random.nextDouble() <= randomFactor) {
            whichGenIsActive++;
            whichGenIsActive = whichGenIsActive %genoType.length;
        } else {
            whichGenIsActive = random.nextInt(genoType.length - 1); // Aktywacja losowego genu
        }
    }

    public void animalEat(int energy, WorldElement grass){
        if (grass instanceof Grass) {
            currentEnergy += energy;
        } else if (grass instanceof BadGrass) {
            currentEnergy -= energy;
        }
        grassEatenCounter++;
    }
    public void animalReproduceEnergyLost(int energy){
        currentEnergy -= energy;
    }
    public void animalNewChild(){
        childNumber ++;
    }
    public void setDeathDay(int day){
        if(currentEnergy < 0){
            deathDay = day;
        }
    }
    public void addNewDescendant(){
        descendantsNumber ++;
    }
    public void addSomeMadness() { randomFactor = 0.8; } // Ustawienie 80% szans na standardową aktywację, 20% szans na losowy gen (dodatek 3)

    // Gettters
    public int getDescendantsNumber() { return descendantsNumber; }
    public Animal getParent1() { return parent1; }
    public Animal getParent2() { return parent2; }
    public int[] getGenoType() { return genoType; }
    public int getWhichGenIsActive() { return whichGenIsActive; }
    public int getGrassEatenCounter() { return grassEatenCounter; }
    public int getCurrentEnergy() {return currentEnergy;}
    public int getDayAlive() { return dayAlive; }
    public int getChildNumber() { return childNumber; }
    public int getDeathDay() { return deathDay; }
    public int getId() { return id; }
    public Vector2d getPosition() { return position; }

}
