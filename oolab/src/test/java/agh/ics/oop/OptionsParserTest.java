package agh.ics.oop;

import agh.ics.oop.model.Exceptions.PositionAlreadyOccupiedException;
import agh.ics.oop.model.MoveDirection;
import com.sun.source.tree.NewArrayTree;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {

    @Test
    void convert(){
        String[] input = {"f","fsfs","b","r",""," ","l"," ","","f"};
        assertThrows(IllegalArgumentException.class, ()->OptionsParser.parse(input));
        String[] input2 = {"f","b","r","l","f"};
        List<MoveDirection> res = OptionsParser.parse(input2);
        List<MoveDirection> correct = List.of(MoveDirection.FORWARD,MoveDirection.BACKWARD,MoveDirection.RIGHT,MoveDirection.LEFT,MoveDirection.FORWARD);
        assertIterableEquals(res, correct );
    }
}