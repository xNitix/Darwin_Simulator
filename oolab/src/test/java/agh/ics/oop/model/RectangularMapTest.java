package agh.ics.oop.model;

import agh.ics.oop.model.Exceptions.PositionAlreadyOccupiedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {

    RectangularMap map;
    Animal animal0;
    Animal animal1;
    Animal animal2;
    Animal animal3;
    Animal animal4;
    Animal animal5;

    @BeforeEach
    void setUp() throws PositionAlreadyOccupiedException
    {
        map = new RectangularMap(6,4);
        animal0 = new Animal(new Vector2d(0,3));
        map.place(animal0);
        animal1 = new Animal(new Vector2d(0,0));
        map.place(animal1);
        animal2 = new Animal(new Vector2d(2,1));
        map.place(animal2);
        animal3 = new Animal(new Vector2d(3,3));
        map.place(animal3);
        animal4 = new Animal(new Vector2d(3,1));
        map.place(animal4);
        animal5 = new Animal(new Vector2d(5,0));
        map.place(animal5);
    }

    @Test
    void canMoveTo() {
        assertTrue(map.canMoveTo(new Vector2d(1,3)));
        assertTrue(map.canMoveTo(new Vector2d(5,3)));
        assertFalse(map.canMoveTo(new Vector2d(3,3)));
        assertFalse(map.canMoveTo(new Vector2d(6,3)));
        assertFalse(map.canMoveTo(new Vector2d(3,-1)));
    }

    @Test
    void place() throws PositionAlreadyOccupiedException{
        Animal animalNew0 = new Animal(new Vector2d(5,0));
        Animal animalNew1 = new Animal(new Vector2d(5,1));
        Animal animalNew2 = new Animal(new Vector2d(5,1));
        Animal animalNew3 = new Animal(new Vector2d(3,0));
        map.place(animalNew1);
        map.place(animalNew3);

        assertThrows(PositionAlreadyOccupiedException.class, ()->map.place(animalNew0));
        assertThrows(PositionAlreadyOccupiedException.class, ()->map.place(animalNew2));
    }

    @Test
    void move() {
        map.move(animal0,MoveDirection.RIGHT);
        assertEquals(animal0.getCurrentOrientation(),MapDirection.EAST);
        assertEquals(animal0.getPosition(),new Vector2d(0,3));
        map.move(animal0,MoveDirection.FORWARD);
        assertEquals(animal0.getPosition(),new Vector2d(1,3));
        map.move(animal0,MoveDirection.FORWARD);
        map.move(animal0,MoveDirection.FORWARD);
        assertNotEquals(animal0.getPosition(),animal3.getPosition());
        map.move(animal1,MoveDirection.BACKWARD);
        assertEquals(animal1.getPosition(),new Vector2d(0,0));
        map.move(animal2,MoveDirection.BACKWARD);
        assertEquals(animal2.getPosition(),new Vector2d(2,0));
        map.move(animal4,MoveDirection.LEFT);
        map.move(animal4,MoveDirection.FORWARD);
        assertEquals(animal4.getPosition(),new Vector2d(2,1));
    }

    @Test
    void isOccupied() {
        assertTrue(map.isOccupied(animal0.getPosition()));
        assertTrue(map.isOccupied(animal1.getPosition()));
        assertFalse(map.isOccupied(new Vector2d(5,2)));
        assertTrue(map.isOccupied(animal2.getPosition()));
        assertFalse(map.isOccupied(new Vector2d(3,0)));
        assertTrue(map.isOccupied(animal3.getPosition()));
        assertFalse(map.isOccupied(new Vector2d(3,2)));
    }

    @Test
    void objectAt() {
        assertNull(map.objectAt(new Vector2d(3,2)));
        assertNull(map.objectAt(new Vector2d(5,2)));
        assertEquals(animal0,map.objectAt(animal0.getPosition()));
        assertEquals(animal2,map.objectAt(animal2.getPosition()));
        assertEquals(animal3,map.objectAt(animal3.getPosition()));
    }

}