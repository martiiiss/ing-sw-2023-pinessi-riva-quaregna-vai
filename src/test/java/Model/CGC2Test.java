package Model;

import model.Bookshelf;
import model.Tile;
import model.Type;
import model.CGC2;


import static org.junit.jupiter.api.Assertions.*;

class CGC2Test {
    private final int MAX_ROW = 6;
    private final int MAX_COLUMN = 5;
    Tile cat = new Tile(Type.CAT,1);
    Tile book = new Tile(Type.BOOK,2);
    Tile game = new Tile(Type.GAME,3);
    Tile frame = new Tile(Type.FRAME,2);
    Tile trophy = new Tile(Type.TROPHY,1);
    Tile plant = new Tile(Type.PLANT,3);
    Tile nothing = new Tile(Type.NOTHING,0);

    //the first 4 bookshelf cover the true cases
    Tile[][] bookshelf1 = {{ cat, nothing, nothing, nothing, nothing },
                          { plant, cat, game, book, game},
                          { frame, trophy, cat, book, game},
                          { plant, trophy, cat, cat, book},
                          { game, cat, book, frame, cat},
                          { book, game, trophy, plant, plant}};
    Tile[][] bookshelf2 = {{ cat, nothing, nothing, nothing, nothing },
                            { plant, cat, game, book, book},
                            { frame, trophy, game, book, game},
                            { plant, trophy, book, cat, book},
                            { game, book, book, frame, cat},
                            { book, game, trophy, plant, plant}};
    Tile[][] bookshelf3 = {{ cat, nothing, nothing, nothing, game },
                            { plant, nothing, game, game, book},
                            { frame, trophy, game, cat, game},
                            { plant, game, book, game, book},
                            { game, book, book, frame, cat},
                            { book, game, trophy, plant, plant}};
    Tile[][] bookshelf4 = {{ cat, nothing, nothing, nothing, nothing },
                            { plant, cat, nothing, book, book},
                            { frame, plant, nothing, game, game},
                            { plant, trophy, plant, cat, book},
                            { game, book, book, plant, frame},
                            { book, game, trophy, plant, plant}};

    //false case
    Tile[][] bookshelfFalse1 = {{ cat,   nothing, nothing, nothing, nothing },
                                { plant, nothing, nothing, book,    book},
                                { frame, plant,   nothing, game,    game},
                                { plant, trophy,  plant,   cat,     book},
                                { game,  book,    book,    game,   frame},
                                { book,  game,    trophy,  plant,   plant}};
    private final CGC2 cgc2 = new CGC2();

    @org.junit.jupiter.api.Test
    void compareRule() {
        Bookshelf bks1 = new Bookshelf();

        bks1.setBookshelf(bookshelf1);
        assertTrue(cgc2.compareRule(bks1 , 2));

        bks1.setBookshelf(bookshelf2);
        assertTrue(cgc2.compareRule(bks1, 2));

        bks1.setBookshelf(bookshelf3);
        assertTrue(cgc2.compareRule(bks1, 2));

        Bookshelf bks4 = new Bookshelf();
        bks4.setBookshelf(bookshelf4);
        assertTrue(cgc2.compareRule(bks4, 2));

        Bookshelf bksf1 = new Bookshelf();
        bksf1.setBookshelf(bookshelfFalse1);
        assertFalse(cgc2.compareRule(bksf1, 2));

    }

    @org.junit.jupiter.api.Test
    void checkDiagonal() {
        assertTrue(cgc2.checkDiagonal(0, 0, bookshelf1, 1));
        assertTrue(cgc2.checkDiagonal(5,0, bookshelf2,-1));
        assertTrue(cgc2.checkDiagonal(4,0, bookshelf3,-1));
        assertTrue(cgc2.checkDiagonal(1,0, bookshelf4,1));
        assertFalse(cgc2.checkDiagonal(0, 0, bookshelf3, 1));
        assertFalse(cgc2.checkDiagonal(5, 0, bookshelfFalse1, -1));
    }
}