package Model;

import static org.junit.jupiter.api.Assertions.*;
import model.*;
import model.Tile;
import model.Type;
import model.CGC68;
import model.Bookshelf;


class CGC68Test {
    Tile cat = new Tile(Type.CAT,1);
    Tile book = new Tile(Type.BOOK,2);
    Tile game = new Tile(Type.GAME,3);
    Tile frame = new Tile(Type.FRAME,2);
    Tile trophy = new Tile(Type.TROPHY,1);
    Tile plant = new Tile(Type.PLANT,3);
    Tile nothing = new Tile(Type.NOTHING,0);

    Tile[][] bookshelfT1_6 = {
            { cat, book, nothing, nothing, cat },
            { plant, cat, game, cat, game},
            { frame, trophy, cat, book, plant},
            { trophy, game, cat, cat, trophy},
            { game, frame, book, cat, book},
            { book, plant, cat, plant, frame}
    };

    Tile[][] bookshelfF1_6 = {
            { cat, nothing, nothing, nothing, cat},
            { plant, cat, game, book, game},
            { frame, trophy, cat, book, game},
            { trophy, trophy, cat, cat, book},
            { game, cat, book, frame, trophy},
            { book, game, trophy, plant, cat}
    };
    Tile[][] bookshelfT1_8 = {
            { cat, trophy, nothing, nothing, cat },
            { plant, cat, game, cat, cat},
            { frame, trophy, game, book, cat},
            { cat, cat, cat, cat, cat},
            { plant, trophy, book, game, cat},
            { cat, trophy, game, plant, frame}
    };
    Tile[][] bookshelfF1_8 = {
            {cat, nothing, nothing, nothing, plant},
            {plant, cat, nothing, book, game},
            {frame, trophy, nothing, book, trophy},
            {book, trophy, nothing, cat, book},
            {game, cat, nothing, nothing, frame},
            {trophy, game, frame, book, cat}
    };

    private final CGC68 cgc68 = new CGC68();
    @org.junit.jupiter.api.Test
    void compareRule() {
        Bookshelf bks1 = new Bookshelf();

        bks1.setBookshelf(bookshelfT1_6);
        assertTrue(cgc68.compareRule(bks1, 6));

        bks1.setBookshelf(bookshelfF1_6);
        assertFalse(cgc68.compareRule(bks1, 6));

        bks1.setBookshelf(bookshelfT1_8);
        assertTrue(cgc68.compareRule(bks1, 8));

        bks1.setBookshelf(bookshelfF1_8);
        assertFalse(cgc68.compareRule(bks1, 8));
    }

}