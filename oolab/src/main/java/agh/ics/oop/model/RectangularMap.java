package agh.ics.oop.model;

import java.util.UUID;

public class RectangularMap extends AbstractWorldMap {

    private Vector2d leftDown;
    private Vector2d rightUp;

    public RectangularMap(int width, int height) {
        this.leftDown = new Vector2d(0,0);
        this.rightUp = new Vector2d(width-1,height-1);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(leftDown) && position.precedes(rightUp) && super.canMoveTo(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(leftDown,rightUp);
    }

    @Override
    public UUID getID() {
        return id;
    }
}
