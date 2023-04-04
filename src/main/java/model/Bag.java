package model;

import jdk.jshell.spi.ExecutionControl;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.out;

public class Bag {
    private boolean isBagEmpty;
    private ArrayList<Tile> tilesContained;
    // Bag takes in the number of requested tiles and returns tilesContained

    /**
     *
     * @param requestedTiles
     * @return
     */
    public ArrayList<Tile> getBagTiles(int requestedTiles) { //take tiles from bag
        ArrayList<Tile> returnedTiles = new ArrayList<>();
        for(int e=0; e<requestedTiles; e++) {
            int i = (new Random()).nextInt(tilesContained.size());
            returnedTiles.add(tilesContained.get(i));
            tilesContained.remove(tilesContained.get(i));//Remove the tiles from the ArrayList
        }
        return returnedTiles;

        /*
        ArrayList<Tile> returnedTiles = new ArrayList<>();
        for(int i=requestedTiles-1; i>0; i--){// Seen that it removes from the arraylist I prefer it done from the end to the beginning
            returnedTiles.add(tilesContained.get(i));
            tilesContained.remove(tilesContained.get(i));//Remove the tiles from the ArrayList
        }
        return returnedTiles;
         */
    }

    public void addTile(Tile tileAdded) {
        this.tilesContained.add(tileAdded);
        }

    public Bag(){ // the constructor creates ALL the tiles and adds them into tilesContained
        tilesContained = new ArrayList<>();
        int e=0;
        for(int i=0; i<6; i++){ //6 type
            for(int j=0; j<22; j++){ //22 tiles for one type
                switch (i) {
                    case 0 -> tilesContained.add(new Tile(Type.CAT, j % 3 + 1));
                    case 1 -> tilesContained.add(new Tile(Type.BOOK, j % 3 + 1));
                    case 2 -> tilesContained.add(new Tile(Type.GAME, j % 3 + 1));
                    case 3 -> tilesContained.add(new Tile(Type.FRAME, j % 3 + 1));
                    case 4 -> tilesContained.add(new Tile(Type.TROPHY, j % 3 + 1));
                    case 5 -> tilesContained.add(new Tile(Type.PLANT, j % 3 + 1));
                }
                e++;

            }
        }
    }
}

// I don't know if the tile shuffling is  part of the model