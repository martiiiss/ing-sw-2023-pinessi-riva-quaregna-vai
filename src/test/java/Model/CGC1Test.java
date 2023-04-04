package Model;

import model.Type;
import model.Tile;
import model.CGC1;
import model.Bookshelf;

import static org.junit.jupiter.api.Assertions.*;


public class CGC1Test {
    private final int MAX_ROW = 6;
    private final int MAX_COLUMN = 5;
    Tile cat = new Tile(Type.CAT,1);
    Tile book = new Tile(Type.BOOK,2);
    Tile game = new Tile(Type.GAME,3);
    Tile frame = new Tile(Type.FRAME,2);
    Tile trophy = new Tile(Type.TROPHY,1);
    Tile plant = new Tile(Type.PLANT,3);
    Tile nothing = new Tile(Type.NOTHING,0);

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

    Tile[][] bookshelf4 = { { cat,   game,   nothing, frame,  plant },
                            { cat,   trophy, nothing, trophy, cat},
                            { plant, cat,   trophy,  trophy, game},
                            { frame, game,   frame,   frame,   frame},
                            { plant, book,   frame,   plant,  cat},
                            { plant, plant,  game,     game,   cat}};

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

        Bookshelf bks4 = new Bookshelf();
        bks4.setBookshelf(bookshelf4);
        assertTrue(cgc1.compareRule(bks4,1));
    }

}

