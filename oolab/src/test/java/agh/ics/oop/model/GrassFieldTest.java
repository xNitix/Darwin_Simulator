package agh.ics.oop.model;

import agh.ics.oop.model.Exceptions.PositionAlreadyOccupiedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {

    GrassField map;
    Animal animal0;
    Animal animal1;
    Animal animal2;

    @BeforeEach
    void setUp() throws PositionAlreadyOccupiedException
    {
        map = new GrassField(-777);
        animal0 = new Animal(new Vector2d(0,3));
        map.place(animal0);
        animal1 = new Animal(new Vector2d(0,0));
        map.place(animal1);
        animal2 = new Animal();
        map.place(animal2);
    }

    @Test
    void canMoveTo(){
        //nachodzenie na innego zwierzaka
        assertFalse(map.canMoveTo(new Vector2d(0,3)));
        assertFalse(map.canMoveTo(new Vector2d(2,2)));

        assertTrue(map.canMoveTo(new Vector2d(-444,10)));
        assertTrue(map.canMoveTo(new Vector2d(100,1000)));

        //nachodzenie na trawe
        assertTrue(map.canMoveTo(new Vector2d(5,5)));
        assertTrue(map.canMoveTo(new Vector2d(2,3)));
    }

    @Test
    void place() throws PositionAlreadyOccupiedException {
        Animal animalNew0 = new Animal(new Vector2d(5,0));
        Animal animalNew1 = new Animal(new Vector2d(5,1));
        Animal animalNew2 = new Animal(new Vector2d(5,1));
        map.place(animalNew1);
        map.place(animalNew0);

        assertThrows(PositionAlreadyOccupiedException.class, ()->map.place(animalNew2));
    }

    @Test
    void move() {
        map.move(animal0,MoveDirection.RIGHT);
        assertEquals(animal0.getCurrentOrientation(),MapDirection.EAST);
        assertEquals(animal0.getPosition(),new Vector2d(0,3));
        map.move(animal0,MoveDirection.FORWARD);
        assertEquals(animal0.getPosition(),new Vector2d(1,3));
        map.move(animal1,MoveDirection.BACKWARD);
        assertNotEquals(animal1.getPosition(),new Vector2d(0,0));
        map.move(animal2,MoveDirection.BACKWARD);
        assertNotEquals(animal2.getPosition(),new Vector2d(2,0));
        map.move(animal0,MoveDirection.FORWARD);
        assertEquals(animal0.getPosition(),new Vector2d(2,3));
        map.move(animal0,MoveDirection.FORWARD);
        assertEquals(animal0.getPosition(),new Vector2d(3,3));
        assertTrue(map.isOccupied(new Vector2d(2,3)));
    }

    @Test
    void isOccupied() {
        assertTrue(map.isOccupied(new Vector2d(0,3)));
        assertTrue(map.isOccupied(new Vector2d(2, 2)));
        assertFalse(map.isOccupied(new Vector2d(0, 2)));
        assertFalse(map.isOccupied(new Vector2d(5, 2)));
        assertTrue(map.isOccupied(new Vector2d(5, 5)));
        assertTrue(map.isOccupied(new Vector2d(2, 1)));
    }

    @Test
    void objectAt() {
        assertNull(map.objectAt(new Vector2d(0, 5)));
        assertNull(map.objectAt(new Vector2d(5, 0)));
        assertNull(map.objectAt(new Vector2d(4, 2)));
        assertNotNull(map.objectAt(new Vector2d(0, 3)));
        assertNotNull(map.objectAt(new Vector2d(0, 0)));
        assertNotNull(map.objectAt(new Vector2d(2, 3)));
        assertNotNull(map.objectAt(new Vector2d(5, 5)));
    }
}