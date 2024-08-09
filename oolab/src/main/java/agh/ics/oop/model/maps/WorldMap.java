package agh.ics.oop.model.maps;

import agh.ics.oop.model.utils.Boundary;
import agh.ics.oop.model.entity.Animal;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap {
    /**
     * Place a animal on the map.
     *
     * @param animal The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the move is not valid.
     */
    void place(Animal animal);

    void move(Animal animal, int energy);
    Boundary getCurrentBounds();

}
