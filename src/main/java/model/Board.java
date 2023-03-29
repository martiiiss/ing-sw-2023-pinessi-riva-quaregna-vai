package model;


import jdk.jshell.spi.ExecutionControl;
import java.util.ArrayList;

public class Board {
    private int numOfCells;
    private final int  BOARD_ROW = 9;
    private final int  BOARD_COLUMN = 9;
    private Tile[][] livingRoomBoard;



    public Board(){ //constructor
        this.livingRoomBoard = new Tile[BOARD_ROW][BOARD_COLUMN];
    }

    public void setUpBoard(ArrayList<Tile> tilesToPutOnBoard) { //add tiles to the board.
        int iTiles=0;
        for(int i=0; i<BOARD_ROW; i++){
            for(int j=0; j<BOARD_COLUMN; j++){
                if(livingRoomBoard[i][j].getType()==Type.NOTHING) { // no tile in this cell --> put a tile
                    livingRoomBoard[i][j] = tilesToPutOnBoard.get(iTiles);
                    iTiles++;
                }
            }
        }
    }
    /** fixed creating a variable Tile blocked which I insert every time that that cell has to be blocked*/

    public void initializeBoard(int nOfPlayers){ // at the beginning
        // set cells (livingRoomBoard[][]) that cannot be used during the game to BLOCKED
        Tile blocked = new Tile();
        blocked.setType(Type.BLOCKED);
        //set delle celle sempre blocked (azzurre)
        for(int r=0; r<=3; r++){
            for(int c=0; c<=2; c++){
                if(r==0 || r==1 || (r==2 && c<=1) || (r==3 && c==0)){
                    livingRoomBoard[r][c]=blocked;
                    livingRoomBoard[c][BOARD_COLUMN-1-r]=blocked;
                    livingRoomBoard[BOARD_ROW-1-r][BOARD_COLUMN-1-c]=blocked;
                    livingRoomBoard[BOARD_ROW-1-c][r]=blocked;
                }
            }
        }

        if(nOfPlayers<=3){ //block cell 4
            for(int i=0; i<=1; i++){
                for(int j=4; j<=5; j++){
                    livingRoomBoard[i][j]=blocked;
                    livingRoomBoard[BOARD_ROW-1-j][i]=blocked;
                    livingRoomBoard[j][BOARD_COLUMN-1-i]=blocked;
                    livingRoomBoard[BOARD_ROW-1-i][BOARD_COLUMN-1-j]=blocked;
                }
            }
        }

        if(nOfPlayers==2) { //block cell 3
            for (int i = 0; i <= 2; i+=2) {
                for (int j = 3; j >=2; j--) {
                    livingRoomBoard[i][j]=blocked;
                    livingRoomBoard[BOARD_ROW-1-j][i]=blocked;
                    livingRoomBoard[j][BOARD_COLUMN-1-i]=blocked;
                    livingRoomBoard[BOARD_ROW-1-i][BOARD_COLUMN-1-j]=blocked;

                }
            }
        }
    }

    public void setNumOfCells(int nOfPlayers) { //at the beginning
        //set numOfCell that can be used during the game (depends on nOfPlayers)
        switch(nOfPlayers){
            case 2:
                this.numOfCells = 29;
                break;

            case 3:
                this.numOfCells = 37;
                break;

            case 4:
                this.numOfCells = 45;
                break;
        }
    }

    /** Changed the method having it return the tile (UML)**/

    public Tile removeTile(int row, int column) { //remove the tile in (row, column) --> NOTHING
        Tile removedTile = livingRoomBoard[row][column];
        livingRoomBoard[row][column].setType(Type.NOTHING);
        System.out.println();
        return removedTile;
    }

    Tile[][] getBoard(){
        return this.livingRoomBoard;
    }
}
