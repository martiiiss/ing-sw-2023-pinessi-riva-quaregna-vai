package Model;

import model.Bookshelf;
import model.Tile;
import model.Type;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

public class BookshelfTest {

    Bookshelf bks = new Bookshelf();
    ArrayList<Tile> tiles = new ArrayList<>();


    @Test
    void setAsFullTest() {
        bks.setAsFull();
        assertTrue(bks.getStatus());
    }

    @Test
    void placeTileTest() {
        int columns, i, j;
        j = 0;
        Tile cat = new Tile(Type.CAT, 1);
        columns = (int) (Math.random() * 5);
        while (bks.getBookshelf()[j + 1][columns].getType() == Type.NOTHING) {
            j++;
            if (j == 5)
                break;
        }
        bks.placeTile(columns, cat);
        assertEquals(cat.getType(), bks.getBookshelf()[j][columns].getType());
    }

}
