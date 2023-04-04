package Model;

import static org.junit.jupiter.api.Assertions.*;

import model.*;
import model.Tile;
import model.Type;
import model.CGC49;
import model.Bookshelf;

class CGC49Test {
    Tile cat = new Tile(Type.CAT,1);
    Tile book = new Tile(Type.BOOK,2);
    Tile game = new Tile(Type.GAME,3);
    Tile frame = new Tile(Type.FRAME,2);
    Tile trophy = new Tile(Type.TROPHY,1);
    Tile plant = new Tile(Type.PLANT,3);
    Tile nothing = new Tile(Type.NOTHING,0);


    Tile[][] bookshelfT1_4 = {
            { cat, nothing, nothing, nothing, cat },
            { plant, cat, game, cat, game},
            { frame, trophy, cat, book, game},
            { cat, cat, cat, cat, cat},
            { game, cat, book, cat, cat},
            { cat, plant, cat, plant, cat}
    };

    Tile[][] bookshelfF1_4 = {
            { cat, cat, nothing, nothing, cat},
            { plant, cat, game, cat, game},
            { frame, trophy, cat, book, game},
            { plant, trophy, cat, cat, book},
            { frame, cat, book, frame, cat},
            { cat, game, trophy, plant, book}
    };
    Tile[][] bookshelfT1_9 = {
            { cat, trophy, nothing, nothing, cat },
            { plant, cat, game, cat, cat},
            { frame, trophy, cat, book, cat},
            { cat, cat, cat, cat, cat},
            { plant, cat, book, cat, cat},
            { cat, trophy, cat, plant, cat}
    };

    Tile[][] bookshelfF1_9 = {
            {cat, nothing, nothing, nothing, cat},
            {plant, cat, cat, cat, game},
            {cat, trophy, cat, cat, game},
            {plant, trophy, cat, cat, cat},
            {cat, cat, cat, cat, cat},
            {cat, game, nothing, nothing, cat}
    };

    private final CGC49 cgc49 = new CGC49();
    @org.junit.jupiter.api.Test
    void compareRule() {
        Bookshelf bks1 = new Bookshelf();

        bks1.setBookshelf(bookshelfT1_4);
        assertTrue(cgc49.compareRule(bks1, 4));

        bks1.setBookshelf(bookshelfF1_4);
        assertFalse(cgc49.compareRule(bks1, 4));

        bks1.setBookshelf(bookshelfT1_9);
        assertTrue(cgc49.compareRule(bks1, 9));

        bks1.setBookshelf(bookshelfF1_9);
        assertFalse(cgc49.compareRule(bks1, 9));
    }

}