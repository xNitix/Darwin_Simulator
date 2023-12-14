package agh.ics.oop;

import agh.ics.oop.model.*;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class World {
    public static void main(String[] args)
    {
        /*
        System.out.println("system wystartował");
        List<MoveDirection> new_args = OptionsParser.parse(args);
        run(new_args);
        System.out.println("system zakończył działanie");
        */

        /*Vector2d position1 = new Vector2d(1,2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2,1);
        System.out.println(position2);
        System.out.println(position1.add(position2));*/

        /*
        Animal animal1 = new Animal();
        System.out.println(animal1.getPosition());
        */

        /*
        List<MoveDirection> directions = OptionsParser.parse(args);
        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
        Simulation simulation = new Simulation(positions, directions);
        simulation.run();


        List<Simulation> simulations = new ArrayList<>();
        List<MoveDirection> directions = OptionsParser.parse(args);
        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4),new Vector2d(2,2),new Vector2d(4,4));
        ConsoleMapDisplay mapDisplay = new ConsoleMapDisplay();

        for(int i = 0; i <= 1000 ; i++) {
            GrassField map = new GrassField(10);
            RectangularMap map2 = new RectangularMap(6, 6);
            map.subscribe(mapDisplay);
            map2.subscribe(mapDisplay);
            simulations.add(new Simulation(positions, directions, map));
            simulations.add(new Simulation(positions, directions, map2));
        }

        SimulationEngine simulationEngine = new SimulationEngine(simulations,4);
        //simulationEngine.runAsync();
        simulationEngine.runAsyncInThreadPool();
        simulationEngine.awaitSimulationsEnd();
        */
        Application.launch(SimulationApp.class, args);

        //Collection<WorldElement> collection =  map.getElements();
        //System.out.println(collection);

        System.out.println("system zakonczyl dzialanie");
    }

    public static void run(List<MoveDirection> args)
    {
        /*System.out.println("zwierzak idzie do przodu");

         wypisywanie argumentow z tablicy z przecinkam
         (wtedy run przyjmowal tablice string)

        int len_args = args.length;
        int iterator = 0;
        for (String arg : args)
        {
            System.out.print(arg);
            if (iterator < len_args - 1)
            {
                System.out.print(", ");
                iterator+=1;
            }
        }
        System.out.println();
         */

        /*
        stworzenie switch ktory wypiswal gdzie idzie zwierzak
        (wtedy run przyjmowal tablice string)

        for(String arg : args)
        {
            switch(arg)
            {
                case "f" -> System.out.println("Zwierzak idzie do przodu");
                case "b" -> System.out.println("Zwierzak idzie do tyłu");
                case "r" -> System.out.println("Zwierzak idzie w prawo");
                case "l" -> System.out.println("Zwierzak idzie w lewo");
                default -> System.out.print("");
            }
        }

         */
        /*
        for(MoveDirection kierunek : args)
        {
            switch(kierunek)
            {
                case FORWARD -> System.out.println("Zwierzak idzie do przodu");
                case BACKWARD -> System.out.println("Zwierzak idzie do tyłu");
                case RIGHT -> System.out.println("Zwierzak idzie w prawo");
                case LEFT -> System.out.println("Zwierzak idzie w lewo");
            }
        }

         */



    }
}
