package model;


import jdk.jshell.spi.ExecutionControl;
import java.util.ArrayList;

public class Board {
    private boolean isBoardEmpty;
    private int numOfCells;
    private Tile[][] livingRoomBoard = new Tile[9][9];


    public void setUpBoard(ArrayList<Tile> tilesToPutOnBoard) {
        int iTiles=0;
        for(int i=1; i<=9; i++){
            for(int j=1; j<=9; j++){
                if(livingRoomBoard[i][j].getType()==Type.NOTHING) { // no tile in this cell --> put a tile
                    livingRoomBoard[i][j] = tilesToPutOnBoard.get(iTiles);
                    iTiles++;
                }
            }
        }
    }
    /** fixed creating a variable Tile blocked which I insert every time that that cell has to be blocked*/

    public void setNumOfCells(int nOfPlayers) { //at the beginning
        //set numOfCell that can be used during the game (depends on nOfPlayers)
        // set cells (livingRoomBoard[][]) that cannot be used during the game to BLOCKED
        Tile blocked = new Tile();
        blocked.setType(Type.BLOCKED);
        switch(nOfPlayers){
            case 2:
                this.numOfCells = 29;
                for(int r=1; r<=9; r++){
                    for(int c=1; c<=9; c++){
                        if(r==1 || r==9){
                            livingRoomBoard[r][c]=blocked;
                        } else if(r==2 && c!=4 && c!=5) {
                            livingRoomBoard[r][c]=blocked;
                        } else if(r==8 && c!=5 && c!=6){
                            livingRoomBoard[r][c]=blocked;
                        } else if((r==3 || r==7) && (c!=4 && c!=5 && c!=6)){
                            livingRoomBoard[r][c]=blocked;
                        } else if(r==4 && (c==1 || c==2 || c==9)){
                            livingRoomBoard[r][c]=blocked;
                        } else if(r==5 && (c==1 || c==9)){
                            livingRoomBoard[r][c]=blocked;
                        } else if (r==6 && (c==1 || c==9 || c==8)){
                            livingRoomBoard[r][c]=blocked;
                        }
                    }
                }
                break;

            case 3:
                this.numOfCells = 37;
                for(int r=1; r<=9; r++){
                    for(int c=1; c<=9; c++){
                        if(r==1 && c!=4){
                            livingRoomBoard[r][c]=blocked;
                        } else if(r==2 && c!=4 && c!=5) {
                            livingRoomBoard[r][c]=blocked;
                        } else if(r==8 && c!=5 && c!=6){
                            livingRoomBoard[r][c]=blocked;
                        } else if((r==3 || r==7) && (c==1 || c==2 || c==8 || c==9)){
                            livingRoomBoard[r][c]=blocked;
                        } else if(r==4 && (c==1 || c==2)){
                            livingRoomBoard[r][c]=blocked;
                        } else if(r==5 && (c==1 || c==9)){
                            livingRoomBoard[r][c]=blocked;
                        } else if (r==6 && (c==9 || c==8)){
                            livingRoomBoard[r][c]=blocked;
                        } else if(r==9 && c!=6){
                            livingRoomBoard[r][c]=blocked;
                        }
                    }
                }
                break;

            case 4:
                this.numOfCells = 45;
                for(int r=1; r<=9; r++){
                    for(int c=1; c<=9; c++){
                        if(r==1 && c!=4 && c!=5){
                            livingRoomBoard[r][c]=blocked;
                        } else if((r==2 || r==8) && c!=4 && c!=5 && c!=6) {
                            livingRoomBoard[r][c]=blocked;
                        } else if((r==3 || r==7) && (c==1 || c==2 || c==8 || c==9)){
                            livingRoomBoard[r][c]=blocked;
                        } else if(r==4 && c==1){
                            livingRoomBoard[r][c]=blocked;
                        } else if (r==6 && c==9){
                            livingRoomBoard[r][c]=blocked;
                        } else if(r==9 && c!=5 && c!=6){
                            livingRoomBoard[r][c]=blocked;
                        }
                    }
                }
                break;
        }
    }

    public boolean checkStatus() { //if Board is empty return TRUE
        return this.isBoardEmpty;
    }

    /** Changed the method having it return the tile (UML)**/

    public Tile removeTile(int row, int column) { //remove the tile in (row, column) --> NOTHING
        Tile removedTile = livingRoomBoard[row][column];
        livingRoomBoard[row][column].setType(Type.NOTHING);
        System.out.println();
        return removedTile;
    }
}
