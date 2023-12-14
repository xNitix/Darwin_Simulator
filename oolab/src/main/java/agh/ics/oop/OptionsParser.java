package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    public static List<MoveDirection> parse(String[] args) {
        List<MoveDirection> result = new ArrayList<>();

        for (String arg : args) {
            switch (arg) {
                case "f" -> result.add(MoveDirection.FORWARD);

                case "b" -> result.add(MoveDirection.BACKWARD);

                case "r" -> result.add(MoveDirection.RIGHT);

                case "l" -> result.add(MoveDirection.LEFT);

                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }
        return result;
    }
}