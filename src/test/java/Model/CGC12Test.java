package Model;

import model.Bookshelf;
import model.CGC12;
import model.Tile;
import model.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CGC12Test {

    Tile cat = new Tile(Type.CAT,1);
    Tile book = new Tile(Type.BOOK,2);
    Tile game = new Tile(Type.GAME,3);
    Tile frame = new Tile(Type.FRAME,2);
    Tile trophy = new Tile(Type.TROPHY,1);
    Tile plant = new Tile(Type.PLANT,3);
    Tile nothing = new Tile(Type.NOTHING,0);

    /**
     * First test, should be TRUE -> Right to left, with first row empty
     */
    Tile[][] bookshelfT1 = {
            {nothing, nothing, nothing, nothing, nothing},
            {cat, nothing, nothing, nothing, nothing},
            {plant, cat, nothing, nothing, nothing,},
            {frame, trophy, cat, nothing, nothing,},
            {frame, frame, plant, cat, nothing,},
            {cat, plant, trophy, cat, plant}
    };

    /**
     * Second test, should be FALSE
     */
    Tile[][] bookshelfF1 = {
            {nothing, nothing, nothing, nothing, nothing},
            {cat,     nothing, nothing, nothing, nothing},
            {plant,   cat,     nothing, nothing, nothing,},
            {frame,   trophy,  cat,     nothing, nothing,},
            {frame,   frame,   plant,   cat,     cat,},
            {cat,     plant,   trophy,  cat,     plant}

    };

    /**
     * Third test, should be TRUE -> Left to right, with first row not empty
     */
    Tile[][] bookshelfT2 = {
            {nothing, nothing, nothing, nothing, cat},
            {nothing, nothing, nothing, cat, plant},
            {nothing, nothing, cat, plant, frame,},
            {nothing, trophy, cat, trophy, cat,},
            {frame, frame, plant, cat, trophy,},
            {cat, plant, trophy, cat, plant}
    };

    Tile[][] bookshelfF2 = {
            {nothing, nothing, nothing, plant, cat},
            {nothing, nothing, nothing, cat, plant},
            {nothing, nothing, cat, plant, frame,},
            {nothing, trophy, cat, trophy, cat,},
            {frame, frame, plant, cat, trophy,},
            {cat, plant, trophy, cat, plant}
    };

    private final CGC12 cgc12 = new CGC12();

    @org.junit.jupiter.api.Test
    void compareRule() {
        Bookshelf bks1 = new Bookshelf();

        bks1.setBookshelf(bookshelfT1);
        assertTrue(cgc12.compareRule(bks1, 11));

        bks1.setBookshelf(bookshelfF1);
        assertFalse(cgc12.compareRule(bks1, 11));

        bks1.setBookshelf(bookshelfT2);
        assertTrue(cgc12.compareRule(bks1, 11));

        bks1.setBookshelf(bookshelfF2);
        assertFalse(cgc12.compareRule(bks1, 11));
    }



}