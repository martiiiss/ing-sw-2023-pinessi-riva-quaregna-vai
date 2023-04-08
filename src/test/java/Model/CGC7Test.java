package Model;

import model.CGC7;
import org.junit.jupiter.api.Test;
import model.Bookshelf;
import model.Type;
import model.Tile;

import static org.junit.jupiter.api.Assertions.*;

public class CGC7Test {
    private final int MAX_ROW = 6;
    private final int MAX_COLUMN = 5;
    Tile cat = new Tile(Type.CAT, 1);
    Tile book = new Tile(Type.BOOK, 2);
    Tile game = new Tile(Type.GAME, 3);
    Tile frame = new Tile(Type.FRAME, 2);
    Tile trophy = new Tile(Type.TROPHY,2);
    Tile plant = new Tile(Type.PLANT, 1);
    Tile nothing = new Tile(Type.NOTHING, 0);

    //checks if a bookshelf having 2 groups of the same type is accepted
    Tile[][] bookshelf1 = {
        { cat, cat, nothing, book, book },
        { cat, cat, nothing, book, book},
        { nothing, nothing, nothing, nothing, nothing},
        { nothing, nothing, nothing, nothing, nothing},
        { nothing, nothing, nothing, cat, cat},
        { nothing, nothing, nothing, cat, cat}
    };

    Tile[][] bookshelf2 = {
            { nothing, nothing, nothing, nothing, nothing },
            { nothing, nothing, nothing, nothing, nothing},
            { nothing, nothing, nothing, nothing, nothing},
            { cat, nothing, nothing, nothing, nothing},
            { cat, cat, nothing, cat, cat},
            { cat, cat, nothing, cat, cat}
    };

    Tile[][] b8 = {
            {cat,cat,nothing,nothing,cat},
            {cat,cat,game,cat,cat},
            {frame,trophy,game,cat,cat},
            {cat,cat,cat,cat,cat},
            {plant,trophy,book,game,cat},
            {cat,trophy,game,plant,frame}
    };

    private final CGC7 cgc7 = new CGC7();

    @Test
    void compareRule() {
        Bookshelf bks1 = new Bookshelf();
        bks1.setBookshelf(bookshelf1);
        assertTrue(cgc7.compareRule(bks1, 1));

        Bookshelf bks2 = new Bookshelf();
        bks2.setBookshelf(bookshelf2);
        assertFalse(cgc7.compareRule(bks2, 1));

        //This has to be false, there is only one group of 4
        Bookshelf bks3 = new Bookshelf();
        bks3.setBookshelf(b8);
        assertFalse(cgc7.compareRule(bks3,1));

    }
}