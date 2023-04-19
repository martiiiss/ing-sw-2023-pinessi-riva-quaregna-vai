package model;

import java.util.ArrayList;
import java.util.Arrays;

import static model.Type.NOTHING;

public class Bookshelf {

    private Tile[][] bookshelf;  // Bookshelf [5][6]
    private static final int SHELF_ROW = 6;
    private static final int SHELF_COLUMN = 5;
    private boolean isFull = false;

    //constructor -> it initializes the bookshelf at NOTHING tiles, without this I cannot test the Player class
    public Bookshelf(){
        this.bookshelf = new Tile[SHELF_ROW][SHELF_COLUMN];
        for (int i=0;i<SHELF_ROW;i++){
            for(int j=0; j<SHELF_COLUMN;j++){
                bookshelf[i][j] = new Tile(NOTHING,0);
            }
        }
    }

    public int getNumOfFreeSlots() {
        int values[] = new int[SHELF_COLUMN];
        for (int j=0; j<SHELF_COLUMN; j++) {
            values[j]=0;
            for (int i = 0; i < SHELF_ROW; i++)
                if (this.bookshelf[i][j].getType() == NOTHING)
                    values[j]++;
        }
        return Arrays.stream(values).max().getAsInt();
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
       int j = 0;
        try{
            for(int i=SHELF_ROW-1; i>-1; i--){
                if(tilesInHand.size()==j)
                    break;
                if(bookshelf[i][columnToBeFilled].getType()==NOTHING)
                    bookshelf[i][columnToBeFilled]=tilesInHand.get(j);
                j++;
            }
            throw new NotEnoughSlotsAvailableException("Not enough slots available!");
        } catch (NotEnoughSlotsAvailableException ex) {
        }//Exception already checked
    }
    public Tile[][] getBookshelf(){
        return this.bookshelf;
    }

    public void setBookshelf(Tile[][] bks){ this.bookshelf = bks; }
}

