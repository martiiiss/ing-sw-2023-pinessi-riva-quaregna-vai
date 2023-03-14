package model;

import jdk.jshell.spi.ExecutionControl;


import java.util.ArrayList;

public class ModelBookshelf {
    private static final int SHELF_ROW = 6;
    private static final int SHELF_COLUMN = 5;
    private boolean isFull;
    private ModelTile[][] storedTiles;

    public void checkStatus() {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }
    /*The method gets invoked by player during their turn, tilesInHand is the list of tiles that the player wishes to
    * add to the columnToBeFilled. The method will throw an exception if the coulumn is altready full or if there isn't enough
    * space in the column */
    public void fillBookshelf(int columnToBeFilled, ArrayList<ModelTile> tilesInHand) {
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
    }
}

