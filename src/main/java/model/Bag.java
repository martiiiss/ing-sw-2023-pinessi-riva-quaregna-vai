package model;

import util.Observable;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/** This class represents the bag that contains tiles used to fill the board */
public class Bag extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = -7897863812725192313L;
    private static final int MAX_TILES_ONE_TYPE = 22;
    private static final int MAX_NUM_TYPE = 3;
    private final ArrayList<Tile> tilesContained;

    /**
     *
     * <p>
     *     Method used to extract tiles from the Bag.
     * </p>
     * @param requestedTiles represents the number of tiles that are needed to fill the Board
     * @return an <code>ArrayList</code> of Tile
     */
    public ArrayList<Tile> getBagTiles(int requestedTiles) { //take tiles from bag
        ArrayList<Tile> returnedTiles = new ArrayList<>();
        synchronized (tilesContained) {
            for (int e = 0; e < requestedTiles && !tilesContained.isEmpty(); e++) {
                int i;
                do {
                    i = (new Random()).nextInt(tilesContained.size());
                } while (i >= tilesContained.size());
                returnedTiles.add(tilesContained.get(i));
                tilesContained.remove(tilesContained.get(i));//Remove the tiles from the ArrayList
            }
            return returnedTiles;
        }
    }

    /**
     *<p>
     *     Method used to add a tile into the Bag
     *</p>
     * @param tileAdded represents the tile to add into the Bag
     */

    public void addTile(Tile tileAdded) {
        this.tilesContained.add(tileAdded);
    }

    /**
     * <p>
     *     Method used to get the list of tiles contained by the Bag
     * </p>
     * @return an <code>ArrayList</code> of Tile
     */
    public ArrayList<Tile> getTilesContained (){ return this.tilesContained; }

    /**
     * <p>
     *     Constructor of the Bag class.
     *     This creates all the tiles and adds them into tilesContained.
     *     Inside the bag there are six different types of tile and for each type there arre 22 tiles
     *     for a total of 132 tiles.
     * </p>
     */
    public Bag(){
        tilesContained = new ArrayList<>();
        synchronized (tilesContained) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < MAX_TILES_ONE_TYPE; j++) {
                    switch (i) {
                        case 0 -> tilesContained.add(new Tile(Type.CAT, j % MAX_NUM_TYPE + 1));
                        case 1 -> tilesContained.add(new Tile(Type.BOOK, j % MAX_NUM_TYPE + 1));
                        case 2 -> tilesContained.add(new Tile(Type.GAME, j % MAX_NUM_TYPE + 1));
                        case 3 -> tilesContained.add(new Tile(Type.FRAME, j % MAX_NUM_TYPE + 1));
                        case 4 -> tilesContained.add(new Tile(Type.TROPHY, j % MAX_NUM_TYPE + 1));
                        case 5 -> tilesContained.add(new Tile(Type.PLANT, j % MAX_NUM_TYPE + 1));
                    }
                }
            }
        }
    }
}