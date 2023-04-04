package Model;

import model.*;
import model.Type;
import model.Tile;
import model.Bookshelf;
import model.CGC5;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class CGC5Test {
    private final int MAX_ROW = 6;
    private final int MAX_COLUMN = 5;
    Tile cat = new Tile(Type.CAT, 1);
    Tile book = new Tile(Type.BOOK, 2);
    Tile game = new Tile(Type.GAME, 2);
    Tile frame = new Tile(Type.FRAME, 3);
    Tile trophy = new Tile(Type.TROPHY, 2);
    Tile plant = new Tile(Type.PLANT, 1);
    Tile nothing = new Tile(Type.NOTHING,0);

    //This bookshelf checks that if there are more than 6 groups it get's refused
    Tile[][] bookshelf1 = { { cat, nothing, frame, game, game },
            { cat, nothing, frame, plant, plant},
            { frame, trophy, cat, trophy, trophy},
            { frame, trophy, cat, book, book},
            { game, book, plant, frame, frame},
            { game, book, plant, cat, cat}};

    //This bookshelf has a big group that will be counted only once, it won't be accepted (there are only 2 groups)
    Tile[][] bookshelf2 = { { cat, frame, frame, frame, frame },
            { cat, plant, plant, plant, nothing},
            { cat, nothing, plant, nothing, nothing},
            { cat, nothing, nothing, nothing, nothing},
            { cat, cat, plant, plant, cat},
            { cat, cat, cat, plant, plant}};

    //This  bookshelf will be accepted, the number of groups is 4 but their size is bigger than 2 tiles
    Tile[][] bookshelf3 = { { cat, cat, nothing, frame, frame },
            { cat, trophy, trophy, trophy, frame},
            { plant, game, trophy, trophy, frame},
            { plant, frame, plant, plant, frame},
            { plant, book, plant, plant, frame},
            { plant, plant, cat, plant, cat}};
    private final CGC5 cgc5 = new CGC5();

    @Test
    void compareRule() {
        Bookshelf bks1 = new Bookshelf();
        bks1.setBookshelf(bookshelf1);
        assertFalse(cgc5.compareRule(bks1, 1));

        Bookshelf bks2 = new Bookshelf();
        bks2.setBookshelf(bookshelf2);
        assertTrue(cgc5.compareRule(bks2,1));

        Bookshelf bks3 = new Bookshelf();
        bks3.setBookshelf(bookshelf3);
        assertTrue(cgc5.compareRule(bks3,1));

    }
}
