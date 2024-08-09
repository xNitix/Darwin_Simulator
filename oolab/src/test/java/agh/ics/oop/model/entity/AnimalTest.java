package agh.ics.oop.model.entity;

import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.utils.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    Animal animal;
    Grass grass;
    BadGrass badGrass;

    @BeforeEach
    void setUp() {
        Vector2d initialPosition = new Vector2d(0, 0);
        int[] genoType = {1,1,1};
        animal = new Animal(initialPosition, genoType, 5, false);
        grass = new Grass(initialPosition);
        badGrass = new BadGrass(initialPosition);
    }

    @Test
    void animalEatGrass() {
        animal.animalEat(10, grass);
        assertEquals(15, animal.getCurrentEnergy());
    }

    @Test
    void animalEatBadGrass() {
        animal.animalEat(10, badGrass);
        assertEquals(-5, animal.getCurrentEnergy());
    }

    @Test
    void animalEatenGrassCounter() {
        animal.animalEat(10, grass);
        animal.animalEat(10, badGrass);
        animal.animalEat(10, grass);
        animal.animalEat(10, grass);
        animal.animalEat(10, badGrass);
        animal.animalEat(10, badGrass);
        animal.animalEat(10, grass);
        assertEquals(7, animal.getGrassEatenCounter());
    }

    @Test
    void getNewDirection() {
         animal.getNewDirection();
         assertEquals(MapDirection.NORTH_EAST, animal.getCurrentOrientation());
         animal.getNewDirection();
         assertEquals(MapDirection.EAST, animal.getCurrentOrientation());
         animal.getNewDirection();
         assertEquals(MapDirection.SOUTH_EAST, animal.getCurrentOrientation());
         animal.getNewDirection();
         assertEquals(MapDirection.SOUTH, animal.getCurrentOrientation());
    }

    @Test
    void move() {
        var vec =  new Vector2d(1,1);
        animal.move(-1, vec);
        assertEquals(vec, animal.getPosition());
        assertEquals(4, animal.getCurrentEnergy());
        assertEquals(1, animal.getDayAlive());

        animal.move(-1, vec);
        assertEquals(MapDirection.SOUTH, animal.getCurrentOrientation());
    }
}