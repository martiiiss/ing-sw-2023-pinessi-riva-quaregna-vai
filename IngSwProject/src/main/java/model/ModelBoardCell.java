package model;


import jdk.jshell.spi.ExecutionControl;

public class ModelBoardCell {
    private int boardCellRow;
    private int boardCellColumn;
    private boolean emptyCell;
    private ModelTile tile;
    private ModelStatus status;

    public ModelStatus checkAvailable(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return null;
    } /*returns the status of the board cell*/
    public ModelTile getTile(){
        try{/*code*/ throw new ExecutionControl.NotImplementedException("Method not yet implemented");} catch (Exception ex) {System.out.println("Method not yet implemented");}
        return null;
    } /*returns the Tile associated with this cell*/
}
