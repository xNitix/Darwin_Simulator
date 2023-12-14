package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {

    private int uppdateCounter;
    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        uppdateCounter ++;
        System.out.println(worldMap.getID());
        System.out.println(message);
        System.out.println(worldMap);
        System.out.println(uppdateCounter);
    }
}
