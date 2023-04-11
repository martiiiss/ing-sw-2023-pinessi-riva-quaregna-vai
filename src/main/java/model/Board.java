package model;

import java.util.ArrayList;

public class Board {
    private int numOfCells;
    public final int  BOARD_ROW = 9;
    public final int  BOARD_COLUMN = 9;
    private Tile[][] livingRoomBoard;

    public Board(int numOfPlayers){ //constructor
        this.livingRoomBoard = new Tile[BOARD_ROW][BOARD_COLUMN];
        setNumOfCells(numOfPlayers);
        initializeBoard(numOfPlayers);
    }


    public void setUpBoard(ArrayList<Tile> tilesToPutOnBoard) { //add tiles to the board.
        int iTiles=0;
        for(int i=0; i<BOARD_ROW; i++){
            for(int j=0; j<BOARD_COLUMN; j++){
                if(this.livingRoomBoard[i][j].getType()==Type.NOTHING){
                    this.livingRoomBoard[i][j] = tilesToPutOnBoard.get(iTiles);
                    iTiles++;
                }
            }
        }
    }
    /** fixed creating a variable Tile blocked which I insert every time that that cell has to be blocked*/

    public void initializeBoard(int nOfPlayers){ // at the beginning
        // set cells (livingRoomBoard[][]) that cannot be used during the game to BLOCKED
        Tile blocked = new Tile(Type.BLOCKED,0);
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
        Tile nothing = new Tile(Type.NOTHING,0);
        for (int i=0; i<BOARD_ROW; i++)
            for (int j=0; j<BOARD_COLUMN; j++)
                if(livingRoomBoard[i][j]==null)
                    livingRoomBoard[i][j]=nothing;
    }

    public void setNumOfCells(int nOfPlayers) { //at the beginning
        //set numOfCell that can be used during the game (depends on nOfPlayers)
        switch (nOfPlayers) {
            case 2 -> this.numOfCells = 29;
            case 3 -> this.numOfCells = 37;
            case 4 -> this.numOfCells = 45;
        }
    }

    /** Changed the method having it return the tile (UML)**/

    public Tile removeTile(int row, int column) { //remove the tile in (row, column) --> NOTHING
        Tile removedTile = livingRoomBoard[row][column];
        livingRoomBoard[row][column] = new Tile(Type.NOTHING, 0);
        return removedTile;
    }

    public Tile[][] getBoard(){
        return this.livingRoomBoard;
    }

    public int getNumOfCells(){return this.numOfCells;}

    public boolean checkBoardStatus(){
       /*Scrivere il codice in modo che ritorni vero se la board deve essere riempita, falso altrimenti*/
       return false;
    }//TODO implement this function
}
