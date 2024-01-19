package agh.ics.oop.model;

public class Grass extends AbstractPlant {
    public Grass(Vector2d position) {
        super.position = position;
    }
    @Override
    public String toString() {
        return "*";
    }
}
