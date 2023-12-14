package agh.ics.oop.model;

public class Grass implements WorldElement {
    private Vector2d position;
    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return position.equals(this.position);
    }

    @Override
    public String toString() {
        return "*";
    }
}
