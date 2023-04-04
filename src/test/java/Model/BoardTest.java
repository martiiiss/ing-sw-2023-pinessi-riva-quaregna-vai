package Model;

import model.Bag;
import model.Board;
import model.Tile;
import static model.Type.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class BoardTest {
    private Board board2 = new Board(2);

    private final Bag bag = new Bag();

    Tile[][] livingRoom2;
    ArrayList<Tile> tiles = new ArrayList<Tile>();

    private void setArraylist (){
        tiles = bag.getBagTiles(board2.getNumOfCells());
    }

    @Test
    void setUpBoardTest() {
        int e=0;
        setArraylist();
        livingRoom2 = board2.getBoard();
        board2.setUpBoard(tiles);
        livingRoom2 = board2.getBoard();
        for(int i=0; i< board2.BOARD_ROW; i++){
            for(int j=0; j< board2.BOARD_COLUMN; j++){
                if(livingRoom2[i][j].getType() != BLOCKED){
                    System.out.println(this.livingRoom2[i][j].getType() + " " + this.livingRoom2[i][j].getNumType());
                    assertEquals(livingRoom2[i][j].getType(), tiles.get(e).getType());
                    assertEquals(livingRoom2[i][j].getNumType(), tiles.get(e).getNumType());
                    e++;
                }
            }
        }
    }

    @Test
    void initializeBoard() {
        //board.initializeBoard(2);

    }

    @Test
    void setNumOfCells() {

    }

    @Test
    void removeTile() {
        //? Assertions.assertEquals(NOTHING, board.removeTile(2,5).getType());

    }

    @Test
    void getBoard() {
    }
}