package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void testEquals() {
        Vector2d vA = new Vector2d(3,4);
        Vector2d vB = new Vector2d(-3,4);
        Vector2d vC = new Vector2d(3,-4);
        Vector2d vD = new Vector2d(3,4);
        Vector2d vE = new Vector2d(3,3);
        Vector2d vF = new Vector2d(2,4);

        assertEquals(vA, vD);
        assertFalse(vA.equals(vB));
        assertFalse(vA.equals(vC));
        assertFalse(vA.equals(vE));
        assertFalse(vA.equals(vF));
    }

    @Test
    void testToString() {
        Vector2d vA = new Vector2d(-2,3);
        Vector2d vB = new Vector2d(10,3);
        Vector2d vC = new Vector2d(3,-4);
        Vector2d vD = new Vector2d(-4,-4);

        assertEquals(vA.toString(),"(-2,3)");
        assertEquals(vB.toString(),"(10,3)");
        assertEquals(vC.toString(),"(3,-4)");
        assertEquals(vD.toString(),"(-4,-4)");
    }

    @Test
    void precedes() {
        Vector2d vA = new Vector2d(-2,3);
        Vector2d vB = new Vector2d(10,3);
        Vector2d vC = new Vector2d(3,-4);
        Vector2d vD = new Vector2d(-4,-4);

        assertTrue(vA.precedes(vB));
        assertTrue(vD.precedes(vA));
        assertTrue(vC.precedes(vB));
        assertFalse(vB.precedes(vC));
        assertFalse(vA.precedes(vC));
    }

    @Test
    void follows() {
        Vector2d vA = new Vector2d(-2,3);
        Vector2d vB = new Vector2d(10,3);
        Vector2d vC = new Vector2d(3,-4);
        Vector2d vD = new Vector2d(-4,-4);

        assertFalse(vA.follows(vB));
        assertFalse(vD.follows(vA));
        assertFalse(vC.follows(vB));
        assertTrue(vB.follows(vC));
        assertFalse(vA.follows(vC));
    }

    @Test
    void add() {
        Vector2d vA = new Vector2d(-2,3);
        Vector2d vB = new Vector2d(10,3);
        Vector2d vC = new Vector2d(3,-4);
        Vector2d vD = new Vector2d(-4,-4);

        assertEquals(vA.add(vB),new Vector2d(8,6));
        assertEquals(vA.add(vC),new Vector2d(1,-1));
        assertEquals(vA.add(vD),new Vector2d(-6,-1));
        assertEquals(vC.add(vD),new Vector2d(-1,-8));
    }

    @Test
    void subtract() {
        Vector2d vA = new Vector2d(-2,3);
        Vector2d vB = new Vector2d(10,3);
        Vector2d vC = new Vector2d(3,-4);
        Vector2d vD = new Vector2d(-4,-4);

        assertEquals(vA.subtract(vB),new Vector2d(-12,0));
        assertEquals(vA.subtract(vC),new Vector2d(-5,7));
        assertEquals(vA.subtract(vD),new Vector2d(2,7));
        assertEquals(vC.subtract(vD),new Vector2d(7,0));
    }

    @Test
    void upperRight() {
        Vector2d vA = new Vector2d(-2,3);
        Vector2d vB = new Vector2d(10,3);
        Vector2d vC = new Vector2d(3,-4);
        Vector2d vD = new Vector2d(-4,-4);

        assertEquals(vA.upperRight(vB), new Vector2d(10,3));
        assertEquals(vA.upperRight(vC), new Vector2d(3,3));
        assertEquals(vA.upperRight(vD), new Vector2d(-2,3));
        assertEquals(vC.upperRight(vD), new Vector2d(3,-4));
        assertEquals(vB.upperRight(vD), new Vector2d(10,3));


    }

    @Test
    void lowerLeft() {
        Vector2d vA = new Vector2d(-2,3);
        Vector2d vB = new Vector2d(10,3);
        Vector2d vC = new Vector2d(3,-4);
        Vector2d vD = new Vector2d(-4,-4);

        assertEquals(vA.lowerLeft(vB),new Vector2d(-2,3));
        assertEquals(vA.lowerLeft(vC),new Vector2d(-2,-4));
        assertEquals(vA.lowerLeft(vD),new Vector2d(-4,-4));
        assertEquals(vC.lowerLeft(vB),new Vector2d(3,-4));
        assertEquals(vD.lowerLeft(vB),new Vector2d(-4,-4));
    }

    @Test
    void opposite() {
        Vector2d vA = new Vector2d(-2,3);
        Vector2d vB = new Vector2d(10,3);
        Vector2d vC = new Vector2d(3,-4);
        Vector2d vD = new Vector2d(-4,-4);

        assertEquals(vA.opposite(),new Vector2d(2,-3));
        assertEquals(vB.opposite(),new Vector2d(-10,-3));
        assertEquals(vC.opposite(),new Vector2d(-3,4));
        assertEquals(vD.opposite(),new Vector2d(4,4));

    }

}