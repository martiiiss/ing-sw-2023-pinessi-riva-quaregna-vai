package model;

import java.util.ArrayList;
import java.util.Random;


public class Bag {
    private boolean isBagEmpty;
    private static final int MAX_TILES_ONE_TYPE = 22;
    private static final int MAX_NUM_TYPE = 3;
    private ArrayList<Tile> tilesContained;
    // Bag takes in the number of requested tiles and returns tilesContained


    /**
     * @param requestedTiles
     * @return
     */
    public ArrayList<Tile> getBagTiles(int requestedTiles) { //take tiles from bag
        ArrayList<Tile> returnedTiles = new ArrayList<>();
        for(int e=0; e<requestedTiles && !tilesContained.isEmpty(); e++) {
            int i = (new Random()).nextInt(tilesContained.size());
            returnedTiles.add(tilesContained.get(i));
            tilesContained.remove(tilesContained.get(i));//Remove the tiles from the ArrayList
        }
        return returnedTiles;
    }

    public void addTile(Tile tileAdded) {
        this.tilesContained.add(tileAdded);
        }

    public ArrayList<Tile> getTilesContained (){ return this.tilesContained; }

    public Bag(){ // the constructor creates ALL the tiles and adds them into tilesContained
        tilesContained = new ArrayList<>();
        int e=0;
        for(int i=0; i<6; i++){ //6 type
            for(int j=0; j<MAX_TILES_ONE_TYPE; j++){ //22 tiles for one type
                switch (i) {
                    case 0 -> tilesContained.add(new Tile(Type.CAT, j % MAX_NUM_TYPE + 1));
                    case 1 -> tilesContained.add(new Tile(Type.BOOK, j % MAX_NUM_TYPE + 1));
                    case 2 -> tilesContained.add(new Tile(Type.GAME, j % MAX_NUM_TYPE + 1));
                    case 3 -> tilesContained.add(new Tile(Type.FRAME, j % MAX_NUM_TYPE + 1));
                    case 4 -> tilesContained.add(new Tile(Type.TROPHY, j % MAX_NUM_TYPE + 1));
                    case 5 -> tilesContained.add(new Tile(Type.PLANT, j % MAX_NUM_TYPE + 1));
                }
                e++;
            }
        }
    }
}