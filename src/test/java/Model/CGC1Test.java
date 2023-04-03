package Model;

import model.Type;
import model.Tile;
import model.CGC1;
import model.Bookshelf;

import static org.junit.jupiter.api.Assertions.*;


public class CGC1Test {
    private final int MAX_ROW = 6;
    private final int MAX_COLUMN = 5;
    Tile cat = new Tile(Type.CAT);
    Tile book = new Tile(Type.BOOK);
    Tile game = new Tile(Type.GAME);
    Tile frame = new Tile(Type.FRAME);
    Tile trophy = new Tile(Type.TROPHY);
    Tile plant = new Tile(Type.PLANT);
    Tile nothing = new Tile(Type.NOTHING);

    //This bookshelf checks that if there are more than 6 groups it get's refused
    Tile[][] bookshelf1 = { { cat, nothing, frame, game, game },
                            { cat, nothing, frame, plant, plant},
                            { frame, trophy, cat, trophy, trophy},
                            { frame, trophy, cat, book, book},
                            { game, book, plant, frame, frame},
                            { game, book, plant, cat, cat}};

    //This bookshelf has a big group that will be counted only once, it won't be accepted (there are only 2 groups)
    Tile[][] bookshelf2 = { { cat, nothing, nothing, nothing, nothing },
                            { cat, nothing, nothing, nothing, nothing},
                            { cat, nothing, nothing, nothing, nothing},
                            { cat, nothing, nothing, nothing, nothing},
                            { cat, cat, plant, plant, cat},
                            { cat, cat, cat, cat, cat}};

    //This  bookshelf will be accepted, the number of groups is 4 but their size is bigger than 2 tiles
    Tile[][] bookshelf3 = { { cat, cat, nothing, frame, frame },
                            { cat, trophy, nothing, trophy, frame},
                            { plant, game, trophy, trophy, frame},
                            { plant, frame, plant, plant, frame},
                            { plant, book, plant, plant, cat},
                            { frame, plant, cat, plant, cat}};
    private final CGC1 cgc1 = new CGC1();

    @org.junit.jupiter.api.Test
    void compareRule() {
        Bookshelf bks1 = new Bookshelf();
        bks1.setBookshelf(bookshelf1);
        assertFalse(cgc1.compareRule(bks1, 1));

        Bookshelf bks2 = new Bookshelf();
        bks2.setBookshelf(bookshelf2);
        assertFalse(cgc1.compareRule(bks2,1));

        Bookshelf bks3 = new Bookshelf();
        bks3.setBookshelf(bookshelf3);
        assertTrue(cgc1.compareRule(bks3,1));
    }

}

