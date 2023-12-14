package agh.ics.oop;

import agh.ics.oop.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {
    private List<Vector2d> positions;
    private WorldMap map;

    @BeforeEach
    void setUp()
    {
        positions = List.of(new Vector2d(0,4),new Vector2d(2,2));
        map = new RectangularMap(4,4);
    }

    @Test// wyjscie poza plansze
    void run1() {
        List<MoveDirection> directions = List.of(MoveDirection.FORWARD);
        Simulation simulation = new Simulation(positions,directions,map);
        simulation.run();
        List<Animal> finallAnimals = simulation.getAnimals();
        assertEquals(finallAnimals.get(0).getPosition(),new Vector2d(0,4));
    }

    @Test// sprawdzenie orientacji left-right
    void run2() {
        List<MoveDirection> directions = List.of(MoveDirection.LEFT,MoveDirection.RIGHT);
        Simulation simulation = new Simulation(positions,directions,map);
        simulation.run();
        List<Animal> finallAnimals = simulation.getAnimals();
        assertEquals(finallAnimals.get(0).getCurrentOrientation(),MapDirection.WEST);
        assertEquals(finallAnimals.get(1).getCurrentOrientation(),MapDirection.EAST);
        // sprawdzenie czy ruch do przodu i tylu nie zmienia orientacji
        List<MoveDirection> directions2 = List.of(MoveDirection.BACKWARD,MoveDirection.FORWARD);
        map = new RectangularMap(4,4);
        Simulation simulation2 = new Simulation(positions,directions2,map);
        simulation2.run();
        assertEquals(finallAnimals.get(0).getCurrentOrientation(),MapDirection.WEST);
        assertEquals(finallAnimals.get(1).getCurrentOrientation(),MapDirection.EAST);
    }

    @Test // prawidlowe poruszanie
    void run3() {
        List<MoveDirection> directions = List.of(MoveDirection.LEFT,MoveDirection.FORWARD,MoveDirection.FORWARD,MoveDirection.RIGHT,
        MoveDirection.LEFT,MoveDirection.FORWARD,MoveDirection.FORWARD,MoveDirection.BACKWARD);
        Simulation simulation = new Simulation(positions,directions,map);
        simulation.run();
        List<Animal> finallAnimals = simulation.getAnimals();

        assertEquals(finallAnimals.get(0).getPosition(),new Vector2d(0,3));
        assertEquals(finallAnimals.get(0).getCurrentOrientation(),MapDirection.SOUTH);

        assertEquals(finallAnimals.get(1).getPosition(),new Vector2d(2,3));
        assertEquals(finallAnimals.get(1).getCurrentOrientation(),MapDirection.EAST);
    }

    @Test // dane wejsciowe jako tablica string
    void run4() {
        String[] arg = {"l"," ","g","b","f","n","r","r","b","f","b","r","r","f"};
        List<MoveDirection> directions = OptionsParser.parse(arg);
        //System.out.println(directions);
        Simulation simulation = new Simulation(positions,directions,map);
        simulation.run();
        List<Animal> finallAnimals = simulation.getAnimals();

        assertEquals(finallAnimals.get(0).getPosition(),new Vector2d(1,4));
        assertEquals(finallAnimals.get(0).getCurrentOrientation(),MapDirection.EAST);

        assertEquals(finallAnimals.get(1).getPosition(),new Vector2d(0,1));
        assertEquals(finallAnimals.get(1).getCurrentOrientation(),MapDirection.SOUTH);
    }

    @Test // wiecej testow, wyjscia za plansze
    void run5() {
        String[] arg = {"l"," ","g","b","f","n","r","r","b","f","b","r","r","f","r","l","f","f"};
        List<MoveDirection> directions = OptionsParser.parse(arg);
        //System.out.println(directions);
        Simulation simulation = new Simulation(positions,directions,map);
        simulation.run();
        List<Animal> finallAnimals = simulation.getAnimals();

        assertEquals(finallAnimals.get(0).getPosition(),new Vector2d(1,4));
        assertEquals(finallAnimals.get(0).getCurrentOrientation(),MapDirection.NORTH);

        assertEquals(finallAnimals.get(1).getPosition(),new Vector2d(0,1));
        assertEquals(finallAnimals.get(1).getCurrentOrientation(),MapDirection.WEST);
    }

    @Test // wiecej testow, wyjscie z prawej, pelny obrot
    void run6() {
        String[] arg = {"r","r","f","r","f","r","f","r","f","f","f"};
        List<MoveDirection> directions = OptionsParser.parse(arg);
        //System.out.println(directions);
        Simulation simulation = new Simulation(positions,directions,map);
        simulation.run();
        List<Animal> finallAnimals = simulation.getAnimals();

        assertEquals(finallAnimals.get(0).getPosition(),new Vector2d(4,4));
        assertEquals(finallAnimals.get(0).getCurrentOrientation(),MapDirection.EAST);

        assertEquals(finallAnimals.get(1).getPosition(),new Vector2d(2,3));
        assertEquals(finallAnimals.get(1).getCurrentOrientation(),MapDirection.NORTH);
    }

    @Test // wiecej testow, wyjscie dol, obrot lewo
    void run7() {
        String[] arg = {"l","b","l","b","l","b","l","b"};
        List<MoveDirection> directions = OptionsParser.parse(arg);
        //System.out.println(directions);
        Simulation simulation = new Simulation(positions,directions,map);
        simulation.run();
        List<Animal> finallAnimals = simulation.getAnimals();

        assertEquals(finallAnimals.get(0).getPosition(),new Vector2d(0,4));
        assertEquals(finallAnimals.get(0).getCurrentOrientation(),MapDirection.NORTH);

        assertEquals(finallAnimals.get(1).getPosition(),new Vector2d(2,0));
        assertEquals(finallAnimals.get(1).getCurrentOrientation(),MapDirection.NORTH);
    }

}