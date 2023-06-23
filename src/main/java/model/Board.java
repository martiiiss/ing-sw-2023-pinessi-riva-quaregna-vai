package model;

import distributed.messages.Message;
import util.Event;
import util.Observable;
import util.TileForMessages;

import javax.sound.midi.SysexMessage;
import java.io.Serial;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**This class represents the Board of the game*/
public class Board extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = 4758892564965792652L;
    private int numOfCells;
    public final int  BOARD_ROW = 9;
    public final int  BOARD_COLUMN = 9;
    private final Tile[][] livingRoomBoard;

    /**
     * <p>
     *     Constructor of the class Board.
     *     It initializes the livingRoomBoard by calling:<br>
     *     - <code>setNumOfCells(int numOfPlayers)</code> <br>
     *     - <code>initializeBoard(int numOfPlayers)</code>
     *     methods.
     * </p>
     * @param numOfPlayers an integer, it represents the number of players of the Game
     *
     * */
    public Board(int numOfPlayers){
        this.livingRoomBoard = new Tile[BOARD_ROW][BOARD_COLUMN];
        setNumOfCells(numOfPlayers);
        initializeBoard(numOfPlayers);
    }

    /**
     * <p>
     *     This method is used to add a specific number of tiles to the board.
     *     After the board has been filled the observers are notified with a message,
     *     stating that <code>livingRoomBoard</code> has been modified.
     * </p>
     *
     * @param tilesToPutOnBoard is an <code>ArrayList</code> of Tile that represents the tile that will be added to the Board
     *
     * */
    public void setUpBoard(ArrayList<Tile> tilesToPutOnBoard) {
        int iTiles=0;
        for(int i=0; i<BOARD_ROW; i++){
            for(int j=0; j<BOARD_COLUMN; j++){
                if(this.livingRoomBoard[i][j].getType()==Type.NOTHING){
                    this.livingRoomBoard[i][j] = tilesToPutOnBoard.get(iTiles);
                    setChanged();
                    iTiles++;
                }
            }
        }
        notifyObservers(new Message(this, Event.SET_UP_BOARD));
    }

    /**
     * <p>
     *     This method is called by the constructor <code>Board(int numOfPlayers)</code>.
     *     Based on the number of players it sets the disposition of the blocked tiles.
     *     It also fills the board (where there are no <b>blocked</b> tiles) with <b>nothing</b> tiles.
     * </p>
     *
     * @param nOfPlayers an integer that represents the number of players
     */
    public void initializeBoard(int nOfPlayers){
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

        if(nOfPlayers<=3){
            int i=0, j=4;
            while(i<=1 && j<=5){
                    livingRoomBoard[i][j]=blocked;
                    livingRoomBoard[BOARD_ROW-1-j][i]=blocked;
                    livingRoomBoard[j][BOARD_COLUMN-1-i]=blocked;
                    livingRoomBoard[BOARD_ROW-1-i][BOARD_COLUMN-1-j]=blocked;
                    i++;
                    j++;
                }
        }

        if(nOfPlayers==2) {
            int i=0, j= 3;
            while(i<=2 && j>=2){
                livingRoomBoard[i][j]=blocked;
                livingRoomBoard[BOARD_ROW-1-j][i]=blocked;
                livingRoomBoard[j][BOARD_COLUMN-1-i]=blocked;
                livingRoomBoard[BOARD_ROW-1-i][BOARD_COLUMN-1-j]=blocked;
                i+=2;
                j--;
            }
        }

        Tile nothing = new Tile(Type.NOTHING,0);
        for (int i=0; i<BOARD_ROW; i++) {
            for (int j = 0; j < BOARD_COLUMN; j++) {
                if (livingRoomBoard[i][j] == null) {
                    livingRoomBoard[i][j] = nothing;
                }
            }
        }
    }

    /**
     * <p>
     *     This method sets the number of cells that can be used during the game (using the nOfPlayers).<br>
     * </p>
     *
     * @param nOfPlayers an integer that represents the number of players in game
     * */

    public void setNumOfCells(int nOfPlayers) {
            switch (nOfPlayers) {
            case 2 -> this.numOfCells = 29;
            case 3 -> this.numOfCells = 37;
            case 4 -> this.numOfCells = 45;
        }
    }

    /**
     * <p>
     *     Given two parameters (the coordinates of the tile that has to be removed), this method
     *     returns the Tile that has been selected and substitutes the tile with a <b>nothing</b> tile. <br>
     *     After a tile has been removed the observers are notified with a message.
     * </p>
     * @param column is an integer that represents the coordinate of the column
     * @param row is an integer that represents the coordinate of the row
     *
     * @return a Tile
     * */

    public Tile removeTile(int row, int column) {
        Tile removedTile = livingRoomBoard[row][column];
        livingRoomBoard[row][column] = new Tile(Type.NOTHING, 0);
        setChanged();
        TileForMessages tileForMessages = new TileForMessages(this, row, column, null);
        System.out.println("lancio notify in removeTile (model)");
        this.notifyObservers(new Message(tileForMessages, Event.REMOVE_TILE_BOARD));
        return removedTile;
    }

    /**
     * <p>
     *     Method used to get the disposition of the tiles on the Board.
     * </p>
     *
     * @return a matrix of tiles
     * */
    public Tile[][] getBoard(){
        return this.livingRoomBoard;
    }

    /**
     * <p>
     *     Method used to get the number of available cells of the Board.
     * </p>
     * @return an integer that represents the number of available cells
     * */
    public int getNumOfCells(){return this.numOfCells;}

    /**
     * <p>
     *     This method returns <b>true</b> if board needs to be filled, <b>false</b> otherwise.
     * </p>
     * */
    public boolean checkBoardStatus(){
        for(int i=0; i<BOARD_ROW; i++){
            for(int j=0; j<BOARD_COLUMN; j++){
                if(livingRoomBoard[i][j].getType()!=Type.BLOCKED && livingRoomBoard[i][j].getType()!=Type.NOTHING){ //in this cell there is a tile
                    if((j+1<BOARD_COLUMN && livingRoomBoard[i][j+1].getType()!=Type.NOTHING && livingRoomBoard[i][j+1].getType()!=Type.BLOCKED) ||
                                (i+1<BOARD_ROW && livingRoomBoard[i+1][j].getType()!=Type.NOTHING && livingRoomBoard[i+1][j].getType()!=Type.BLOCKED)){
                        return false;
                    }
                }
            }
        }
       return true;
    }

    /**
     * <p>
     *     This method returns the Type of a selected Tile
     *     (selected using the two parameters <code>row</code> and <code>column</code>).
     * </p>
     *
     * @param column is an integer that represents the coordinate of the column
     * @param row is an integer that represents the coordinate of the row
     * @return the Type of the selected tile
     * */
    public Type getSelectedType(int row, int column){
        return (livingRoomBoard[row][column]).getType();
    }

    /**
     * <p>
     *     Method that returns the number of the selected Tile Type
     *     (selected using the two parameters <code>row</code> and <code>column</code>).
     * </p>
     * @param column is an integer that represents the coordinate of the column
     * @param row is an integer that represents the coordinate of the row
     * @return an integer in between 0 and 3
     * */
    public int getSelectedNumType(int row, int column) {return (livingRoomBoard[row][column]).getNumType();}
}
