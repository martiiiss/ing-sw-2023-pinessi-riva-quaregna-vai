package Model;

import static org.junit.jupiter.api.Assertions.*;
import model.Tile;
import model.Type;
import model.CGC68;
import model.Bookshelf;


class CGC68Test {
    Tile cat = new Tile(Type.CAT,1);
    Tile cat2 = new Tile(Type.CAT,2);
    Tile book = new Tile(Type.BOOK,1);
    Tile book2 = new Tile(Type.BOOK,2);
    Tile game = new Tile(Type.GAME,1);
    Tile game2 = new Tile(Type.GAME,2);
    Tile frame = new Tile(Type.FRAME,1);
    Tile frame2 = new Tile(Type.FRAME,2);
    Tile trophy = new Tile(Type.TROPHY,1);
    Tile trophy2 = new Tile(Type.TROPHY,2);
    Tile plant = new Tile(Type.PLANT,1);
    Tile plant2 = new Tile(Type.PLANT,2);
    Tile nothing = new Tile(Type.NOTHING,0);

    Tile[][] bookshelfT1_6 = {
            { cat, book, nothing, nothing, cat },
            { plant, cat2, game, cat2, game},
            { frame, trophy, cat, book, plant},
            { trophy, game, cat2, cat, trophy},
            { game, frame, book, plant2, book},
            { book, plant, cat, plant, frame}
    };

    Tile[][] bookshelfF1_6 = {
            { cat, cat, nothing, nothing, cat},
            { plant, cat2, game, book, game2},
            { frame, trophy, cat, book2, game},
            { trophy, trophy2, cat2, cat, book},
            { game, plant, book, frame, trophy},
            { book2, game, trophy, plant, cat2}
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
            {plant, cat2, nothing, book, game},
            {frame, trophy, nothing, book, trophy2},
            {book, trophy, trophy2, cat, book},
            {game, cat, nothing, nothing, frame},
            {trophy, game2, frame, book, cat}
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