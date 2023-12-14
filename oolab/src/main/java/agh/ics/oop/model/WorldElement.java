package agh.ics.oop.model;

public interface WorldElement {

    boolean isAt(Vector2d position);

    Vector2d getPosition();
}
