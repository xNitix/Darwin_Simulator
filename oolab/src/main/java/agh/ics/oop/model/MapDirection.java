package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SHOUT_WEST,
    WEST,
    NORTH_WEST;

    public String toString()
    {
        return switch(this)
        {
            case NORTH_EAST -> "Polnocny wschod";
            case EAST -> "Wschod";
            case WEST -> "Zachod";
            case SOUTH_EAST -> "Poludniowy wschod";
            case SOUTH ->  "Poludnie";
            case NORTH -> "Polnoc";
            case SHOUT_WEST -> "Poludniowy zachod";
            case NORTH_WEST -> "Polnocny zachod";
        };

    }

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

    /* sprawdzenie czy dzialaja
    public static void main(String[] args)
    {
        for(MapDirection direction : MapDirection.values())
        {
            System.out.println(direction.toString());
            System.out.print(direction);
            System.out.print(direction.previous());
            System.out.println(direction.next());
            System.out.println(direction.toUnitVector());
        }

    }

     */

}
