package Model;

import model.Board;
import static model.Type.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardTest {
    private final Board board = new Board();

    @Test
    void setUpBoard() {
    }

    @Test
    void initializeBoard() {
        board.initializeBoard(2);

    }

    @Test
    void setNumOfCells() {

    }

    @Test
    void removeTile() {
        Assertions.assertEquals(NOTHING, board.removeTile(2,5).getType());

    }

    @Test
    void getBoard() {
    }
}