package agh.ics.oop.model.Exceptions;

import agh.ics.oop.model.Vector2d;

public class PositionAlreadyOccupiedException extends Exception {
    public PositionAlreadyOccupiedException(Vector2d position) {
        super("Position " +  position.toString() + " is already occupied");
    }
}
