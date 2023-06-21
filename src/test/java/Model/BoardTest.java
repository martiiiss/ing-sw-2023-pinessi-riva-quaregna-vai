package Model;

import model.Bag;
import model.Board;
import model.Tile;
import static model.Type.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

class BoardTest {
    Tile nothing = new Tile(NOTHING, 0);
    Tile blocked = new Tile(BLOCKED, 0);
    Tile cat = new Tile(CAT, 1);
    Tile gameTile = new Tile(GAME, 1);
    Tile frame = new Tile(FRAME, 1);
    Tile book = new Tile(BOOK, 1);
    Tile plant = new Tile(PLANT, 1);
    Tile trophy = new Tile(TROPHY, 1);

    private Board board = new Board(2);
    private Board board4 = new Board(4);
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

    private Tile[][] boardInGame4Players = {{ blocked, blocked, blocked, nothing, nothing, blocked,  blocked, blocked, blocked},
                                            { blocked, blocked, blocked, cat,     nothing, nothing,  blocked, blocked, blocked},
                                            { blocked, blocked, nothing, nothing, book,    nothing,  nothing, blocked, blocked},
                                            { blocked, nothing, nothing, nothing, nothing, nothing,  nothing, nothing, frame  },
                                            { nothing, nothing, nothing, nothing, nothing, nothing,  nothing, nothing, plant  },
                                            { nothing, nothing, nothing, nothing, nothing, nothing,  nothing, nothing, blocked},
                                            { blocked, blocked, nothing, trophy,  nothing, nothing,  nothing, blocked, blocked},
                                            { blocked, blocked, blocked, nothing, nothing, gameTile, blocked, blocked, blocked},
                                            { blocked, blocked, blocked, blocked, nothing, nothing,  blocked, blocked, blocked}};

    private Tile[][] boardInGame4Players2 = {{ blocked, blocked, blocked, nothing, nothing, blocked,  blocked, blocked, blocked},
                                             { blocked, blocked, blocked, cat,     nothing, nothing,  blocked, blocked, blocked},
                                             { blocked, blocked, nothing, nothing, book,    nothing,  nothing, blocked, blocked},
                                             { blocked, nothing, nothing, nothing, nothing, nothing,  nothing, nothing, frame  },
                                             { nothing, nothing, nothing, nothing, nothing, nothing,  nothing, nothing, nothing},
                                             { nothing, nothing, nothing, nothing, nothing, nothing,  nothing, nothing, blocked},
                                             { blocked, blocked, nothing, trophy,  nothing, nothing,  nothing, blocked, blocked},
                                             { blocked, blocked, blocked, nothing, nothing, gameTile, blocked, blocked, blocked},
                                             { blocked, blocked, blocked, blocked, nothing, plant  ,  blocked, blocked, blocked}};

    private Tile[][] boardInGame4Players3 = {{ blocked, blocked, blocked, nothing, nothing, blocked,  blocked, blocked, blocked},
                                             { blocked, blocked, blocked, cat,     nothing, nothing,  blocked, blocked, blocked},
                                             { blocked, blocked, nothing, nothing, book,    nothing,  nothing, blocked, blocked},
                                             { blocked, nothing, nothing, nothing, nothing, nothing,  nothing, nothing, frame  },
                                             { nothing, nothing, nothing, nothing, nothing, nothing,  nothing, nothing, nothing},
                                             { nothing, nothing, nothing, nothing, nothing, nothing,  nothing, nothing, blocked},
                                             { blocked, blocked, nothing, trophy,  nothing, nothing,  nothing, blocked, blocked},
                                             { blocked, blocked, blocked, nothing, nothing, nothing, blocked, blocked, blocked},
                                             { blocked, blocked, blocked, blocked, nothing, plant  ,  blocked, blocked, blocked}};

    private final Bag bag = new Bag();

    Tile[][] livingRoom2;
    ArrayList<Tile> tiles = new ArrayList<>();

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

    @Test
    void checkBoardStatusTest() {
        for (int i = 0; i < board4.BOARD_ROW; i++) {  //initialize board
            for (int j = 0; j < board4.BOARD_COLUMN; j++) {
                board4.getBoard()[i][j] = boardInGame4Players[i][j];
            }
        }
        assertFalse(board4.checkBoardStatus());


        for (int i = 0; i < board4.BOARD_ROW; i++) {  //initialize board
            for (int j = 0; j < board4.BOARD_COLUMN; j++) {
                board4.getBoard()[i][j] = boardInGame4Players2[i][j];
            }
        }
        assertFalse(board4.checkBoardStatus());

        for (int i = 0; i < board4.BOARD_ROW; i++) {  //initialize board
            for (int j = 0; j < board4.BOARD_COLUMN; j++) {
                board4.getBoard()[i][j] = boardInGame4Players3[i][j];
            }
        }
        assertTrue(board4.checkBoardStatus()); //board must be filled
    }
}