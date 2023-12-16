package agh.ics.oop.model;

public class Animal implements WorldElement{
    private MapDirection currentOrientation;
    private Vector2d position;
    public Animal()
    {
        this.position = new Vector2d(2,2);
        this.currentOrientation =  MapDirection.NORTH;
    }

    public Animal(Vector2d position)
    {
        this.position = position;
        this.currentOrientation = MapDirection.NORTH;
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

    public void move(MoveDirection direction, MoveValidator validator){
        if(direction == MoveDirection.RIGHT){
            currentOrientation = currentOrientation.next();
        }else if(direction == MoveDirection.LEFT){
            currentOrientation = currentOrientation.previous();
        }else if(direction == MoveDirection.FORWARD){
            Vector2d newPosition = this.position.add(currentOrientation.toUnitVector());
            if(validator.canMoveTo(newPosition)){
                this.position = newPosition;
            }
        } else if (direction == MoveDirection.BACKWARD) {
            Vector2d newPosition = this.position.subtract(currentOrientation.toUnitVector());
            if(validator.canMoveTo(newPosition)){
                this.position = newPosition;
            }
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
