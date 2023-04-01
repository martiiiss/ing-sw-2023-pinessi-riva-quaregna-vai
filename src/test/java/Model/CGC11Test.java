package Model;

import static org.junit.jupiter.api.Assertions.*;

import model.*;
import model.Tile;
import model.Type;
import model.CGC11;
import model.Bookshelf;


class CGC11Test {
    Tile cat = new Tile(Type.CAT);
    Tile book = new Tile(Type.BOOK);
    Tile game = new Tile(Type.GAME);
    Tile frame = new Tile(Type.FRAME);
    Tile trophy = new Tile(Type.TROPHY);
    Tile plant = new Tile(Type.PLANT);
    Tile nothing = new Tile(Type.NOTHING);

    Tile[][] bookshelfT1 = {
            { cat, nothing, nothing, nothing, cat },
            { plant, cat, game, book, game},
            { frame, trophy, cat, book, game},
            { plant, trophy, cat, cat, cat},
            { game, cat, book, cat, cat},
            { cat, game, cat, plant, cat}
    };
    Tile[][] bookshelfF1 = {
            { cat, nothing, nothing, nothing, game },
            { plant, cat, game, book, game},
            { frame, trophy, cat, book, game},
            { plant, trophy, cat, cat, book},
            { game, cat, book, frame, cat},
            { game, game, trophy, plant, nothing}
    };
    Tile[][] bookshelfF2 = {
            { cat, nothing, nothing, nothing, cat },
            { plant, cat, game, book, game},
            { frame, trophy, cat, book, game},
            { nothing, trophy, nothing, nothing, book},
            { game, cat, book, frame, cat},
            { nothing, game, trophy, plant, nothing}
    };

    private final CGC11 cgc11 = new CGC11();

    @org.junit.jupiter.api.Test
    void compareRule() {
        Bookshelf bks1 = new Bookshelf();

        bks1.setBookshelf(bookshelfT1);
        assertTrue(cgc11.compareRule(bks1, 10));

        bks1.setBookshelf(bookshelfF1);
        assertFalse(cgc11.compareRule(bks1, 10));

        bks1.setBookshelf(bookshelfF2);
        assertFalse(cgc11.compareRule(bks1, 10));

    }

}