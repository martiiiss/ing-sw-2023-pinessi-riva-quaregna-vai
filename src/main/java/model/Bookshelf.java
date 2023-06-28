package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import static model.Type.NOTHING;
import distributed.messages.Message;
import util.Event;
import util.Observable;
import util.TileForMessages;

/**Class that represents the Bookshelf*/
public class Bookshelf extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = 4784659265294763952L;
    private Tile[][] bookshelf;
    /**The bookshelf row max value*/
    public static final int SHELF_ROW = 6;
    /**The bookshelf column max value*/
    public static final int SHELF_COLUMN = 5;
    private boolean isFull = false;

    //constructor -> it initializes the bookshelf at NOTHING tiles, without this I cannot test the Player class
    /**
     * <p>
     *  Constructor of the Class. <br>
     *  This initializes the bookshelf as a matrix of 30 <b>nothing</b> tiles.
     * </p>
     * */
    public Bookshelf(){
        this.bookshelf = new Tile[SHELF_ROW][SHELF_COLUMN];
        for (int i=0;i<SHELF_ROW;i++){
            for(int j=0; j<SHELF_COLUMN;j++){
                bookshelf[i][j] = new Tile(NOTHING,0);
            }
        }
    }

    /**
     * <p>
     *     Method that returns the maximum number of free slot. <br>
     *     <b>NOTE:</b> it returns the maximum number of free slots in a column
     *     (<i>for example</i> if there's an empty column the returned in will be <b>6</b>).
     * </p>
     * @return and integer that represents the max number of free slots in a column */

    public int getNumOfFreeSlots() {
        int[] values = new int[SHELF_COLUMN];
        for (int j=0; j<SHELF_COLUMN; j++) {
            values[j]=0;
            for (int i = 0; i < SHELF_ROW; i++)
                if (this.bookshelf[i][j].getType() == NOTHING)
                    values[j]++;
        }
        return Arrays.stream(values).max().getAsInt();
    }

    /**
     * <p>
     *     Method that gives information about the completion of the bookshelf.
     * </p>
     * @return a boolean: <b>true</b> if the bookshelf is <i>full</i>, <b>false</b> otherwise*/
    public boolean getStatus() {
        Tile[][] bookshelf = this.getBookshelf();
        for (int i = 0; i<SHELF_ROW; i++){
            for (int j = 0; j<SHELF_COLUMN; j++) {
                if(bookshelf[i][j].getType()== NOTHING)
                    return false;
            }
        }
        return true;
    }

    /**
     * <p>
     *     Method that sets a bookshelf as full.
     * </p>*/
    public void setAsFull() {
        this.isFull=true;
        }

    /*The method gets invoked by player during their turn, tilesInHand is the list of tiles that the player wishes to
    * add to the columnToBeFilled. The method will throw an exception if the column is already full or if there isn't enough
    * space in the column */

    /**
     * <p>
     *     Method used to place a tile in a column using the given parameters.<br>
     *     This methods also notifies the Observers that a tile has been added to te bookshelf,
     *     therefore the bookshelf has changed.
     * </p>
     * @param columnToBeFilled is an <code>int</code> that represents a coordinate in between 0 and 4;
     * @param tile is the tile that has to be added to the bookshelf
     * */
    public void placeTile (int columnToBeFilled, Tile tile) {
        for(int i=SHELF_ROW-1;i>=0; i--){
            if(bookshelf[i][columnToBeFilled].getType() == NOTHING){
                bookshelf[i][columnToBeFilled] = tile;
                TileForMessages tileForMessages = new TileForMessages(this, i, columnToBeFilled, tile);
                notifyObservers(new Message(tileForMessages, Event.SET_TILE_BOOKSHELF));
                i = -1;
            }
        }
    }

    /**
     * <p>
     *     Method that returns the bookshelf.
     * </p>
     * @return a matrix of Tile that represents the bookshelf*/
    public Tile[][] getBookshelf(){
        return this.bookshelf;
    }

    /**
     * <p>
     *     Method that sets a Bookshelf given a matrix of Tile.
     * </p>
     * @param bks a matrix of Tile*/
    public void setBookshelf(Tile[][] bks){ this.bookshelf = bks; }

}

