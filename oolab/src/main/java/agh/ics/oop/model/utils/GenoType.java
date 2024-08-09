package agh.ics.oop.model.utils;

import agh.ics.oop.model.entity.Animal;

import java.util.Random;

public class GenoType {
    private final static int TYPES_OF_GENES = 8;
    public static int[] createRandomGenoType(int genNumber){
        int[] genotype = new int[genNumber];
        for(int i = 0; i < genNumber; i++){
            genotype[i] = (int) ((Math.random()*TYPES_OF_GENES ));
        }
        return genotype;
    }

    public static int[] combineGenoType(int genNumber, Animal animal1, Animal animal2, int minMutations, int maxMutations){

        int[] childGenes = new int[genNumber];
        int totalEnergy = animal1.getCurrentEnergy() + animal2.getCurrentEnergy();

        //miejsce przecięcia genotypów na podstawie energii rodziców
        double parent1Ratio = (double) animal1.getCurrentEnergy() / totalEnergy;
        int crossoverPoint = (int) (animal1.getGenoType().length * parent1Ratio);

        Random random = new Random();

        // Losowanie strony genotypu
        boolean useParent1 = random.nextBoolean();

        // Tworzenie genotypu dziecka na podstawie genotypów rodziców
        for (int i = 0; i < genNumber; i++) {
            if ((useParent1 && i < crossoverPoint) || (!useParent1 && i >= crossoverPoint)) {
                childGenes[i] = animal1.getGenoType()[i];
            } else {
                childGenes[i] = animal2.getGenoType()[i];
            }
        }

        // Mutacje - losowe zmiany wybranych genów potomka
        int numberOfMutations = random.nextInt(maxMutations - minMutations + 1) + minMutations;
        for (int i = 0; i < numberOfMutations; i++) {
            int mutationIndex = random.nextInt(genNumber);
            childGenes[mutationIndex] = random.nextInt(8);
        }
        return childGenes;
    }

}
