package Model;

import model.Bag;
import model.Board;
import model.Tile;
import static model.Type.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class BoardTest {
    Tile nothing = new Tile(NOTHING, 0);
    Tile blocked = new Tile(BLOCKED, 0);
    private Board board = new Board(2);
    private Tile[][] board2Players = {{ blocked, blocked, blocked, blocked, blocked, blocked, blocked, blocked, blocked},
                                       { blocked, blocked, blocked, nothing, nothing, blocked, blocked, blocked, blocked},
                                       { blocked, blocked, blocked, nothing, nothing, nothing, blocked, blocked, blocked},
                                       { blocked, blocked, nothing, nothing, nothing, nothing, nothing, nothing, blocked},
                                       { blocked, nothing, nothing, nothing, nothing, nothing, nothing, nothing, blocked},
                                       { blocked, nothing, nothing, nothing, nothing, nothing, nothing, blocked, blocked},
                                       { blocked, blocked, blocked, nothing, nothing, nothing, blocked, blocked, blocked},
                                       { blocked, blocked, blocked, blocked, nothing, nothing, blocked, blocked, blocked},
                                       { blocked, blocked, blocked, blocked, blocked, blocked, blocked, blocked, blocked}};

    private Tile[][] board3Players = {{ blocked, blocked, blocked, nothing, blocked, blocked, blocked, blocked, blocked},
                                      { blocked, blocked, blocked, nothing, nothing, blocked, blocked, blocked, blocked},
                                      { blocked, blocked, nothing, nothing, nothing, nothing, nothing, blocked, blocked},
                                      { blocked, blocked, nothing, nothing, nothing, nothing, nothing, nothing, nothing},
                                      { blocked, nothing, nothing, nothing, nothing, nothing, nothing, nothing, blocked},
                                      { nothing, nothing, nothing, nothing, nothing, nothing, nothing, blocked, blocked},
                                      { blocked, blocked, nothing, nothing, nothing, nothing, nothing, blocked, blocked},
                                      { blocked, blocked, blocked, blocked, nothing, nothing, blocked, blocked, blocked},
                                      { blocked, blocked, blocked, blocked, blocked, nothing, blocked, blocked, blocked}};

    private Tile[][] board4Players = {{ blocked, blocked, blocked, nothing, nothing, blocked, blocked, blocked, blocked},
                                      { blocked, blocked, blocked, nothing, nothing, nothing, blocked, blocked, blocked},
                                      { blocked, blocked, nothing, nothing, nothing, nothing, nothing, blocked, blocked},
                                      { blocked, nothing, nothing, nothing, nothing, nothing, nothing, nothing, nothing},
                                      { nothing, nothing, nothing, nothing, nothing, nothing, nothing, nothing, nothing},
                                      { nothing, nothing, nothing, nothing, nothing, nothing, nothing, nothing, blocked},
                                      { blocked, blocked, nothing, nothing, nothing, nothing, nothing, blocked, blocked},
                                      { blocked, blocked, blocked, nothing, nothing, nothing, blocked, blocked, blocked},
                                      { blocked, blocked, blocked, blocked, nothing, nothing, blocked, blocked, blocked}};

    private final Bag bag = new Bag();

    Tile[][] livingRoom2;
    ArrayList<Tile> tiles = new ArrayList<Tile>();

    private void setArraylist (int num){
        tiles = bag.getBagTiles(num);
    }


    @Test
    void setUpBoardTest() {
        int e=0;
        setArraylist(board.getNumOfCells());
        board.setUpBoard(tiles);
        livingRoom2 = board.getBoard();
        for(int i=0; i< board.BOARD_ROW; i++){
            for(int j=0; j< board.BOARD_COLUMN; j++){
                if(livingRoom2[i][j].getType() != BLOCKED){
                    assertEquals(livingRoom2[i][j].getType(), tiles.get(e).getType());
                    assertEquals(livingRoom2[i][j].getNumType(), tiles.get(e).getNumType());
                    e++;
                }
            }
        }
    }

    @Test
    void initializeBoard() {
        board.initializeBoard(2);
        for(int i=0; i<board.BOARD_ROW; i++){
            for(int j=0; j<board.BOARD_COLUMN; j++){
                if(board2Players[i][j].getType()==BLOCKED)
                    assertEquals(BLOCKED, board.getBoard()[i][j].getType());
            }
        }
        board.initializeBoard(3);
        for(int i=0; i<board.BOARD_ROW; i++){
            for(int j=0; j<board.BOARD_COLUMN; j++){
                if(board3Players[i][j].getType()==BLOCKED)
                    assertEquals(BLOCKED, board.getBoard()[i][j].getType());
            }
        }
        board.initializeBoard(4);
        for(int i=0; i<board.BOARD_ROW; i++){
            for(int j=0; j<board.BOARD_COLUMN; j++){
                if(board4Players[i][j].getType()==BLOCKED)
                    assertEquals(BLOCKED, board.getBoard()[i][j].getType());
            }
        }
    }

    @Test
    void setNumOfCells() {
        board.setNumOfCells(2);
        assertEquals(29, board.getNumOfCells());

        board.setNumOfCells(3);
        assertEquals(37, board.getNumOfCells());

        board.setNumOfCells(4);
        assertEquals(45, board.getNumOfCells());
    }

    @Test
    void removeTileTest() {
        setArraylist(board.getNumOfCells());
        board.setUpBoard(tiles);
        for(int i=0; i< board.BOARD_ROW; i++){
            for(int j=0; j< board.BOARD_COLUMN; j++){
                if(board.getBoard()[i][j].getType()!=BLOCKED) {
                    Tile take = board.getBoard()[i][j];
                    assertEquals(take, board.removeTile(i, j));
                    assertEquals(NOTHING, board.getBoard()[i][j].getType());
                }
            }
        }
    }

    @Test
    void getBoard() {
        setArraylist(board.getNumOfCells());
        board.setUpBoard(tiles);
        int e = 0;

        for (int i = 0; i < board.BOARD_ROW; i++) {
            for (int j = 0; j < board.BOARD_COLUMN; j++) {
                if (board.getBoard()[i][j].getType() != BLOCKED) {
                    assertEquals(board.getBoard()[i][j].getType(), tiles.get(e).getType());
                    assertEquals(board.getBoard()[i][j].getNumType(), tiles.get(e).getNumType());
                    e++;
                }
            }
        }
    }
}