package agh.ics.oop.model;

public class Animal implements WorldElement{
    private MapDirection currentOrientation;
    private Vector2d position;

    public int[] getGenoType() {
        return genoType;
    }

    private final int[] genoType;
    private int wchichGen = 0;
    private final int startEnergy;

    private final WorldMap map;

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    private int currentEnergy;

    public Animal(Vector2d position, int[] genoType, int currentEnergy, WorldMap map)
    {
        this.position = position;
        this.currentOrientation = MapDirection.NORTH;
        this.genoType = genoType;
        this.currentEnergy = currentEnergy;
        this.startEnergy = currentEnergy;
        this.map = map;
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
        int newDirectionId = (currentOrientation.directionToId() + genoType[wchichGen%genoType.length])%8;
        currentOrientation = currentOrientation.idToDirection(newDirectionId);
        return currentOrientation;
    }

    public void move(int eneryCost,Vector2d newPosition ){
        if(this.position == newPosition){
            currentOrientation = currentOrientation.getReverse(currentOrientation.directionToId());
        } else {
            this.position = newPosition;
            currentEnergy += eneryCost;
        }
        wchichGen++;
        /*
            if(validator.canMoveTo(newPosition)){
                this.position = newPosition;
            }

         */
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
