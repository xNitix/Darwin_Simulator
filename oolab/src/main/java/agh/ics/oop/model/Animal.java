package agh.ics.oop.model;

import java.util.Random;

public class Animal implements WorldElement{
    private MapDirection currentOrientation;
    private Vector2d position;

    private double randomFactor = 1.0;
    private Random random = new Random();

    public int[] getGenoType() {
        return genoType;
    }
    private final int[] genoType;

    public int getWhichGen() {
        return whichGen;
    }

    public int whichGen;

    public int getGrassEatenCounter() {
        return grassEatenCounter;
    }

    public int grassEatenCounter = 0;
    private final int startEnergy;

    private final GrassField map;

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    private int currentEnergy;

    public int getDayAlive() {
        return dayAlive;
    }

    private int dayAlive = 0;

    public int getChildNumber() {
        return childNumber;
    }

    private int childNumber = 0;

    private Boolean madness;

    public int getDeathDay() {
        return deathDay;
    }

    public int deathDay = -1;

    private static int nextId = 0; // zmienna wspoldzielona (static)

    public int getId() {
        return id;
    }

    private final int id;


    public Animal(Vector2d position, int[] genoType, int currentEnergy, GrassField map, Boolean madness)
    {
        this.madness = madness;
        this.id = nextId;
        nextId++;
        Random random = new Random();
        this.position = position;
        this.currentOrientation = MapDirection.NORTH;
        this.genoType = genoType;
        this.currentEnergy = currentEnergy;
        this.startEnergy = currentEnergy;
        this.map = map;
        this.whichGen = random.nextInt(genoType.length-1);
        if(madness){
            addSomeMadness();
            System.out.println("dodano madness");
        }
    }


    public MapDirection getCurrentOrientation() {
        return currentOrientation;
    }

    public Vector2d getPosition() {
        return position;
    }

    public boolean isAt(Vector2d position){
        return this.position.equals(position);
    }

    public MapDirection getNewDirection(){
        int newDirectionId = (currentOrientation.directionToId() + genoType[whichGen % genoType.length])%8;
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
        //System.out.println(getCurrentEnergy());

        if (random.nextDouble() <= randomFactor) {
            whichGen++;
            whichGen = whichGen%genoType.length;
        } else {
            whichGen = random.nextInt(genoType.length - 1); // Aktywacja losowego genu
        }


        /*
            if(validator.canMoveTo(newPosition)){
                this.position = newPosition;
            }

         */
    }

    public void addSomeMadness() {
        randomFactor = 0.8; // Ustawienie 80% szans na standardową aktywację, 20% szans na losowy gen
    }

    public void animalEat(int energy, WorldElement grass){
        if (grass instanceof Grass) {
            currentEnergy += energy;
        } else if (grass instanceof BadGrass) {
            currentEnergy -= energy;
        }
        grassEatenCounter++;
    }

    public void animalReprodueEnergyLost(int energy){
        currentEnergy -= energy;
    }

    public synchronized void animalNewChild(){
        childNumber ++;
    }

    public void setDeathDay(int day){
        if(currentEnergy < 0){
            deathDay = day;
        }
    }



    public String toString() {
        return switch (this.currentOrientation)
        {
            case NORTH -> "^";
            case NORTH_EAST -> "NE";
            case EAST ->  ">";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "v";
            case SHOUT_WEST -> "SW";
            case WEST -> "<";
            case NORTH_WEST -> "NW";
        };
    }

}
