package agh.ics.oop.model;

public class BadGrass extends AbstractPlant{
    public BadGrass(Vector2d position) {
        super.position = position;
    }
    @Override
    public String toString() {
        return "!";
    }
}
