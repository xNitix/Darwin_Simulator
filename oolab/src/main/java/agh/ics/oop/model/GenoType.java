package agh.ics.oop.model;

import java.util.Arrays;

public class GenoType {
    private final static int TYPES_OF_GENES = 8;
    public static int[] createRandomGenoType(int genNumber){
        int[] genotype = new int[genNumber];
        for(int i = 0; i < genNumber; i++){
            genotype[i] = (int) ((Math.random()*TYPES_OF_GENES ));
        }
        Arrays.sort(genotype);
        return genotype;
    }
}
