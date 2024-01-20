package agh.ics.oop.model;

import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SHOUT_WEST,
    WEST,
    NORTH_WEST;

    public MapDirection next()
    {
        int nextOne = (this.ordinal() + 1) % values().length;
        return values()[nextOne];
    }

    public MapDirection previous()
    {
        int nextOne = (values().length + this.ordinal() - 1) % values().length; // ekstra obrot by nie wyjsc na -
        return values()[nextOne];
    }

    public Vector2d toUnitVector()
    {
        return switch(this)
        {
            case NORTH -> new Vector2d(0,1);
            case NORTH_EAST -> new Vector2d(1,1);
            case EAST -> new Vector2d(1,0);
            case SOUTH_EAST -> new Vector2d(1,-1);
            case SOUTH -> new Vector2d(0,-1);
            case SHOUT_WEST -> new Vector2d(-1,-1);
            case WEST -> new Vector2d(-1,0);
            case NORTH_WEST -> new Vector2d(-1,1);
        };
    }

    public int directionToId()
    {
        return switch(this)
        {
            case NORTH -> 0;
            case NORTH_EAST -> 1;
            case EAST -> 2;
            case SOUTH_EAST -> 3;
            case SOUTH -> 4;
            case SHOUT_WEST -> 5;
            case WEST -> 6;
            case NORTH_WEST -> 7;
        };
    }

    public MapDirection idToDirection(int directionId)
    {
        return switch(directionId)
        {
            case 0 -> NORTH;
            case 1 -> NORTH_EAST;
            case 2 -> EAST;
            case 3 -> SOUTH_EAST;
            case 4 -> SOUTH;
            case 5 -> SHOUT_WEST;
            case 6 -> WEST;
            case 7 -> NORTH_WEST;
            default -> throw new IllegalStateException("Unexpected value: " + directionId);
        };
    }

    public static MapDirection randomDirection() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }

    public MapDirection getReverse(int directionId){
        return idToDirection((directionId+4) % 8);
    }

}
