package agh.ics.oop.model.entity;

import agh.ics.oop.model.utils.Vector2d;

public abstract class AbstractPlant  implements WorldElement {
    Vector2d position;
    public Vector2d getPosition() {
        return position;
    }
}
