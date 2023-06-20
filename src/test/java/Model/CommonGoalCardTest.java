package Model;

import model.CommonGoalCard;
import model.Bookshelf;
import model.ScoringToken;
import model.Tile;
import model.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CommonGoalCardTest {
    final CommonGoalCard cgc2p = new CommonGoalCard(2, 2, 1);
    final CommonGoalCard cgc3p = new CommonGoalCard(7, 3, 1);
    final CommonGoalCard cgc4p = new CommonGoalCard(2, 4, 1);
    final CommonGoalCard cgc2p2 = new CommonGoalCard(9, 2, 2);

    final ScoringToken sc8 = new ScoringToken(8,1);
    final ScoringToken sc6 = new ScoringToken(6,1);
    final ScoringToken sc4 = new ScoringToken(4,1);
    final ScoringToken sc2 = new ScoringToken(2,1);

    Tile cat = new Tile(Type.CAT,1);
    Tile book = new Tile(Type.BOOK,2);
    Tile gameTile = new Tile(Type.GAME,3);
    Tile frame = new Tile(Type.FRAME,2);
    Tile trophy = new Tile(Type.TROPHY,1);
    Tile plant = new Tile(Type.PLANT,3);
    Tile nothing = new Tile(Type.NOTHING,0);

    @Test
    void popScoringToken() { //test if it removes all the ScoringTokens
        //2 players
        ScoringToken scT2 = cgc2p.popScoringToken(); //remove the last ScoringToken
        assertEquals(scT2.getValue(), sc8.getValue());
        assertEquals(scT2.getRomanNumber(), sc8.getRomanNumber());
        scT2 = cgc2p.popScoringToken();
        assertEquals(scT2.getValue(), sc4.getValue());
        assertEquals(scT2.getRomanNumber(), sc4.getRomanNumber());
        assertTrue(cgc2p.getTokenStack().isEmpty());

        //3 players
        ScoringToken scT3 = cgc3p.popScoringToken();
        assertEquals(scT3.getValue(), sc8.getValue());
        assertEquals(scT3.getRomanNumber(), sc8.getRomanNumber());
        scT3 = cgc3p.popScoringToken();
        assertEquals(scT3.getValue(), sc6.getValue());
        assertEquals(scT3.getRomanNumber(), sc6.getRomanNumber());
        scT3 = cgc3p.popScoringToken();
        assertEquals(scT3.getValue(), sc4.getValue());
        assertEquals(scT3.getRomanNumber(), sc4.getRomanNumber());
        assertTrue(cgc3p.getTokenStack().isEmpty());

        // 4 players
        ScoringToken scT4 = cgc4p.popScoringToken();
        assertEquals(scT4.getValue(), sc8.getValue());
        assertEquals(scT4.getRomanNumber(), sc8.getRomanNumber());
        scT4 = cgc4p.popScoringToken();
        assertEquals(scT4.getValue(), sc6.getValue());
        assertEquals(scT4.getRomanNumber(), sc6.getRomanNumber());
        scT4 = cgc4p.popScoringToken();
        assertEquals(scT4.getValue(), sc4.getValue());
        assertEquals(scT4.getRomanNumber(), sc4.getRomanNumber());
        scT4 = cgc4p.popScoringToken();
        assertEquals(scT4.getValue(), sc2.getValue());
        assertEquals(scT4.getRomanNumber(), sc2.getRomanNumber());
        assertTrue(cgc4p.getTokenStack().isEmpty());
    }

    @Test
    void pushScoringToken() { //try to push sc2
        cgc2p.pushScoringToken(sc2);
        assertEquals(sc2.getValue(), cgc2p.getTokenStack().peek().getValue());
        assertEquals(sc2.getRomanNumber(), cgc2p.getTokenStack().peek().getRomanNumber());
    }

    @Test
    void getRomanNumber() {
        assertEquals(1, cgc2p.getRomanNumber());
        assertEquals(2, cgc2p2.getRomanNumber());
    }

    @Test
    void getTokenStackTest(){
        assertNotNull(cgc4p.getTokenStack());
        assertEquals(sc8.getValue(), cgc4p.getTokenStack().peek().getValue());
        assertEquals(sc8.getRomanNumber(),cgc4p.getTokenStack().peek().getRomanNumber());
    }

    @Test
    void getRomanNumberTest(){
        assertEquals(1, cgc4p.getRomanNumber());
    }

    @Test
    void setCGCStrategy() { //exception -> invalid id (id<1 || id>12)
        assertThrows(IllegalStateException.class, ()-> cgc2p2.setCGCStrategy(13));
    }

    @Test
    void compare() {
        Bookshelf bookshelf = new Bookshelf();
        Tile[][] bookshelf1 = {{ cat,      nothing,  nothing,  nothing,  gameTile },
                               { plant,    plant,    gameTile, gameTile, book},
                               { frame,    trophy,   gameTile, cat,      gameTile},
                               { plant,    gameTile, book,     gameTile, book},
                               { gameTile, book,     book,     frame,    cat},
                               { book,     gameTile, trophy,   plant,    plant}};
        bookshelf.setBookshelf(bookshelf1);
        assertTrue(cgc4p.compare(bookshelf));
        assertEquals(cgc4p.getStrategy().compareRule(bookshelf, 2), cgc2p.compare(bookshelf));
    }
}