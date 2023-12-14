package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void next() {
        assertEquals(MapDirection.NORTH.next(), MapDirection.EAST);
        assertTrue(MapDirection.EAST.next() == MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH.next(), MapDirection.WEST);
        assertEquals(MapDirection.WEST.next(), MapDirection.NORTH);
        assertFalse(MapDirection.NORTH.next() == MapDirection.WEST);
        assertFalse(MapDirection.SOUTH.next() == MapDirection.EAST);
    }

    @Test
    void previous() {
        assertEquals(MapDirection.NORTH.previous(), MapDirection.WEST);
        assertTrue(MapDirection.WEST.previous()== MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH.previous(), MapDirection.EAST);
        assertEquals(MapDirection.EAST.previous(), MapDirection.NORTH);
        assertFalse(MapDirection.NORTH.previous() == MapDirection.EAST);
        assertFalse(MapDirection.SOUTH.previous() == MapDirection.WEST);
    }
}