package Model;

import static org.junit.jupiter.api.Assertions.*;
import model.Tile;
import model.Type;
import model.CGC10;
import model.Bookshelf;

class CGC10Test {
    Tile cat = new Tile(Type.CAT,1);
    Tile book = new Tile(Type.BOOK,2);
    Tile game = new Tile(Type.GAME,3);
    Tile frame = new Tile(Type.FRAME,2);
    Tile trophy = new Tile(Type.TROPHY,1);
    Tile plant = new Tile(Type.PLANT,3);
    Tile nothing = new Tile(Type.NOTHING,0);

    Tile[][] bookshelfT1 = {
            { cat, nothing, nothing, nothing, cat },
            { plant, cat, game, book, game},
            { frame, trophy, cat, book, game},
            { plant, trophy, cat, cat, cat},
            { game, cat, book, cat, cat},
            { cat, game, cat, plant, cat}
    };
    Tile[][] bookshelfF1 = {
            { cat, nothing, nothing, nothing,  },
            { plant, cat, game, book, game},
            { frame, trophy, cat, book, game},
            { plant, trophy, cat, cat, book},
            { game, cat, book, frame, cat},
            { cat, game, trophy, plant, nothing}
    };
    Tile[][] bookshelfF2 = {
            { cat, nothing, nothing, nothing, cat },
            { plant, cat, game, book, game},
            { frame, trophy, cat, book, game},
            { plant, trophy, nothing, cat, nothing},
            { game, cat, book, nothing, cat},
            { cat, game, nothing, plant, nothing}
    };

    private final CGC10 cgc10 = new CGC10();

    @org.junit.jupiter.api.Test
    void compareRule() {
        Bookshelf bks1 = new Bookshelf();

        bks1.setBookshelf(bookshelfT1);
        assertTrue(cgc10.compareRule(bks1, 10));

        bks1.setBookshelf(bookshelfF1);
        assertFalse(cgc10.compareRule(bks1, 10));

        bks1.setBookshelf(bookshelfF2);
        assertFalse(cgc10.compareRule(bks1, 10));

    }

}