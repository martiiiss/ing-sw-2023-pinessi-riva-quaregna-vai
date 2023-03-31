package model;

import jdk.jshell.spi.ExecutionControl;


import java.util.ArrayList;

import static model.Type.NOTHING;

public class Bookshelf {

    private Tile[][] bookshelf;  // Bookshelf [5][6]
    private static final int SHELF_ROW = 6;
    private static final int SHELF_COLUMN = 5;
    private boolean isFull = false;

    //constructor
    public Bookshelf(){
        this.bookshelf = new Tile[SHELF_ROW][SHELF_COLUMN];
    }

    public boolean getStatus() {
        return isFull;
    }

    public void setAsFull() {
        this.isFull=true;
        }

    /*The method gets invoked by player during their turn, tilesInHand is the list of tiles that the player wishes to
    * add to the columnToBeFilled. The method will throw an exception if the column is already full or if there isn't enough
    * space in the column */
    public void placeTile (int columnToBeFilled, ArrayList<Tile> tilesInHand) {
        try{
            for(int i=0; i<SHELF_ROW; i++){
                if(bookshelf[i][columnToBeFilled].getType()==NOTHING)
                    bookshelf[i][columnToBeFilled] = tilesInHand.get(i);
            }
            throw new NotEnoughSlotsAvailableException("Not enough slots available!");
        } catch (NotEnoughSlotsAvailableException ex) {}
    }
    public Tile[][] getBookshelf(){
        return this.bookshelf;
    }

    public void setBookshelf(Tile[][] bks){ this.bookshelf = bks; }
}

