package model;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;



public class Bag {
    private boolean isBagEmpty;
    private ArrayList<Tile> tilesContained;
    // Bag takes in the number of requested tiles and returns tilesContained

    public ArrayList<Tile> getBagTiles(int requestedTiles) {
        ArrayList<Tile> returnedTiles = new ArrayList<>();
        for(int i=requestedTiles-1; i>0; i--)   {          // Seen that it removes from the arraylist I prefer it done from the end to the beginning
            returnedTiles.add(tilesContained.get(i));
            tilesContained.remove(tilesContained.get(i));  //Remove the tiles from the ArrayList
        }
        return returnedTiles;
    }
    public void addTile(Tile tileAdded) {
        this.tilesContained.add(tileAdded);
        }
}

// I don't know if the tile shuffling is  part of the model
