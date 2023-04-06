package Model;

import model.CGC2;
import model.ScoringToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import model.CommonGoalCard;
import model.Game;

import static org.junit.jupiter.api.Assertions.*;


class CommonGoalCardTest {
    private final Game game = new Game();
    final CommonGoalCard cgc2p = new CommonGoalCard(2, 2, 1);
    final CommonGoalCard cgc3p = new CommonGoalCard(7, 3, 1);
    final CommonGoalCard cgc4p = new CommonGoalCard(10, 4, 1);
    final CommonGoalCard cgc2p2 = new CommonGoalCard(9, 2, 2);

    final ScoringToken sc8 = new ScoringToken(8,1);
    final ScoringToken sc6 = new ScoringToken(6,1);
    final ScoringToken sc4 = new ScoringToken(4,1);
    final ScoringToken sc2 = new ScoringToken(2,1);

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
    void setCGCStrategy() {
     //   assertEquals(cgc2p.getStrategy(), cgc2p2.setCGCStrategy(2).getStrategy());
    }//TODO do this test -> already tested


    @Test
    void compare() {
    } //TODO do this test -> already tested
}